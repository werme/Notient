/**
 * Our connection to securesocial, this is where we save the users locally when the users
 * and retrieve information from the different login processes, google, facebook and
 * registering. 
 *
 * This class also handles the tokens for userspecific pages (such as registration 
 * and reset password link)
 *
 */

package service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import models.Token;
import models.User;
import models.Provider;
import play.Application;
import play.Logger;
import scala.Option;
import scala.Some;
import securesocial.core.AuthenticationMethod;
import securesocial.core.Identity;
import securesocial.core.PasswordInfo;
import securesocial.core.SocialUser;
import securesocial.core.IdentityId;
import securesocial.core.java.BaseUserService;
import securesocial.core.providers.utils.GravatarHelper;

public class UserService extends BaseUserService {

    public UserService(Application application) {
        super(application);
    }

    @Override
    public void doDeleteExpiredTokens() {
        List<Token> list = Token.find.where().lt("expireAt", new DateTime().toString()).findList();
        for(Token token : list) {
            token.delete();
        }
    }

    @Override
    public void doDeleteToken(String uuid) {
        Token token = Token.find.byId(uuid);
        if(token != null) {
            token.delete();
        }
    }

    @Override
    public Identity doFind(IdentityId identityId){
        User user;

        user = User.findByEmail(identityId.userId().toLowerCase());
        if(user == null){
            user = User.findByUsername(identityId.userId().toLowerCase());
        }
        if(user == null){
            Provider p = Provider.findById(identityId.userId());
            if(p != null){
                user = User.findByEmail(p.user.email);
            }
        }

        if(user == null) return null;
        SocialUser socialUser = new SocialUser(new IdentityId(user.email, identityId.providerId()),    
            user.firstName, 
            user.lastName, 
            String.format("%s %s", user.firstName, user.lastName),
            Option.apply(user.email), 
            null, 
            new AuthenticationMethod("userPassword"),
            null, 
            null, 
            Some.apply(new PasswordInfo("bcrypt", user.password, null))
        );
        return socialUser;
    }

    @Override
    public Identity doFindByEmailAndProvider(String email, String providerId) {
        User user = User.findByEmail(email.toLowerCase());
        if(user == null)
            return null;
        if(user.hasProvider("userpass")){
            SocialUser socialUser = 
                new SocialUser(new IdentityId(user.email, "userpass"),
                        user.firstName, 
                        user.lastName, 
                        String.format("%s %s", user.firstName, user.lastName),
                        Option.apply(user.email), 
                        null, 
                        new AuthenticationMethod("userPassword"),
                        null, 
                        null, 
                        Some.apply(new PasswordInfo("bcrypt", user.password, null))
                   );  
            return socialUser;
        }
        return null;
    }

    @Override
    public securesocial.core.java.Token doFindToken(String securetoken) {
        Token token = Token.find.byId(securetoken);
        if(token == null) return null;
        securesocial.core.java.Token result = new securesocial.core.java.Token();
        result.uuid = token.uuid;
        result.creationTime = new DateTime(token.createdAt);
        result.email = token.email;
        result.expirationTime = new DateTime(token.expireAt);
        result.isSignUp = token.isSignUp;
        return result;
    }


    /**
     * This method saves and updates the user each time you login.
     */
    @Override
    public Identity doSave(Identity socialUser) {
        String suID = socialUser.identityId().userId();
        String suEmail = socialUser.email().get().toLowerCase();
        String suUsername = null;
        String suProvider = socialUser.identityId().providerId();
        String suFirstName = socialUser.firstName();
        String suLastName = socialUser.lastName();
        String suPassword = null;
        Boolean isProviderUserPass = suProvider.equals("userpass");

        if(isProviderUserPass){
            suUsername = socialUser.identityId().userId().toLowerCase();
            suPassword = socialUser.passwordInfo().get().password();
        }
        User user = User.findByEmail(socialUser.email().get().toLowerCase());

        if(user == null){
            //New user
            user = User.create(new User(suEmail, suFirstName, suLastName, suUsername, suPassword));
            user.addProvider(new Provider(suProvider, suID, user));
            user.updateAvatarUrl();
            return socialUser;
        } 
        user.updateAvatarUrl();

        if(isProviderUserPass){
            //Register or password reset
            if(!user.hasProvider(suProvider)){
                user.update(suFirstName, suLastName, suUsername);
            }
            user.setPassword(suPassword);
        }

        if(!user.hasProvider(suProvider)){
            //New provider
            user.addProvider(new Provider(suProvider, suID, user));
        }
        return socialUser;
    }

    @Override
    public void doSave(securesocial.core.java.Token securetoken) {
        Token token = new Token();
        token.uuid = securetoken.uuid;
        token.email = securetoken.email;
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            token.createdAt = df.parse(securetoken.creationTime.toString("yyyy-MM-dd HH:mm:ss"));
            token.expireAt = df.parse(securetoken.expirationTime.toString("yyyy-MM-dd HH:mm:ss"));
        } catch (ParseException e) {
            Logger.error("UserService.doSave(): ", e);
        }
        token.isSignUp = securetoken.isSignUp;
        token.save();
    }
}
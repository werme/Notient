package service;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import models.Token;
import models.User;
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

public class UserService extends BaseUserService {

    public UserService(Application application) {
        super(application);
    }

    @Override
    public void doDeleteExpiredTokens() {
        if (Logger.isDebugEnabled()) {
            Logger.debug("deleteExpiredTokens...");
        }
        List<Token> list = Token.find.where().lt("expireAt", new DateTime().toString()).findList();
        for(Token localToken : list) {
            localToken.delete();
        }
    }

    @Override
    public void doDeleteToken(String uuid) {
        if (Logger.isDebugEnabled()) {
            Logger.debug("deleteToken...");
            Logger.debug(String.format("uuid = %s", uuid));
        }
        Token localToken = Token.find.byId(uuid);
        if(localToken != null) {
            localToken.delete();
        }
    }

    @Override
    //public Identity doFind(UserId userId) {
    public Identity doFind(IdentityId identityId){
        if (Logger.isDebugEnabled()) {
            Logger.debug(String.format("finding by Id = %s", identityId.userId()));

        }

        //Might should be findByEmail
        User localUser;

        localUser = User.findById(identityId.userId());
        if(localUser == null){
            localUser = User.findByEmail(identityId.userId().toLowerCase());
        }
        if(localUser == null){
            localUser = User.findByUsername(identityId.userId().toLowerCase());
        }

        Logger.debug(String.format("localUser = " + localUser));
        if(localUser == null) return null;
        SocialUser socialUser = new SocialUser(new IdentityId(localUser.id, localUser.provider),    
            localUser.firstName, 
            localUser.lastName, 
            String.format("%s %s", localUser.firstName, localUser.lastName),
            Option.apply(localUser.email), 
            null, 
            new AuthenticationMethod("userPassword"),
            null, 
            null, 
            Some.apply(new PasswordInfo("bcrypt", localUser.password, null))
        );  
        if (Logger.isDebugEnabled()) {
            Logger.debug(String.format("socialUser = %s", socialUser));
        }
        return socialUser;
    }


    @Override
    public Identity doFindByEmailAndProvider(String email, String providerId) {
        List<User> list = User.find.where().eq("email", email).eq("provider", providerId).findList();
        if(list.size() != 1){
            Logger.debug("found a null in findByEmailAndProvider..." + "Provider: " + providerId + " Email: " + email + " #Results: " + list.size());
            return null;
        }
        //Logger.debug("Provider: "+ list.get(0).provider + " Password: " + list.get(0).password);
        User localUser = list.get(0);
        SocialUser socialUser = 
                new SocialUser(new IdentityId(localUser.id, localUser.provider),
                        localUser.firstName, 
                        localUser.lastName, 
                        String.format("%s %s", localUser.firstName, localUser.lastName),
                        Option.apply(localUser.email), 
                        null, 
                        new AuthenticationMethod("userPassword"),
                        null, 
                        null, 
                        Some.apply(new PasswordInfo("bcrypt", localUser.password, null))
                   );  
        return socialUser;
    }

    @Override
    public securesocial.core.java.Token doFindToken(String token) {
        if (Logger.isDebugEnabled()) {
            Logger.debug("findToken...");
            Logger.debug(String.format("token = %s", token));
        }
        Token localToken = Token.find.byId(token);
        if(localToken == null) return null;
        securesocial.core.java.Token result = new securesocial.core.java.Token();
        result.uuid = localToken.uuid;
        result.creationTime = new DateTime(localToken.createdAt);
        result.email = localToken.email;
        result.expirationTime = new DateTime(localToken.expireAt);
        result.isSignUp = localToken.isSignUp;
        if (Logger.isDebugEnabled()) {
            Logger.debug(String.format("foundToken = %s", result));
        }
        return result;
    }

    @Override
    public Identity doSave(Identity user) {
        if (Logger.isDebugEnabled()) {
            Logger.debug("save...!_!");
            Logger.debug(String.format("user = %s", user));
        }
        User localUser = null;
        localUser = User.find.byId(user.identityId().userId());
        
        /*
        Logger.debug("id = " + user.identityId().userId() + "  " + user.identityId().userId().toLowerCase());
        Logger.debug("provider = " + user.identityId().providerId());
        Logger.debug("firstName = " + user.firstName());
        Logger.debug("lastName = " + user.lastName());
        Logger.debug(user.fullName() + "");
        Logger.debug("email = " + user.email());
        Logger.debug(user.email().getClass() + "");
        Logger.debug(user.avatarUrl() + "");
        */

        if (localUser == null) {
            Logger.debug("adding new...");
            localUser = new User();
            localUser.id = user.identityId().userId().toLowerCase();
            localUser.provider = user.identityId().providerId();
            localUser.firstName = user.firstName();
            localUser.lastName = user.lastName();

            if(user.avatarUrl() instanceof scala.Some){
                localUser.avatarUrl = user.avatarUrl().get();
            }
            //Temporary solution for twitter which does not have email in OAuth answer
            if(user.email() instanceof scala.Some){
                localUser.email = user.email().get().toLowerCase();
            }
            if(user.passwordInfo() instanceof scala.Some){
                localUser.password = user.passwordInfo().get().password();
            }
            localUser.save();
        } else {
            Logger.debug("existing one...");
            localUser.id = user.identityId().userId().toLowerCase();
            localUser.provider = user.identityId().providerId();
            localUser.firstName = user.firstName();
            localUser.lastName = user.lastName();
            
            if(user.avatarUrl() instanceof scala.Some){
                localUser.avatarUrl = user.avatarUrl().get();
            }

            //Temporary solution for twitter which does not have email in OAuth answer
            if(user.email() instanceof scala.Some){
                localUser.email = user.email().get().toLowerCase();
            }
            if(user.passwordInfo() instanceof scala.Some){
                localUser.password = user.passwordInfo().get().password();
            }
            localUser.update();
        }
        return user;
    }

    @Override
    public void doSave(securesocial.core.java.Token token) {
        Token localToken = new Token();
        localToken.uuid = token.uuid;
        localToken.email = token.email;
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            localToken.createdAt = df.parse(token.creationTime.toString("yyyy-MM-dd HH:mm:ss"));
            localToken.expireAt = df.parse(token.expirationTime.toString("yyyy-MM-dd HH:mm:ss"));
        } catch (ParseException e) {
            Logger.error("UserService.doSave(): ", e);
        }
        localToken.isSignUp = token.isSignUp;
        localToken.save();
    }
}
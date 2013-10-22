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
            Logger.debug("THIS IS IDENTITY: " + identityId);

        }
        //Might should be findByEmail
        User localUser;

        localUser = User.findByEmail(identityId.userId().toLowerCase());
        if(localUser == null){
            localUser = User.findByUsername(identityId.userId().toLowerCase());
        }
        if(localUser == null){
            Logger.debug("searching for provider data with id: " + identityId.userId());
            Provider p = Provider.findById(identityId.userId());
            Logger.debug("found provider: " + p);
            if(p != null){
                localUser = User.findByEmail(p.user.email);
            }
        }


        //localUser = User.findByEmail("notient1@gmail.com");

        Logger.debug(String.format("localUser = " + localUser));
        if(localUser == null) return null;
        SocialUser socialUser = new SocialUser(new IdentityId(localUser.email, identityId.providerId()),    
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
        Logger.debug("doFindByEmailAndProvider WAS USED!!!");
        User localUser = list.get(0);
        SocialUser socialUser = 
                new SocialUser(new IdentityId(localUser.email, localUser.provider),
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

        localUser = User.findByEmail(user.email().get().toLowerCase());
        Logger.debug("THIS LOCALUSER WAS FOUND ON DO SAVE: " + localUser);
        /*
        if(localUser == null){
            //user have never ever logged in, add him to the database. with provider info
        } else {
            //User have logged in before.
            if(localUser.providers.contains("userpass") || localUser.providers.contains(user.identityId().providerId())){
                //user is already registered. Nothing to do update or save.
            } else {
                if(user.identityId().providerId().equals("userpass")){
                    //Update the old social media information, user have registered!
                } else {
                    //User logged in with a new media, add it to the user.

                    //Provider should be added to provider list along with the id.
                }
            }
        }*/

                Logger.debug("id = " + user.identityId().userId() + "  " + user.identityId().userId().toLowerCase());
        Logger.debug("provider = " + user.identityId().providerId());
        Logger.debug("firstName = " + user.firstName());
        Logger.debug("lastName = " + user.lastName());
        Logger.debug(user.fullName() + "");
        Logger.debug("email = " + user.email());
        Logger.debug(user.email().getClass() + "");
        Logger.debug(user.avatarUrl() + "");
        

        if(localUser == null){
            //user have never ever logged in, add him to the database. with provider info
            Logger.debug("adding new..." + user.identityId().providerId());
            localUser = new User();

            //Provider should be added to provider list along with the id.

            //Genererate some username?
            localUser.email = user.email().get().toLowerCase();
            Provider p = new Provider(user.identityId().providerId(), user.identityId().userId(), localUser);
            Logger.debug(p.toString());
            localUser.addProvider(p);

            if(user.identityId().providerId().equals("userpass")){
                localUser.username = user.identityId().userId().toLowerCase();
            }
            //localUser.provider = user.identityId().providerId();

            localUser.firstName = user.firstName();
            localUser.lastName = user.lastName();
            

            if(user.avatarUrl() instanceof scala.Some){
                localUser.avatarUrl = user.avatarUrl().get();
            }

            //If user registered add the password
            if(user.passwordInfo() instanceof scala.Some){
                localUser.password = user.passwordInfo().get().password();
            }
            localUser.save();
        } else {
            //User have logged in before.
            if(localUser.hasProvider(user.identityId().providerId())){
                Logger.debug("User already registered this medium, nothing to save!");
                //user is already registered or logged in with this medium. Nothing to do update or save.
            } else {
                if(user.identityId().providerId().equals("userpass")){
                    //Update the old social media information, user have registered!
                    Logger.debug("Already existing user registered! Logging in via form");
                    localUser.username = user.identityId().userId().toLowerCase();
                    localUser.firstName = user.firstName();
                    localUser.lastName = user.lastName();
                    
                    if(user.avatarUrl() instanceof scala.Some){
                        localUser.avatarUrl = user.avatarUrl().get();
                    }

                    if(user.passwordInfo() instanceof scala.Some){
                        localUser.password = user.passwordInfo().get().password();
                    }
                    localUser.update();

                } else {
                    Logger.debug("Already existing user logged in with new media!");
                    //User logged in with a new media, add it to the user.
                    //Provider should be added to provider list along with the id.            
                    Provider p = new Provider(user.identityId().providerId(), user.identityId().userId(), localUser);
                    Logger.debug(p.toString());
                    localUser.addProvider(p);
                    localUser.save();
                }
            }
        } 
        return user;
    }

    @Override
    public void doSave(securesocial.core.java.Token token) {
        Logger.debug("doSave TOKEN WAS CALLED!!!");
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
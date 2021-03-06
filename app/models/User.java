/**
 * A user is created when logging in via a socialmedia or registering process from securesocial.
 */

package models;

import java.util.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.*;
import play.db.ebean.Model;
import play.Logger;

import securesocial.core.java.SecureSocial;
import securesocial.core.Identity;

import scala.Some;

import securesocial.core.providers.utils.GravatarHelper;
import com.avaje.ebean.Expr;

@Table(
        uniqueConstraints=
            @UniqueConstraint(columnNames={"email"}))
@Entity
public class User extends Model {

    private static final long serialVersionUID = 1L;

    @Id
    public Long id;

    public String email;

    @OneToMany(cascade=CascadeType.ALL)
    public List<Provider> providers = new ArrayList<Provider>();

    public String firstName;

    public String lastName;

    public String password;

    public String avatarUrl;

    public String privilege = PrivilegeLevel.USER;

    @MinLength(5)
    @MaxLength(20)
    public String username;

    public static Finder<String, User> find = new Finder<String, User>(
            String.class, User.class);

    public User (String email, String firstName, String lastName){
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        save();
    }

    public User (String email, String firstName, String lastName, String username, String password){
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        save();
    }

    public static User create(User user){
        user.save();
        return user;
    }
    
    public void update(String firstName, String lastName, String username){
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.update();
    }

    public void setPassword(String password){
        this.password = password;
        update();
    }

    public String displayName() {
        if(username != null) {
            return username;
        } else if (firstName != null && lastName != null) { 
            return firstName + " " + lastName; 
        } else if (email != null) {
            return email;
        } else {
            return "Unknown user";
        }
    }
    
    public void addProvider(Provider provider){
        providers.add(provider);
        this.save();
    }

    public boolean hasProvider(String provider){
        for (Provider registeredProvider : providers) {
            if(registeredProvider.getProvider().equals(provider))
                return true;
        }
        return false;
    }

    public static User findByEmail(String email) {
        return find.where().eq("email", email).findUnique();
    }

    public static User findByUsername(String username) {
        return find.where().eq("username", username).findUnique();
    }

    /**
     * Returns the currentUser in a static sense. Used by the views
     * to get which user is currently logged in.
     */
    public static User currentUser(){
        Identity identity = SecureSocial.currentUser();
        User user = null;
        if (identity != null) {
           user = User.findByEmail(identity.identityId().userId());
        }
        return user;
    }

    public static boolean userSignedIn() {
        return currentUser() == null ? false : true;
    }

    /**
     * Returns a list of users related to the search query.
     * Will look at names and usernames.
     */
    public static List<User> searchUsers(String query) {
        List<User> result = new ArrayList<User>();
        for (String word : query.split("\\s")) {
            List<User> wordResult = find.where()
                .or(Expr.or(Expr.like("firstName", "%"+word+"%"), Expr.ilike("lastName", "%"+word+"%")), Expr.ilike("userName", "%"+word+"%"))
                .findList();
            for (User user : wordResult) {
                if (!result.contains(user))
                    result.add(user);
            }
        }
        return result;
    }

    /**
     * Return total upVotes of all users posts.
     */ 
    public int getScore(){
        List<Note> allNotes = Note.byAuthor(this);
        int totalScore = 0;
        for(Note note : allNotes){
            totalScore += note.getScore();
        }
        return totalScore;
    }

    public String getAvatarUrl() {
        return avatarUrl != null ? avatarUrl : "https://sigil.cupcake.io/" + displayName();
    }

    public void updateAvatarUrl(){
        if(avatarUrl == null && GravatarHelper.avatarFor(email) instanceof scala.Some){
                avatarUrl = GravatarHelper.avatarFor(email).get();
        }
        update();
    }

    @Override
    public String toString() {
        return this.email + " - " + this.firstName;
    }
}

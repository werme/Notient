package models;

import play.data.validation.Constraints.*;

import javax.persistence.*;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;
import securesocial.core.java.SecureSocial;
import securesocial.core.Identity;
import play.Logger;



@Table(
	    uniqueConstraints=
	        @UniqueConstraint(columnNames={"username"}))
@Entity
public class User extends Model {

	    private static final long serialVersionUID = 1L;

    @Id
    public String id;

    public String provider;

    public String firstName;

    public String lastName;

    public String email;

    public String password;

    public String avatarUrl;

    public String privilege = PrivilegeLevel.USER;

	@MinLength(5)
	@MaxLength(20)
	public String username;

	public static Finder<String, User> find = new Finder<String, User>(
			String.class, User.class);
	
	public static User findById(String id) {
		return find.where().eq("id", id).findUnique();
	}
    public static User findByEmail(String email) {
        return find.where().eq("email", email).findUnique();
    }

    public static User findByUsername(String username) {
        return find.where().eq("username", username).findUnique();
    }

    public static User currentUser(){
        Identity identity = SecureSocial.currentUser();
        User localUser = null;
        if (identity != null){
           localUser = User.find.byId(identity.identityId().userId());
        }
        Logger.debug("identity: " + identity);
        return localUser;
    }

	@Override
    public String toString() {
        return this.id + " - " + this.firstName;
    }
}

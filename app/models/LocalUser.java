package models;

import play.data.validation.Constraints.*;

import javax.persistence.*;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Table(
	    uniqueConstraints=
	        @UniqueConstraint(columnNames={"username"}))
@Entity
public class LocalUser extends Model {

	    private static final long serialVersionUID = 1L;

    @Id
    public String id;

    public String provider;

    public String firstName;

    public String lastName;

    public String email;

    public String password;

    public String avatarUrl;

    public String privilege = "user";

	@MinLength(5)
	@MaxLength(20)
	public String username;

	public static Finder<String, LocalUser> find = new Finder<String, LocalUser>(
			String.class, LocalUser.class);
	
	public static LocalUser findById(String id) {
		return find.where().eq("id", id).findUnique();
	}
    public static LocalUser findByEmail(String email) {
        return find.where().eq("email", email).findUnique();
    }

    public static LocalUser findByUsername(String username) {
        return find.where().eq("username", username).findUnique();
    }
	@Override
    public String toString() {
        return this.id + " - " + this.firstName;
    }
}

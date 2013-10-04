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
public class User extends Model {

	    private static final long serialVersionUID = 1L;

    @Id
    public String id;

    public String provider;

    public String firstName;

    public String lastName;

    public String email;

    public String password;

	@MinLength(5)
	@MaxLength(20)
	public String username;

	public static Finder<String, User> find = new Finder<String, User>(
			String.class, User.class);
	
	public static User findById(String id) {
		return find.where().eq("id", id).findUnique();
	}
	@Override
    public String toString() {
        return this.id + " - " + this.firstName;
    }
}

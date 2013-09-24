package models;

import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

import play.db.ebean.*;

import com.avaje.ebean.*;

@Entity
public class User extends Model {

	@Id
	public String email;
	@Min(4)
	@Max(16)
	public String username;
	public String password;
	
	public User(String email, String username, String password){
		this.email = email;
		this.username = username;
		this.password = password;
	}
    public static Finder<String,User> find = new Finder<String,User>(
            String.class, User.class
    );
    public static User authenticate(String email, String password) {
        return find.where().eq("email", email)
            .eq("password", password).findUnique();
    }
	public static User findByEmail(String email) {
		return find.where().eq("email", email).findUnique();
	}
	
	public static void deleteUser(String email){
		find.ref(email).delete();
	}
}

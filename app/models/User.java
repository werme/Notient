package models;

import play.data.validation.*;
import javax.persistence.*;
import play.db.ebean.*;

@Entity
public class User extends Model {

	@Id
	public String email;

	@Constraints.MinLength(5)
	@Constraints.MaxLength(20)
	public String username;
	public String password;

	public User(String email, String username, String password) {
		this.email = email;
		this.username = username;
		this.password = password;
	}

	public static Finder<String, User> find = new Finder<String, User>(
			String.class, User.class);

	public static User authenticate(String email, String password) {
		return find.where().eq("email", email).eq("password", password)
				.findUnique();
	}

	public static User findByEmail(String email) {
		return find.where().eq("email", email).findUnique();
	}

	public static void deleteUser(String email) {
		find.ref(email).delete();
	}
}

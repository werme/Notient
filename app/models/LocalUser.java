package models;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import play.db.ebean.Model;
import play.data.validation.*;
import play.data.format.*;

public class LocalUser extends Model {

    @Id
    public String id;
    public String name;
    public String email;
    public String password;
    public String provider;
        public String firstName;
        public String lastName;

        public static Finder<String, LocalUser> find = new Finder<String, LocalUser>(String.class,LocalUser.class);


/**
 * Retrieve a User using an email.
 */
    public static LocalUser findByEmail(String email) {
        return find.where().eq("email", email).findUnique();
    }
}
package models;
import play.db.ebean.*;
import javax.persistence.*;

@Entity
public class Provider extends Model {

    @Id
    public String uniqueID;

    public String provider;
    public String id;
    public String email;

    public Provider(String provider, String id, String email){
        this.provider = provider;
        this.id = id;
        this.email = email;
    }

    public String getProvider(){
        return provider;
    }

    public static Finder<String, Provider> find = new Finder<String, Provider>(
        String.class, Provider.class);

    public static Provider findById(String id) {
        return find.where().eq("id", id).findUnique();
    }
    @Override
    public String toString() {
        return "Id: " + this.id + " - provider: "+ this.provider + " - email: " + this.email;
    }
}
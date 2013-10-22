package models;
import play.db.ebean.*;
import javax.persistence.*;

@Entity
public class Provider extends Model {

    @Id
    public String uniqueID;


    public String provider;
    public String id;
    
    @ManyToOne(cascade=CascadeType.ALL)
    public User user;

    public Provider(String provider, String id, User user){
        this.provider = provider;
        this.id = id;
        this.user = user;
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
        return "Id: " + this.id + " - provider: "+ this.provider + " - user: " + this.user;
    }
}
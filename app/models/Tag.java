package models;

import java.util.*;
import play.db.ebean.*;
import play.data.validation.Constraints.*;
import play.data.format.Formats.*;
import javax.persistence.*;

@Entity
public class Tag extends Model {

  @Id
  public Long id;

  @Required
  @NonEmpty
  @MinLength(3)
  @MaxLength(40)
  public String title;

  public Tag(String title) {
    this.title = title;
  }

  public static Finder<Long, Tag> find = new Finder(Long.class, Tag.class);

  public static List<Tag> all() {
    return find.all();
  }

  public static void create(Tag tag) {
    tag.save();
  }

  public static void delete(Long id) {
    find.ref(id).delete();
  }
}

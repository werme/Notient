package models;

import java.util.*;
import play.db.ebean.*;
import play.data.validation.Constraints.*;
import play.data.format.Formats.*;
import javax.persistence.*;
import java.util.regex.PatternSyntaxException;

@Entity
public class Comment extends Model {

  @Id
  public Long id;

  @Required
  public String author;

  @Required
  public Date postedAt;

  @Required
  @NonEmpty
  @MaxLength(140)
  public String content;

  @ManyToOne
  @Required
  public Note note;

  public Comment(Note note, String author, String content) {
      this.note = note;
      this.author = author;
      this.content = content;
      this.postedAt = new Date();
  }

  public static Finder<Long, Comment> find = new Finder(Long.class, Comment.class);

  public String toString() {
      return content.length() > 50 ? content.substring(0, 50) + "..." : content;
  }
}

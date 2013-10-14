package models;

import java.util.*;
import play.db.ebean.*;
import play.data.validation.Constraints.*;
import play.data.format.Formats.*;
import javax.persistence.*;
import java.util.regex.PatternSyntaxException;
import play.Logger;

@Entity
public class Comment extends Model {

  @Id
  public Long id;

  //@Required
  public String author;

  //@Required
  public Date postedAt;

  @Required
  @NonEmpty
  @MaxLength(140)
  public String content;

  //@Required
  @ManyToOne
  public Note note;

  public Comment(String content, LocalUser author) { // User key?
    this.author = author.id;
    this.content = content;
    this.postedAt = new Date();
  }

  public Comment(Long noteId, String content, LocalUser author) { // User key?
    this.note = Note.find.ref(noteId);
    this.author = author.id;
    this.content = content;
    this.postedAt = new Date();
  }

  public static Finder<Long, Comment> find = new Finder(Long.class, Comment.class);

  public static Comment create(Long noteId, Comment comment) {
    comment.note = Note.find.ref(noteId);
    comment.save();
    return comment;
  }

  public static Comment create(Comment comment) {
    comment.save();
    return comment;
  }

  public String toString() {
    return content.length() > 50 ? content.substring(0, 50) + "..." : content;
  }
}

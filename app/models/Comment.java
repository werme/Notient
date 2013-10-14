package models;

import java.util.*;
import play.db.ebean.*;
import play.data.validation.Constraints.*;
import play.data.format.Formats.*;
import javax.persistence.*;
import java.util.regex.PatternSyntaxException;
import play.Logger;

import securesocial.core.java.SecureSocial;
import securesocial.core.Identity;

@Entity
public class Comment extends Model {

  @Id
  public Long id;

  //@Required
  public User author;

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
    this.content = content;
    this.postedAt = new Date();
  }

  public Comment(Long noteId, String content, LocalUser author) { // User key?
    this.note = Note.find.ref(noteId);
    this.content = content;
    this.postedAt = new Date();
  }

  public static Finder<Long, Comment> find = new Finder(Long.class, Comment.class);

  public static Comment create(Long noteId, Comment comment, LocalUser author) {
    comment.note = Note.find.ref(noteId);
    comment.author = author.firstName + " " + author.lastName;
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

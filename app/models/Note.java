package models;

import java.util.*;

import play.db.ebean.*;
import play.data.validation.Constraints.*;

import javax.persistence.*;

@Entity
public class Note extends Model {

  @Id
  public Long id;

  @Required
  public String text;

  public Note(String text) {
    this.text = text;
  }

  public static Finder<Long,Note> find = new Finder(Long.class, Note.class);

  public static List<Note> all() {
    return find.all();
  }

  public static void create(Note note) {
    note.save();
  }

  public static void delete(Long id) {
    find.ref(id).delete();
  }
}

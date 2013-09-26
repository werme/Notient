package models;

import java.util.*;
import play.db.ebean.*;
import play.data.validation.Constraints.*;
import play.data.format.Formats.*;
import javax.persistence.*;

@Entity
public class Note extends Model {

	@Id
	public Long id;

	@Required
	@NonEmpty
	@MinLength(2)
	@MaxLength(30)
	public String title;

	public String text;

	@ManyToMany
	public List<Tag> tags = new ArrayList<Tag>();

	public Note(String title) {
		this.title = title;
	}

	public Note(String title, String text) {
		this.title = title;
		this.text = text;
	}

	public static Finder<Long, Note> find = new Finder(Long.class, Note.class);

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

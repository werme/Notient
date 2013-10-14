package models;

import java.util.*;
import play.db.ebean.*;
import play.data.validation.Constraints.*;
import play.data.format.Formats.*;
import play.Logger;
import javax.persistence.*;
import securesocial.core.java.SecureSocial;

@Entity
public class Note extends Model {

	@Id
	public Long id;

	@Required
	@NonEmpty
	@MinLength(2)
	@MaxLength(30)
	public String title;

  @Column(columnDefinition = "TEXT")
	public String content;

	public String author;

	@ManyToMany(cascade = CascadeType.REMOVE)
	public List<Tag> tags = new ArrayList<Tag>();

	@OneToMany(mappedBy="note", cascade=CascadeType.ALL)
	public List<Comment> comments = new ArrayList<Comment>();

	public Note(String title, LocalUser author) {
		Logger.debug("HEJHEJ " + author);
		this.title = title;
		this.author = author.id;
	}

	public Note(String title, String content, LocalUser author) {
		Logger.debug("HEJHEJ " + author);
		this.title = title;
		this.content = content;
		this.author = author.id;
	}

	public static Finder<Long, Note> find = new Finder(Long.class, Note.class);

    public static List<Note> notesBy(LocalUser author) {
        return find.where()
            .eq("author", author.id)
            .findList();
    }
    
	public static List<Note> all() {
		return find.all();
	}

	public static Note create(Note note) {
		note.save();
		return note;
	}

	public static Note create(Note note, String tagsList) {
		note.save();
		if(tagsList != null && !tagsList.equals("") && !tagsList.equals(" ")) {
			note.tags.addAll(Tag.createOrFindAllFromString(tagsList));
			note.saveManyToManyAssociations("tags");
		}
		return note;
	}

	public static void addTag(Long id, Tag tag) {
		Note note = find.ref(id);
		note.tags.add(tag);
		note.saveManyToManyAssociations("tags");
	}

	public static void delete(Long id) {
		Note note = find.ref(id);
		note.delete();

		Tag.clean();
	}

	public Note addComment(String content, LocalUser author) {
    Comment comment = new Comment(id, content, author);
    this.comments.add(comment);
    this.save();
    return this;
  }

	public String extract(int length) {
		if(content != null) {
		  return content.length() > length ? content.substring(0, length-1) + " ..." : content;
		}
		return null;
	}
}

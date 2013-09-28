package models;

import com.avaje.ebean.Ebean;

import org.junit.Before;
import org.junit.Test;

import play.libs.Yaml;
import play.test.WithApplication;

import java.util.*;

import static org.junit.Assert.*;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;

public class CommentsTest extends WithApplication {

	@Before
	public void setUp() {
		start(fakeApplication(inMemoryDatabase()));
		Ebean.save((List) Yaml.load("test-data.yml"));
	}

	@Test
	public void createNote() {
		Note.create(new Note("My note", User.findByEmail("test@notes.com")));
    	Note note = Note.find.where().eq("title", "My note").findUnique();

		new Comment(note, "Jeff", "Nice post").save();
		new Comment(note, "Tom", "Awesome").save();

		List<Comment> commentsOnNote = note.comments;

		assertEquals(2, commentsOnNote.size());

		Comment firstComment = commentsOnNote.get(0);
		assertNotNull(firstComment);
		assertEquals("Jeff", firstComment.author);
		assertEquals("Nice post", firstComment.content);
		assertNotNull(firstComment.postedAt);

		Comment secondComment = commentsOnNote.get(1);
		assertNotNull(secondComment);
		assertEquals("Tom", secondComment.author);
		assertEquals("Awesome", secondComment.content);
		assertNotNull(secondComment.postedAt);
	}

}

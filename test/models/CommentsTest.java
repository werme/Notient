package models;

import com.avaje.ebean.Ebean;

import org.junit.Before;
import org.junit.Test;

import play.libs.Yaml;
import play.test.WithApplication;

import play.Logger;

import java.util.*;

import static org.junit.Assert.*;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;

public class CommentsTest extends WithApplication {

	LocalUser testUser;
	
	@Before
	public void setUp() {
		start(fakeApplication(inMemoryDatabase()));
		Ebean.save((List) Yaml.load("test-data.yml"));
		testUser = LocalUser.findById("1234567890");
	}

	@Test
	public void createComment() {
		Note.create(new Note("My note", testUser));
    	Note note = Note.find.where().eq("title", "My note").findUnique();

    	Comment.create(note.id, new Comment("Awesome post"), testUser);
    	Comment myComment = Comment.find.where().eq("content", "Awesome post").findUnique();
    	assertNotNull(myComment);
    	assertEquals("Awesome post", myComment.content);
	}

	@Test
	public void deleteComment() {
		Note.create(new Note("Delete this", testUser));
    	Note note = Note.find.where().eq("title", "Delete this").findUnique();

    	Comment.create(note.id, new Comment("Will do"), testUser);
    	Comment myComment = Comment.find.where().eq("content", "Will do").findUnique();

    	Comment.delete(myComment.id);
    	Comment myDeletedComment = Comment.find.where().eq("content", "Will do").findUnique();
    	assertNull(myDeletedComment);
	}
}

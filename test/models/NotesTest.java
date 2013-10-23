package models;

import com.avaje.ebean.Ebean;

import org.junit.Before;
import org.junit.Test;

import play.libs.Yaml;
import play.test.WithApplication;

import java.util.*;

import play.Logger;
import static org.junit.Assert.*;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;

public class NotesTest extends WithApplication {

  User testUser;
  User testUser1;
  User testUser2;

  @Before
  public void setUp() {
    start(fakeApplication(inMemoryDatabase()));
    Ebean.save((List) Yaml.load("test-data.yml"));
    testUser = User.findByEmail("pingu@notient.com");
    testUser1 = User.findByEmail("test1@notient.com");
    testUser2 = User.findByEmail("test2@notient.com");
  }

  @Test
  public void createAndRetrieveNote() {
    String title = "My test note title";
    String content = "My test note content";
    Long id = Note.create(new Note(title, content), testUser).id;

    Note note = Note.find.ref(id);
    assertNotNull(note);
    assertEquals(title, note.title);
  }

  @Test
  public void addTagToNote() {
  }

  @Test
  public void updateNote() {
    // TODO
  }

  @Test
  public void deleteNote() {
    Note myNote = Note.find.where().eq("title", "Test note title").findUnique();
    Note.delete(myNote.id);

    Note myDeletedNote = Note.find.where().eq("title", "Test note title").findUnique();
    assertNull(myDeletedNote);
  }

  @Test
  public void byAuthor() {
    assertEquals(0, Note.byAuthor(testUser).size());
    Note.create(new Note("My first note", "My test content"), testUser);
    Note.create(new Note("My second note", "My test content"), testUser);
    assertEquals(2, Note.byAuthor(testUser).size());
  }

  @Test
  public void vote(){
    Long id = Note.create(new Note("Note with votes", "Vote note content"), testUser).id;
    Note voteNote = Note.find.ref(id);

    //Check if user can toggle up vote correctly;
    voteNote.toggleUpVote(testUser);
    assertEquals(voteNote.getVoteStatus(testUser), 1);
    voteNote.toggleUpVote(testUser);
    assertEquals(voteNote.getVoteStatus(testUser), 0);

    //Total score of vote should be equals to 2.
    voteNote.toggleUpVote(testUser);
    voteNote.toggleUpVote(testUser1);
    assertEquals(voteNote.getScore(),2);

    //With one a down vote we should have a score of 1
    voteNote.toggleDownVote(testUser2);
    assertEquals(voteNote.getScore(),1);

    //Switch toggle testUsers vote and we should have a score of -1.
    voteNote.toggleDownVote(testUser);
    assertEquals(voteNote.getVoteStatus(testUser), -1);
    assertEquals(voteNote.getScore(),-1);

    Long id2 = Note.create(new Note("Second voteNote", "My test content"), testUser).id;

    Note voteNote2 = Note.find.ref(id2);

    assertEquals(voteNote2.getScore(), 0);
  }
}
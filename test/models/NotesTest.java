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

public class NotesTest extends WithApplication {

  User testUser;
  User testUser1;
  User testUser2;

  @Before
  public void setUp() {
    start(fakeApplication(inMemoryDatabase()));
    Ebean.save((List) Yaml.load("test-data.yml"));
    testUser = User.findById("1234567890");
    testUser1 = User.findById("1234567891");
    testUser2 = User.findById("1234567892");
  }

  @Test
  public void createAndRetrieveNote() {
    Note.create(new Note("My note", testUser));
    Note myNote = Note.find.where().eq("title", "My note").findUnique();
    assertNotNull(myNote);
    assertEquals("My note", myNote.title);
  }

  @Test
  public void addTagToNote() {
    Note myNote = Note.find.where().eq("title", "Test note title").findUnique();
    Note.addTag(myNote.id, Tag.findByTitle("Test tag title"));

    assertEquals("Test tag title", myNote.tags.get(0).title);
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
  public void notesBy() {
      Note.create(new Note("My first note", testUser));
      Note.create(new Note("My second note", testUser));

      List<Note> results = Note.notesBy(testUser);
      assertEquals(2, results.size());
      assertEquals("My first note", results.get(0).title);
  }
  @Test
  public void vote(){
    Note.create(new Note("Note with votes", testUser));
    Note voteNote = Note.find.where().eq("title", "Note with votes").findUnique();

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

  }
}
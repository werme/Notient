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

  @Before
  public void setUp() {
    start(fakeApplication(inMemoryDatabase()));
    Ebean.save((List) Yaml.load("test-data.yml"));
  }

  @Test
  public void createAndRetrieveNote() {
    new Note("My note").save();
    Note myNote = Note.find.where().eq("title", "My note").findUnique();
    assertNotNull(myNote);
    assertEquals("My note", myNote.title);
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
}
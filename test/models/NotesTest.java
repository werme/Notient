package models;

import com.avaje.ebean.Ebean;
import org.junit.Before;
import org.junit.Test;
import play.libs.Yaml;
import play.test.WithApplication;

import java.util.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
    Note myNote = Note.find.where().eq("text", "My note").findUnique();
    assertNotNull(myNote);
    assertEquals("My note", myNote.text);
  }

  @Test
  public void updateNote() {
    // TODO
  }

  @Test
  public void deleteNote() {
    Note myNote = Note.find.where().eq("text", "Test note").findUnique();
    Note.delete(myNote.id);

    Note myDeletedNote = Note.find.where().eq("text", "Test note").findUnique();
    assertNull(myDeletedNote);
  }
}
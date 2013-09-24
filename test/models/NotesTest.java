import com.avaje.ebean.*;

import java.util.*;

import org.junit.*;

import play.test.WithApplication;
import play.libs.*;

import models.*;

import static org.junit.Assert.*;

import static play.test.Helpers.*;

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
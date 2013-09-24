import models.*;
import org.junit.*;
import static org.junit.Assert.*;
import play.test.WithApplication;
import static play.test.Helpers.*;

public class NotesTest extends WithApplication {

  @Before
  public void setUp() {
    start(fakeApplication(inMemoryDatabase()));
  }

  @Test
    public void createAndRetrieveUser() {
      new Note("My note").save();
      Note myNote = Note.find.where().eq("text", "My note").findUnique();
      assertNotNull(myNote);
      assertEquals("My note", myNote.text);
    }
}
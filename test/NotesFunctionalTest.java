import com.google.common.collect.ImmutableMap;

import models.Note;

import org.junit.Before;
import org.junit.Test;

import play.mvc.Result;
import play.test.WithApplication;

import java.util.*;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.*;
import static play.test.Helpers.*;

public class NotesFunctionalTest extends WithApplication {

  @Before
  public void setUp() {
    start(fakeApplication(inMemoryDatabase()));
  }

  @Test
  public void createNewNote() {
    Result result = callAction(controllers.routes.ref.Application.newNote());

    // Should return bad request if no data is given
    assertThat(status(result)).isEqualTo(BAD_REQUEST);

    result = callAction(
        controllers.routes.ref.Application.newNote(),
        fakeRequest().withFormUrlEncodedBody(ImmutableMap.of("title", "My note title", "text", "My note content"))
    );

    // Should return redirect status if successful
    assertThat(status(result)).isEqualTo(SEE_OTHER);
    assertThat(redirectLocation(result)).isEqualTo("/");

    Note newNote = Note.find.where().eq("title", "My note title").findUnique();

    // Should be saved to DB
    assertNotNull(newNote);
    assertEquals("My note title", newNote.title);
    assertEquals("My note content", newNote.text);
  }

  @Test
  public void createInvalidNote() {
    String title = "";
    Result result = callAction(
        controllers.routes.ref.Application.newNote(),
        fakeRequest().withFormUrlEncodedBody(ImmutableMap.of("title", title))
    );
    assertThat(status(result)).isEqualTo(BAD_REQUEST);

    title = " ";
    result = callAction(
        controllers.routes.ref.Application.newNote(),
        fakeRequest().withFormUrlEncodedBody(ImmutableMap.of("title", title))
    );
    assertThat(status(result)).isEqualTo(BAD_REQUEST);

    title = "N";
    result = callAction(
        controllers.routes.ref.Application.newNote(),
        fakeRequest().withFormUrlEncodedBody(ImmutableMap.of("title", title))
    );
    assertThat(status(result)).isEqualTo(BAD_REQUEST);

    title = "Lorem ipsum Veniam sunt nulla enim esse incididunt eiusmod qui aliqua dolor nisi";
    result = callAction(
        controllers.routes.ref.Application.newNote(),
        fakeRequest().withFormUrlEncodedBody(ImmutableMap.of("title", title))
    );
    assertThat(status(result)).isEqualTo(BAD_REQUEST);
  }
}
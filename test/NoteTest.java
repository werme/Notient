import org.junit.*;

import java.util.*;

import play.mvc.*;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

public class NoteTest {

  @Test
  public void createNewNote() {
    running(fakeApplication(), new Runnable() {
      public void run() {
        Result result = callAction(controllers.routes.ref.Application.newNote());

        // Should return bad request if no data is given
        assertThat(status(result)).isEqualTo(BAD_REQUEST);

        Map<String,String> data = new HashMap<String,String>();
        data.put("text", "My note");

        result = callAction(
            controllers.routes.ref.Application.newNote(),
            fakeRequest().withFormUrlEncodedBody(data)
        );

        // Should return redirect status if successful
        assertThat(status(result)).isEqualTo(SEE_OTHER);
        assertThat(redirectLocation(result)).isEqualTo("/");
      }
    });
  }
}
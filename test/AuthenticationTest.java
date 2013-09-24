import org.junit.*;
import static org.junit.Assert.*;
import static org.fest.assertions.Assertions.*;
import java.util.*;
import play.mvc.*;
import play.test.WithApplication;
import static play.test.Helpers.*;
import models.*;
import com.google.common.collect.ImmutableMap;
import com.avaje.ebean.*;

public class AuthenticationTest extends WithApplication {

  @Before
  public void setUp() {
    start(fakeApplication(inMemoryDatabase()));
    Ebean.save((List) Yaml.load("test-data.yml"));
  }

  @Test
  public void authenticateSuccess() {
    Result result = callAction(
      controllers.routes.ref.Application.authenticate(), // Move from application controller?
      fakeRequest().withFormUrlEncodedBody(ImmutableMap.of(
          "email", "student@notes.com"
    );

    // Should redirect on successful login
    assertEquals(SEE_OTHER, status(result));
    // Should save user email in session
    assertEquals("student@notes.com", session(result).get("email"));
  }

  @Test
  public void authenticateFail() {
    Result result = callAction(
      controllers.routes.ref.Application.authenticate(), // Move from application controller?
      fakeRequest().withFormUrlEncodedBody(ImmutableMap.of(
          "email", "address@notin.db"
    );

    // Should return bad request on authentication fail
    assertEquals(BAD_REQUEST, status(result));
    // Shouldn't save email to session
    assertNull(session(result).get("email"));
  }
}
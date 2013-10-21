import com.avaje.ebean.Ebean;
import com.google.common.collect.ImmutableMap;

import models.Note;
import service.UserService;

import org.junit.Before;
import org.junit.Test;

import play.libs.Yaml;
import play.mvc.Result;
import play.test.WithApplication;

import java.util.*;
import models.User;
import play.Logger;
import securesocial.core.Authenticator;
import securesocial.core.IdentityId;
import securesocial.core.SocialUser;
import securesocial.core.PasswordInfo;
import scala.Some;
import securesocial.core.AuthenticationMethod;
import scala.Option;
import scala.util.Right;
import scala.util.Either;
import play.mvc.Http.Cookie;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.*;
import static play.test.Helpers.*;

public class SearchFunctionalTest extends WithApplication {

  @Before
  public void setUp() {
    start(fakeApplication(inMemoryDatabase()));
    Ebean.save((List) Yaml.load("test-data.yml"));
  }

  @Test
  public void searchNotes() {
    Result result;

    Cookie cookie = Utils.fakeCookie("pingu@notient.com");

    // Should return redirect status if successful
    result = callAction(
        controllers.routes.ref.Search.performSearch(),
        fakeRequest().withFormUrlEncodedBody(
            ImmutableMap.of("query", "lorem")).withCookies(cookie));

    assertThat(status(result)).isEqualTo(SEE_OTHER);
    assertThat(redirectLocation(result)).isEqualTo("/search/lorem");

    // Should return bad request if no data is given
    result = callAction(
        controllers.routes.ref.Search.performSearch(),
        fakeRequest().withFormUrlEncodedBody(
            ImmutableMap.of("title", "")).withCookies(cookie));
    //assertThat(status(result)).isEqualTo(BAD_REQUEST);
  }
}
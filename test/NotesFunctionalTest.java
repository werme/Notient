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

public class NotesFunctionalTest extends WithApplication {

	@Before
	public void setUp() {
		start(fakeApplication(inMemoryDatabase()));
		Ebean.save((List) Yaml.load("test-data.yml"));
	}

	@Test
	public void createNewNote() {
		Result result;

		Cookie cookie = Utils.fakeCookie("pingu@notient.com");

		result = callAction(
				controllers.routes.ref.Notes.create(),
				fakeRequest().withFormUrlEncodedBody(
						ImmutableMap.of("title", "My note title", "content",
								"My note content")).withCookies(cookie));
		// Should return redirect status if successful
		assertThat(status(result)).isEqualTo(SEE_OTHER);
		assertThat(redirectLocation(result)).isEqualTo("/notes");
		// Should return bad request if no data is given
		result = callAction(
				controllers.routes.ref.Notes.create(),
				fakeRequest().withFormUrlEncodedBody(
						ImmutableMap.of("title", "", "content",
								"")).withCookies(cookie));
		assertThat(status(result)).isEqualTo(BAD_REQUEST);

		Note newNote = Note.find.where().eq("title", "My note title")
				.findUnique();

		// Should be saved to DB
		assertNotNull(newNote);
		assertEquals("My note title", newNote.title);
		assertEquals("My note content", newNote.content);
	}

	@Test
	public void createNoteWithTags() {

		Cookie cookie = Utils.fakeCookie("pingu@notient.com");
		Result result = callAction(
				controllers.routes.ref.Notes.create(),
				fakeRequest().withFormUrlEncodedBody(
						ImmutableMap.of("title", "My note title", "content",
								"My note content", "tagList", "tag1 tag2")).withCookies(cookie));

		// Should return redirect status if successful
		Logger.debug("TEST1");
		assertThat(status(result)).isEqualTo(SEE_OTHER);
		Logger.debug("TEST2");
		assertThat(redirectLocation(result)).isEqualTo("/notes");

		Note newNote = Note.find.where().eq("title", "My note title")
				.findUnique();

		// Tags should have been created and saveed to DB
		assertEquals(2, newNote.tags.size());
		assertEquals("tag1", newNote.tags.get(0).title);
		assertEquals("tag2", newNote.tags.get(1).title);
	}

	@Test
	public void createInvalidNote() {

		Cookie cookie = Utils.fakeCookie("pingu@notient.com");
		String title = "";
		Result result = callAction(
				controllers.routes.ref.Notes.create(),
				fakeRequest()
						.withFormUrlEncodedBody(ImmutableMap.of("title", title)).withCookies(cookie));
		assertThat(status(result)).isEqualTo(BAD_REQUEST);

		title = " ";
		result = callAction(
				controllers.routes.ref.Notes.create(),
				fakeRequest().withFormUrlEncodedBody(
						ImmutableMap.of("title", title)).withCookies(cookie));
		assertThat(status(result)).isEqualTo(BAD_REQUEST);

		title = "N";
		result = callAction(
				controllers.routes.ref.Notes.create(),
				fakeRequest().withFormUrlEncodedBody(
						ImmutableMap.of("title", title)).withCookies(cookie));
		assertThat(status(result)).isEqualTo(BAD_REQUEST);

		title = "Lorem ipsum Veniam sunt nulla enim esse incididunt eiusmod qui aliqua dolor nisi";
		result = callAction(
				controllers.routes.ref.Notes.create(),
				fakeRequest().withFormUrlEncodedBody(
						ImmutableMap.of("title", title)).withCookies(cookie));
		assertThat(status(result)).isEqualTo(BAD_REQUEST);
	}
}
package models;

import com.avaje.ebean.Ebean;
import com.google.common.collect.ImmutableMap;

import org.junit.Before;
import org.junit.Test;

import play.libs.Yaml;
import play.mvc.Result;
import play.test.WithApplication;

import java.util.*;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.*;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.test.Helpers.callAction;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.status;

public class UsersTest extends WithApplication {

	@Before
	public void setUp() {
		start(fakeApplication(inMemoryDatabase()));
		Ebean.save((List) Yaml.load("test-data.yml"));
	}

	@Test
	public void createAndRetrieveUser() {

		// Valid user
		new User("pingu@notes.com", "Pingu", "mysecretpasword").save();
		User pingu = User.find.where().eq("email", "pingu@notes.com")
				.findUnique();
		assertNotNull(pingu);
		assertEquals("Pingu", pingu.username);

		// Invalid users
		new User("invalidPingu@notes.com", "P", "mysecretpasword").save();
		pingu = User.find.where().eq("email", "invalidPingu@notes.com")
				.findUnique();
		
		/*
		// Should not create user with too short username
		Result result = callAction(
				controllers.routes.ref.Application.newUser(),
				fakeRequest().withFormUrlEncodedBody(
						ImmutableMap.of("email", "pinguUser@notes.com",
								"username", "p", "password",
								"mysecret password")).withSession("email",
						"test@notes.com"));
		assertThat(status(result)).isEqualTo(BAD_REQUEST);
		// Username should be unique
		
		Result result = callAction(
				controllers.routes.ref.Application.newUser(),
				fakeRequest().withFormUrlEncodedBody(
						ImmutableMap.of("email", "pinguUser@notes.com",
								"username", "unique", "password",
								"mysecret password")).withSession("email",
						"test@notes.com"));
		assertThat(status(result)).isEqualTo(SEE_OTHER);
						
		Result result = callAction(
				controllers.routes.ref.Application.newUser(),
				fakeRequest().withFormUrlEncodedBody(
						ImmutableMap.of("email", "otherPinguUser@notes.com",
								"username", "unique", "password",
								"mysecret password")).withSession("email",
						"test@notes.com"));
		assertThat(status(result)).isEqualTo(BAD_REQUEST);
		*/
		// TODO: More invalid user cases
	}

	@Test
	public void changeUserPassword() {
		// TODO
	}

	@Test
	public void deleteUser() {
		User student = User.find.where().eq("email", "student@notes.com")
				.findUnique();
		User.deleteUser("student@notes.com");

		User myDeletedUser = User.find.where().eq("email", "student@notes.com")
				.findUnique();
		assertNull(myDeletedUser);
	}

	// Basic query methods

	@Test
	public void findUserByEmail() {
		User student = User.findByEmail("student@notes.com");
		assertEquals("student", student.username);
	}
}
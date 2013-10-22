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
import static play.mvc.Http.Status.SEE_OTHER;
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
		User pingu = new User();
		pingu.email = "pingu1@notient.com";
		pingu.username = "newPingu";
		pingu.save();
		pingu = User.find.where().eq("email", "pingu1@notient.com")
				.findUnique();
		assertNotNull(pingu);
		assertEquals("newPingu", pingu.username);
		
		// TODO: More invalid user cases
		
	}

	@Test
	public void changeUserPassword() {
		// TODO
	}

	@Test
	public void deleteUser() {
    	User user = User.findByEmail("pingu@notient.com");
		String userEmail = user.email;
		user.delete();
		User myDeletedUser = User.find.where().eq("email", userEmail)
				.findUnique();
		assertNull(myDeletedUser);
	}

	// Basic query methods

	@Test
	public void findUserByEmail() {
		User student = new User();
		student.email = "student@notient.com";
		student.username = "foundStudent";
		student.save();
		User foundUser = User.findByEmail("student@notient.com");
		assertEquals("foundStudent", foundUser.username);
	}
}
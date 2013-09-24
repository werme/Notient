package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;
import static play.test.Helpers.*;

public class UserTest extends WithApplication{

	@Before
	  public void setUp() {
          start(fakeApplication(inMemoryDatabase()));
      }
	  @Test
	  public void createAndRetrieveUser() {
	      //new User("user@test.com", "user", "password").save();
	      User user = User.find.where().eq("email", "user@test.com").findUnique();
	      assertNotNull(user);
	      assertEquals("user", user.name);
	  }
	  @Test
	  public void tryAuthenticateUser() {
	      //new User("user@test.com", "user", "password").save();
	      
	      assertNotNull(User.authenticate("user@test.com", "password"));
	      assertNull(User.authenticate("user@test.com", "badpassword"));
	      assertNull(User.authenticate("baduser@test.com", "password"));
	      // Null test
	      assertNull(User.authenticate("baduser@test.com", null));
	      assertNull(User.authenticate(null, "password"));
	  }
}

import models.*;

import org.junit.*;

import play.test.*;
import play.libs.F.*;
import play.Logger;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

import static org.fluentlenium.core.filter.FilterConstructor.*;

public class UserIntegrationTest extends IntegrationTest {

  @Test
  public void testLogin() {
      running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
          public void invoke(TestBrowser browser) {
              login(browser);
              assertThat(browser.$("header h1").first().getText()).isEqualTo("notient");        
              assertThat(browser.$("nav").first().getText()).contains("Profile");        
          }
      });        
  }

  @Test
  public void testLogout() {
      running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
              public void invoke(TestBrowser browser) {
                  login(browser);
                  browser.$("#logout-link").click();
                  assertThat(browser.$("h1").first().getText()).isEqualTo("Login");
              }
          });   
  }

  @Test
  public void testProfile() {
      running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
              public void invoke(TestBrowser browser) {
                  login(browser);
                  browser.$("#profile-link").click();
                  assertThat(browser.url()).isEqualTo(myUrl + "/user/notient1@gmail.com");
              }
          });     
  }
}
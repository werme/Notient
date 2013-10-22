import org.junit.*;

import play.test.*;
import play.libs.F.*;
import play.Logger;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

import static org.fluentlenium.core.filter.FilterConstructor.*;

public class IntegrationTest {

    String baseUrl = "http://localhost";
    String port = "3333";
    String myUrl = baseUrl + ":" + port;

    public void login(TestBrowser browser) {
        browser.goTo(myUrl);
        browser.$("#login-link").click();
        browser.$("#username").text("pingu1");
        browser.$("#password").text("password");
        browser.$("button", withText("Login")).click();
    }
    
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
    public void testFrontPage() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                browser.goTo(myUrl);
                assertThat(browser.$("header h1").first().getText()).isEqualTo("notient");
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
  // @Test
  // public void testCreateNote() {
  //   running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
  //           public void invoke(TestBrowser browser) {
  //               login(browser);
  //               browser.$("#newnote-button").click();
  //               browser.fill("#title").with("IntegrationTest");
  //               browser.fill("#tags").with("tests");
  //               browser.fill("#content").with("This text is for testing the note creation");
  //               browser.$("#createnote-button").click();

  //               assertThat(browser.$("new note is found").first().getText()).isEqualTo("IntegratonTest");
  //           }
  //       });     
  // }
  @Test
  public void testProfile() {
    running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                login(browser);
                browser.$("#profile-link").click();
                Logger.debug(browser.url());
                assertThat(browser.$("div h1").first().getText()).isEqualTo("pingu1");
            }
        });     
  }
  // @Test
  // public void testSearch() {
  //   running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
  //           public void invoke(TestBrowser browser) {
  //               login(browser);
                
  //               browser.$("#profile-link").click();
  //               assertThat(browser.$("header h1").first().getText()).isEqualTo("pingu1");
  //           }
  //       });     
  // }
}
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
    public void createNote(TestBrowser browser) {
        browser.$("#new-note-link").click();
        browser.$("#title").text("IntegrationTest");
        browser.$("#content").text("testing testing");
        browser.$("#create-note-button").click();
        //Logger.debug("##### SE HÄR MACKAN #####" + browser.url());
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
  @Test
  public void testNoteCreator() {
    running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                login(browser);
                createNote(browser);
                assertThat(browser.url()).isEqualTo(myUrl + "/note/5");
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
  // @Test
  // public void testDeleteNote() {
  //   running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
  //           public void invoke(TestBrowser browser) {
  //               login(browser);
  //               createNote(browser);
  //               Logger.info("!!!!!!!!!!!!!!!!!!!!!!!" + browser.$("#message-wrapper").getText());
  //               browser.$("#delete-note-button").click();
  //               Logger.info("!!!!!!!!!!!!!!!!!!!!!!!" + browser.$("#message-wrapper").getText());
  //               Logger.debug("##### SE HÄR MACKAN #####" + browser.url());
  //               assertThat(browser.url()).isEqualTo(myUrl + "/notes");
  //           }
  //       });  
  // }
  @Test
  public void testEditNote() {
    running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                login(browser);
                createNote(browser);
                browser.$("#edit-note-button").click();
                browser.$("#content").text("123456789");
                browser.$("#update-note-button").click();
                //Logger.info("!!!!!!!!!!!!!!!!!!!!!!!" + browser.$("#message-wrapper").getText());
                assertThat(browser.pageSource()).contains("123456789");
            }
        });  
  }
}
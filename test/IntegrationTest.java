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
                assertThat(browser.pageSource()).contains("123456789");
            }
        });  
  }
  @Test
  public void testComment() {
    running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                login(browser);
                createNote(browser);
                browser.$("#content").text("First comment");
                browser.$("#post-comment-button").click();
                assertThat(browser.$("#message-wrapper").getText()).isEqualTo("Successfully posted comment!");
                assertThat(browser.$("#comment-body").getText()).isEqualTo("First comment");
            }
        });  
  }
  @Test
  public void testUpVote() {
    running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                login(browser);
                createNote(browser);
                browser.$("#upvote-button").click();
                assertThat(browser.$("#userscore").getText()).isEqualTo("1");
            }
        });  
  }
  @Test
  public void testDownVote() {
    running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                login(browser);
                createNote(browser);
                browser.$("#downvote-button").click();
                assertThat(browser.$("#userscore").getText()).isEqualTo("-1");
            }
        });  
  }
  /**
  *Should only be able to vote a specific note once per user
  */
  @Test
  public void testVoteToggle() {
    running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                login(browser);
                createNote(browser);
                browser.$("#upvote-button").click();
                browser.$("#upvote-button").click();
                assertThat(browser.$("#userscore").getText()).isEqualTo("0");
            }
        });  
  }
}
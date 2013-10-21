import org.junit.*;

import play.test.*;
import play.libs.F.*;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

import static org.fluentlenium.core.filter.FilterConstructor.*;

public class IntegrationTest {
    
    @Before
    public void testLogin() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                browser.goTo("http://localhost:3333");

                browser.$("#login-button").click();
                assertThat(browser.$("header h1").first().getText()).isEqualTo("Login");
                browser.fill("#username").with("pingu1");
                browser.fill("#password").with("password");
                browser.$("#submit-button").click();
                // Checks if routed back to the frontpage
                assertThat(browser.$("header h1").first().getText()).isEqualTo("notient");
            }
        });        
    }

    @Test
    public void testFrontPage() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                browser.goTo("http://localhost:3333");
                
                assertThat(browser.$("header h1").first().getText()).isEqualTo("notient");
            }
        });
    }
  @Test
  public void testLogout() {
    running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                browser.goTo("http://localhost:3333");
                
                browser.$("#logout-button").click();
                assertThat(browser.$("header h1").first().getText()).isEqualTo("Login");
            }
        });   
  }
  @Test
  public void testCreateNote() {
    running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                browser.goTo("http://localhost:3333");
                
                browser.$("#newnote-button").click();
                browser.fill("#title").with("IntegrationTest");
                browser.fill("#tags").with("tests");
                browser.fill("#content").with("This text is for testing the note creation");
                browser.$("#createnote-button").click();

                assertThat(browser.$("new note is found").first().getText()).isEqualTo("IntegratonTest");
            }
        });     
  }
}
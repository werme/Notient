import models.*;

import org.junit.*;

import play.test.*;
import play.libs.F.*;
import play.Logger;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

import static org.fluentlenium.core.filter.FilterConstructor.*;

public class CommentIntegrationTest extends IntegrationTest {

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
}
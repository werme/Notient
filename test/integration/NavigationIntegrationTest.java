import models.*;

import org.junit.*;

import play.test.*;
import play.libs.F.*;
import play.Logger;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

import static org.fluentlenium.core.filter.FilterConstructor.*;

public class NavigationIntegrationTest extends IntegrationTest {

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
  public void testPaginationForward() {
    running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                login(browser);
                browser.$("#next-page-link").click();
                assertThat(browser.url()).isEqualTo(myUrl + "/thumbnails/2");
            }
        });  
  }
  @Test
  public void testPaginationBackward() {
    running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                login(browser);
                browser.$("#next-page-link").click();
                browser.$("#last-page-link").click();
                assertThat(browser.url()).isEqualTo(myUrl + "/thumbnails/1");
            }
        });  
  }
}
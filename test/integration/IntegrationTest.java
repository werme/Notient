import models.*;

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
        browser.$("#username").text("pingu");
        browser.$("#password").text("password");
        browser.$("button", withText("Login")).click();
    }
}
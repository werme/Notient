package controllers;

import play.mvc.*;
import play.data.*;
import play.Logger;
import models.*;
import views.html.*;
import securesocial.core.java.SecureSocial;
import securesocial.core.Identity;

public class Users extends Controller {

  static Form<Note> noteForm = Form.form(Note.class);

  public static Result show(String id) {
    return ok(views.html.users.show.render(User.find.ref(id), noteForm));
  }
}
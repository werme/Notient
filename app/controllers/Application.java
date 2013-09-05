package controllers;

import play.*;
import play.mvc.*;
import play.data.*;

import models.*;

import views.html.*;

public class Application extends Controller {

  static Form<Note> noteForm = Form.form(Note.class);

  public static Result index() {
    return ok(views.html.index.render(Note.all(), noteForm));
  }

  public static Result notes() {
    return ok(views.html.index.render(Note.all(), noteForm));
  }

  public static Result newNote() {
    Form<Note> filledForm = noteForm.bindFromRequest();

    if(filledForm.hasErrors()) {
      return badRequest(views.html.index.render(Note.all(), filledForm));
    } else {
      Note.create(filledForm.get());
      return redirect(routes.Application.notes());
    }
  }

  public static Result deleteNote(Long id) {
    Note.delete(id);
    return redirect(routes.Application.notes());
  }
}

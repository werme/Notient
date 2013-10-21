package controllers;

import java.util.*;
import play.mvc.*;
import play.data.*;
import play.Logger;
import models.*;
import views.html.*;

public class Search extends Controller {

  static Form<Note> noteForm = Form.form(Note.class);
  static Form<Comment> commentForm = Form.form(Comment.class);
  static Form<String> searchForm = Form.form(String.class);
  
  public static Result performSearch() {
    Form<String> filledForm = searchForm.bindFromRequest();
    String query = filledForm.field("query").value();
    return redirect(routes.Search.show(query));
  }

  public static Result show(String query) {
    List<Note> notes = Note.searchNotes(query);
    List<User> users = User.searchUsers(query); // Bapl
    return ok(index.render(notes, noteForm, searchForm));
  }
}
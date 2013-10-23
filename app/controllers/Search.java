/**
 *  This is the controller that handles search queries.
 */

package controllers;

import java.util.*;
import play.mvc.*;
import play.data.*;
import play.Logger;
import models.*;
import views.html.*;
import views.html.notes.*;

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

    // Searching for users is not implemented in the GUI yet.
    // List<User> users = User.searchUsers(query);
    return ok(list.render(notes, noteForm, searchForm, 1, 1));
  }
}
package controllers;

import play.mvc.*;
import play.data.*;
import play.Logger;
import models.*;
import views.html.*;

public class Application extends Controller {

	public static Result index() {
		return redirect(routes.Notes.thumbnailsAll());
	}
}

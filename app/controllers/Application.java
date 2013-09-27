package controllers;

import play.mvc.*;
import play.data.*;
import play.Logger;
import models.*;
import views.html.*;

public class Application extends Controller {

	public static Result index() {
		return redirect(routes.Notes.list());
	}

	public static Result login() {
		return ok(login.render(Form.form(Login.class)));
	}

	public static class Login {

		public String email;
		public String password;

		public String validate() {
			if (User.authenticate(email, password) == null) {
				return "Invalid user or password";
			}
			return null;
		}
	}

	public static Result authenticate() {
		Form<Login> loginForm = Form.form(Login.class).bindFromRequest();
		if (loginForm.hasErrors()) {
			return badRequest(login.render(loginForm));
		} else {
			session().clear();
			session("email", loginForm.get().email);
			return redirect(
				routes.Application.index()
				);
		}
	}

	public static Result register() {
		return ok(register.render(Form.form(Register.class)));
	}

	public static class Register {

		public String email;
		public String username;
		public String password;

		public String validate() {
			//Validate the email, username and password here.
			return null;
		}

	}

	public static Result logout() {
	    session().clear();
	    flash("success", "You've been logged out");
	    return redirect(
	        routes.Application.login()
	    );
	}
	public static Result newUser() {
		Form<User> registerForm = Form.form(User.class).bindFromRequest();
		if (registerForm.hasErrors()) {
			return badRequest(register.render(Form.form(Register.class)));
		} else {
			User.createUser(registerForm.get());
			session().clear();
			session("email", registerForm.get().email);
			return redirect(
				routes.Application.index()
				);
		}
	}
}

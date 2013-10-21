package controllers;

import java.util.*;
import play.mvc.*;
import play.data.*;
import play.db.ebean.*;
import play.Logger;
import models.*;
import views.html.*;
import views.html.notes.*;
import helpers.UnauthorizedException;
import securesocial.core.java.SecureSocial;
import securesocial.core.Identity;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import java.io.File;
import models.S3File;
import java.util.*;


public class Notes extends Controller {

	static Form<Note> noteForm = Form.form(Note.class);
	static Form<Comment> commentForm = Form.form(Comment.class);
	static Form<String> searchForm = Form.form(String.class);

	public static Result index() {
		return ok(index.render(Note.all(), noteForm, searchForm));
	}

	public static Result list() {
		return ok(index.render(Note.all(), noteForm, searchForm));
	}

	public static Result show(Long id) {
		return ok(show.render(Note.find.ref(id), noteForm, commentForm, searchForm));
	}

	@SecureSocial.SecuredAction
	public static Result newNote() {
		return ok(new_note.render(noteForm));
	}

	@SecureSocial.SecuredAction
	public static Result create() {
		Form<Note> filledForm = noteForm.bindFromRequest();

		if (filledForm.hasErrors()) {
			return badRequest(new_note.render(filledForm));
		} else {
            //Temporary solution since i dont know how the form works with files.
            Http.MultipartFormData body = request().body().asMultipartFormData();
            Http.MultipartFormData.FilePart uploadFilePart = body.getFile("upload");
            Logger.debug("uploadFilePart" + uploadFilePart);
            if (uploadFilePart != null) {
                S3File s3File = new S3File();
                s3File.name = uploadFilePart.getFilename();
                s3File.file = uploadFilePart.getFile();
                s3File.save();
                Note.create(filledForm.get(), Form.form().bindFromRequest().get("tagList"), User.currentUser(), s3File);
                return redirect(routes.Notes.list());
            } else {
	            Note.create(filledForm.get(), Form.form().bindFromRequest().get("tagList"), User.currentUser(), null);
				flash("info", "Successfully created note!");
				return redirect(routes.Notes.list());
            }
		}
	}

	@SecureSocial.SecuredAction(authorization = WithPrivilegeLevel.class, params = {PrivilegeLevel.USER, PrivilegeLevel.ADMIN})
	public static Result delete(Long id) {
		try {
			Note.delete(id);
		} catch (UnauthorizedException e) {
			flash("error", "You are not authorized to delete this note.");
			return badRequest(index.render(Note.all(), noteForm, searchForm));
		}
		return redirect(routes.Notes.list());
	}

	@SecureSocial.SecuredAction
	public static Result newComment(Long id) {
		Form<Comment> filledForm = commentForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(index.render(Note.all(), filledForm)); // should redirect to show note view later 
		} else {
			Comment.create(id, filledForm.get(), User.currentUser());
			flash("info", "Successfully posted comment!")
			return redirect(routes.Notes.show(id));
		}
	}

	//@SecureSocial.SecuredAction(authorization = WithPrivilegeLevel.class, params = {PrivilegeLevel.USER, PrivilegeLevel.ADMIN})
	public static Result deleteComment(Long id, Long commentId) {
		// TODO: try/catch and flash this
		Comment.delete(commentId);
		return redirect(routes.Notes.show(id));
	}

	@SecureSocial.SecuredAction
	public static Result toggleUpVote(Long id) {
		Note.find.ref(id).toggleUpVote(User.currentUser());
		return redirect(routes.Notes.show(id));
	}
	
	@SecureSocial.SecuredAction
	public static Result toggleDownVote(Long id) {
		Note.find.ref(id).toggleDownVote(User.currentUser());
		return redirect(routes.Notes.show(id));
	}
	@SecureSocial.SecuredAction
	public static Result edit(Long id) {
		Form<Note> filledForm = noteForm.fill(Note.find.ref(id));
		return ok(edit.render(Note.find.ref(id), filledForm));
	}

	@SecureSocial.SecuredAction
	public static Result update(Long id) {
		Form<Note> filledForm = noteForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(index.render(Note.all(), filledForm, searchForm));
		} else {
			Note.update(filledForm.get(), Form.form().bindFromRequest().get("tagList"));
			flash("info", "Successfully update note!");
			return redirect(routes.Notes.show(id));
		}
	}
}

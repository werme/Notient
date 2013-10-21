package controllers;

import play.mvc.*;
import play.data.*;
import play.db.ebean.*;
import play.Logger;
import models.*;
import views.html.*;
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

	public static Result index() {
		return ok(views.html.index.render(Note.all(), noteForm));
	}

	public static Result list() {
		return ok(views.html.index.render(Note.all(), noteForm));
	}

	public static Result show(Long id) {
		return ok(views.html.notes.show.render(Note.find.ref(id), noteForm, commentForm));
	}

	@SecureSocial.SecuredAction
	public static Result newNote() {
		Form<Note> filledForm = noteForm.bindFromRequest();

		if (filledForm.hasErrors()) {
			return badRequest(views.html.index.render(Note.all(), filledForm));
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
                Note.create(filledForm.get(), Form.form().bindFromRequest().get("tagList"), s3File);
                return redirect(routes.Notes.list());
            } else {
            Note.create(filledForm.get(), Form.form().bindFromRequest().get("tagList"), null);
			return redirect(routes.Notes.list());
            }
		}
	}

	@SecureSocial.SecuredAction(authorization = WithPrivilegeLevel.class, params = {PrivilegeLevel.USER, PrivilegeLevel.ADMIN})
	public static Result delete(Long id) {
		Note.delete(id);
		return redirect(routes.Notes.list());
	}

	@SecureSocial.SecuredAction
	public static Result newComment(Long id) {
		Identity user = (Identity) ctx().args.get(SecureSocial.USER_KEY);
    	User localUser = User.find.byId(user.identityId().userId());

		Form<Comment> filledForm = commentForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			//return badRequest(views.html.index.render(Note.all(), filledForm)); // should redirect to show note view later 
		} else {
			Comment.create(id, filledForm.get(), localUser);
		}
		return redirect(routes.Notes.show(id));
	}

	//@SecureSocial.SecuredAction(authorization = WithPrivilegeLevel.class, params = {PrivilegeLevel.USER, PrivilegeLevel.ADMIN})
	public static Result deleteComment(Long id, Long commentId) {
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
}

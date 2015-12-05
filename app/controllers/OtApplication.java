package controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.ot.dbdata.DataFiller;
import controllers.ot.emailer.Emailer;
import controllers.ot.forms.LoginForm;
import controllers.ot.forms.PasswordRequestForm;
import controllers.ot.forms.PasswordResetForm;
import controllers.ot.forms.SignupForm;
import controllers.ot.mapper.NewQuestionRequestMapper;
import controllers.ot.security.Secured;
import models.ot.Enums.PermissionType;
import models.ot.Option;
import models.ot.Question;
import models.ot.Users;
import play.data.Form;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.ot.ztheme.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by amd on 11/22/15.
 */
public class OtApplication extends Controller {

    public static Form<SignupForm> signupForm = Form.form(SignupForm.class);
    public static Form<LoginForm> loginForm = Form.form(LoginForm.class);
    public static Form<PasswordResetForm> passwordResetForm = Form.form(PasswordResetForm.class);
    public static Form<PasswordRequestForm> passwordRequestForm = Form.form(PasswordRequestForm.class);

    public static final String NEW_QUESTION_KEY = "mQENBFZX3IgBCACR4ASPOUZO46OrGsCHVOrH5pgrIGyWeKoel5OdrMNS40HV";

    public static String ERROR_KEY = "error";
    public static String SUCCESS_KEY = "success";

    public Result index() {
        new DataFiller();
        Users users = Users.findUserByUsername(session().getOrDefault("username", null));
        return ok(index.render("Hello world", users));
    }

    @AddCSRFToken
    public Result register(){
        return ok(register.render(signupForm));
    }

    @AddCSRFToken
    public Result login(){
        return ok(login.render(loginForm));
    }

    @AddCSRFToken
    public Result forgotPassword(){
        return ok(forgot_password.render(passwordRequestForm));
    }

    @AddCSRFToken
    public Result resetPassword(String token){
        if(token.equals("")){
            flash(ERROR_KEY, "Token to reset password is missing");
            return badRequest(forgot_password.render(passwordRequestForm));
        }
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        Form<PasswordResetForm> boundPasswordResetForm = passwordResetForm.bind(map);
        return ok(reset_password.render(boundPasswordResetForm));
    }

    @RequireCSRFCheck
    public Result registerPOST(){
        Form<SignupForm> boundSignupForm = signupForm.bindFromRequest();

        if(boundSignupForm.hasErrors()){
            return badRequest(register.render(boundSignupForm));
        }else {
            SignupForm formData = boundSignupForm.get();
            Users users = new Users(formData.getUsername(), formData.getPassword(), formData.getEmail(), formData.getFirstName(), formData.getLastName(), false, false);
            users.save();
            Emailer.getInstance().sendUserActivationMail(users.getEmail(), users.getToken());
            flash(SUCCESS_KEY, "Signup Successfull, Activate by clicking on link send to mail");
            return ok(login.render(loginForm));
        }
    }

    @RequireCSRFCheck
    public Result loginPOST(){
        Form<LoginForm> boundLoginForm = loginForm.bindFromRequest();
        if(boundLoginForm.hasErrors()){
            return badRequest(login.render(boundLoginForm));
        }else {
            LoginForm formData = boundLoginForm.get();
            Users users = Users.findUserByUsername(formData.getUsername());
            if(users == null) {
                flash(ERROR_KEY, "Login credentials does not match or user is inactive");
                return badRequest(login.render(loginForm));
            }else {
                session().clear();
                session("username", users.getUsername());
                flash(SUCCESS_KEY, "Login Successfull");
                return ok(index.render("Hello World", users));
            }
        }
    }

    @RequireCSRFCheck
    public Result forgotPasswordPOST(){
        Form<PasswordRequestForm> boundPasswordRequestForm  = passwordRequestForm.bindFromRequest();
        if(boundPasswordRequestForm.hasErrors()){
            return badRequest(forgot_password.render(boundPasswordRequestForm));
        }else{
            Users users = Users.find.where().eq("username", boundPasswordRequestForm.get().getUsername())
                    .eq("email", boundPasswordRequestForm.get().getEmail())
                    .eq("first_name", boundPasswordRequestForm.get().getFirstName()).findUnique();
            users.resetToken();
                if(!Emailer.getInstance().sendResetPasswordMail(users.getEmail(), users.getToken())) {
                    flash(ERROR_KEY, "Password Reset failed, Try Again");
                    return badRequest(forgot_password.render(passwordRequestForm));
                }else {
                    flash(SUCCESS_KEY, "Password Reset email send successfully");
                    return ok(login.render(loginForm));
                }
        }
    }
 
    @RequireCSRFCheck
    public Result resetPasswordPOST(){
        Form<PasswordResetForm> boundPasswordResetForm = passwordResetForm.bindFromRequest();
        if(boundPasswordResetForm.hasErrors()){
            return badRequest(reset_password.render(boundPasswordResetForm));
        }else{
            try {
                Users.resetPassword(boundPasswordResetForm.get().getToken(), boundPasswordResetForm.get().getPassword());
                flash(SUCCESS_KEY, "Password reset successfull");
                return ok(login.render(loginForm));
            } catch (Exception e) {
                flash(ERROR_KEY, e.getMessage());
                return badRequest(reset_password.render(passwordResetForm));
            }
        }
    }

    public Result activate(String token){
        try {
            Users.activate(token);
            flash(SUCCESS_KEY, "Your account has been activated");
            return ok(login.render(loginForm));
        } catch (Exception e) {
            flash(ERROR_KEY, e.getMessage());
            return ok(login.render(loginForm));
        }
    }


    @Security.Authenticated(Secured.class)
    public Result newQuestion(){
        Users users = Users.findUserByUsername(session().get("username"));
        if(users == null)loginFailedState();
        return  ok(new_question.render(NEW_QUESTION_KEY, users));
    }


    @Security.Authenticated(Secured.class)
    public Result newQuestionPOST() throws InterruptedException {
        Thread.sleep(2000);
        Users user = Users.findUserByUsername(session().get("username"));
        if(user == null) return loginFailedState();
        if(!user.isPermitted(PermissionType.CAN_ADD_QUESTIONS)) return unAuthenticatedRequset();
        NewQuestionRequestMapper requestMapper = new NewQuestionRequestMapper().parseFromJSON(request().body().asJson());
        ObjectNode result = Json.newObject();

        if(requestMapper == null){
            result.putArray(ERROR_KEY).add("Internal Error Occurred! ");
            return badRequest(result);
        }

        if (requestMapper.hasErrors()){
            result.put("message", "Failure");
            ArrayNode arrayNode = result.putArray(ERROR_KEY);
            for(String errors:requestMapper.getErrors())
                arrayNode.add(errors);
            return badRequest(result);

        }else {
            if(!requestMapper.saveToDB()) {
                result.putArray(ERROR_KEY).add("Internal Error Occurred! ");
                return badRequest(result);
            }else {
                result.putArray(SUCCESS_KEY).add("Question submitted successfully");
                return ok(result);
            }
        }
    }

    @Security.Authenticated(Secured.class)
    public Result questionList(Long page){
        Users user = Users.findUserByUsername(session().get("username"));
        ObjectNode result = Json.newObject();
        if(user == null){
            result.putArray(ERROR_KEY).add("User not logged in ! Re login and try ! ");
            return unauthorized(result);
        }
        if(!Form.form().bindFromRequest().get("key").equals(NEW_QUESTION_KEY)) {
            result.putArray(ERROR_KEY).add("API KEY validation failed");
            return unauthorized(result);
        }

        List<Question> questions = Question.find.where().order("id").findPagedList(page.intValue(), 100).getList();
        ArrayNode arrayNode = result.putArray("data");
        for(Question question:questions){
            ObjectNode node = Json.newObject();
            node.put("id",question.getId());
            node.put("question", question.getQuestion());
            node.put("questionType", question.getQuestionType().getEventValue());
            node.put("difficultyLevel", question.getDifficultyType().getEventValue());
            ArrayNode optionsNode = node.putArray("options");
            for(Option option:question.getOptions()){
                ObjectNode optionNode = Json.newObject();
                optionNode.put("value", option.getOption());
                optionNode.put("correct", option.isCorrect());
                optionsNode.add(optionNode);
            }
            arrayNode.add(node);
        }
        return ok(result);
    }

    public Result logout(){
        if(!session().isEmpty()){
            session().clear();
            flash(SUCCESS_KEY, "Logout successfully");
        }
        return ok(index.render("Hello", null));
    }

    public Result loginFailedState(){
        flash(ERROR_KEY, "Login session failed! Pleasy retry login");
        return badRequest(login.render(loginForm));
    }

    public Result unAuthenticatedRequset(){
        flash(ERROR_KEY, "You are unauthorized to view the page your requested! Try with other User");
        return badRequest(login.render(loginForm));
    }
}

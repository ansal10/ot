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
import models.ot.Users;
import play.data.Form;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.ot.ztheme.*;

import java.util.HashMap;
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
        return ok(index.render("Hello world"));
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
            return ok(index.render("Hello World"));
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

    public Result newQuestion(){
        return  ok(new_question.render(NEW_QUESTION_KEY));
    }

    public Result newQuestionPOST(){
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
}

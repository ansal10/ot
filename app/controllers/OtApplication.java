package controllers;

import controllers.ot.forms.LoginForm;
import controllers.ot.forms.SignupForm;
import play.data.Form;
import play.data.validation.ValidationError;
import play.filters.csrf.AddCSRFToken;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.ot.ztheme.index;
import views.html.ot.ztheme.login;
import views.html.ot.ztheme.register;

import java.util.List;


/**
 * Created by amd on 11/22/15.
 */
public class OtApplication extends Controller {

    public static Form<SignupForm> signupForm = Form.form(SignupForm.class);
    public static Form<LoginForm> loginForm = Form.form(LoginForm.class);

    public Result index() {
        return ok(index.render("Hello world"));
    }

    @AddCSRFToken
    public Result register(){
        return ok(register.render(signupForm));
    }

    public Result login(){
        return ok(login.render(loginForm));
    }

    public Result registerPOST(){
        Form<SignupForm> boundSignupForm = signupForm.bindFromRequest();

        if(boundSignupForm.hasErrors()){
            return badRequest(register.render(boundSignupForm));
        }else {
            SignupForm formData = boundSignupForm.get();
            redirect(routes.OtApplication.index());
        }
        return null;
    }

    public Result loginPOST(){
        Form<LoginForm> boundLoginForm = loginForm.bindFromRequest();
        if(boundLoginForm.hasErrors()){
            return badRequest(login.render(boundLoginForm));
        }else {
            LoginForm formData = boundLoginForm.get();
            redirect(routes.OtApplication.index());
        }
        return null;
    }
}

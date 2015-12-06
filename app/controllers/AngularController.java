package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.ot.ztheme.newTest.add_questions;
import views.html.ot.ztheme.newTest.new_test_form;

/**
 * Created by amd on 12/6/15.
 */
public class AngularController extends Controller {

    public Result newTestForm(){
        return ok(new_test_form.render(""));
    }

    public Result newTestAddQuestion() throws InterruptedException {
        return ok(add_questions.render(""));
    }
}

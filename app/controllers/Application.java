package controllers;

import play.mvc.Result;
import play.mvc.Controller;
import views.html.index;
import views.html.list;

/**
 * Created by amd on 12/6/15.
 */
public class Application extends Controller {

    public Result index(){
        return ok(index.render("Hello World"));
    }

    public Result list(){
        return ok(list.render("Hello List"));
    }
}

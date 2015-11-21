package controllers.ot;

import play.mvc.Result;

import static play.mvc.Results.ok;

/**
 * Created by amd on 11/22/15.
 */
public class Application {

    public Result index() {
        return ok("Inside Ot application index");
    }
}

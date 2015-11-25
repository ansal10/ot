package controllers;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


/**
 * Created by amd on 11/22/15.
 */
public class OtApplication extends Controller {

    public Result index() {
        String logKey = "Index : ";
        Logger.debug("DEBUG STARTED SERVING");
        Logger.info("INFO STARTED SERVICE");
        Logger.warn("WARN STARTED SERVICE");
        Logger.error("ERROR STARTED SERVICE");

        try{
            new Scanner(new File("wqoihdwq"));
        } catch (FileNotFoundException e) {
            Logger.error(String.format("Exception %s", e.getMessage()));
        }
        return ok(index.render("Hello world"));
    }
}

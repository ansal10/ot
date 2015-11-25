package controllers.ot.security;
/**
 * Created by amd on 7/24/15.
 */
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

import java.util.Date;

import static play.mvc.Controller.flash;

public class Secured extends Security.Authenticator {

    private static final long TIMEOUT = 18000000;

    @Override
    public String getUsername(Context ctx) {

        String username =  ctx.session().get("username");
        long lastLogged = Long.parseLong(ctx.session().getOrDefault("last_logged","0"));
        long timeNow = new Date().getTime();
        if((timeNow - lastLogged) > TIMEOUT ) {
            flash("error", "Session Time out! Login Again");
            return null;
        }
        else{
            ctx.session().put("last_logged", String.valueOf(timeNow));
            return username;
        }

    }

    @Override
    public Result onUnauthorized(Context ctx) {
        flash("error", "Please login first");
        return redirect(controllers.routes.OtApplication.index());
    }
}
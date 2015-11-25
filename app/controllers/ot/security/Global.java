package controllers.ot.security;


import play.GlobalSettings;
import play.api.mvc.EssentialFilter;
import play.filters.csrf.CSRFFilter;

/**
 * Created by amd on 7/23/15.
 */
public class Global extends GlobalSettings {

    @Override
    public <T extends EssentialFilter> Class<T>[] filters(){
        return new Class[]{CSRFFilter.class};
    }
}

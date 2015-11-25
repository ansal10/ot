package controllers.ot.forms;

import models.ot.Users;
import play.data.validation.Constraints;
import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amd on 11/26/15.
 */
public class LoginForm {

    public static final String INVALID_USERNAME = "Only alphabets , numbers and _ are allowed";
    public static final String REQUIRED_FIELD = "This field is Required";


    @Constraints.Required(message = REQUIRED_FIELD)
    @Constraints.Pattern(value = "[a-zA-Z0-9_]+", message = INVALID_USERNAME)
    private String username;

    @Constraints.Required(message = REQUIRED_FIELD)
    private String password;


    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        try {
            Boolean isAuthenticated = Users.authenticate(this.username, this.password);
        }catch (Exception e){
            errors.add(new ValidationError("Error", e.getMessage()));
        }
        return errors.isEmpty() ? null : errors;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}

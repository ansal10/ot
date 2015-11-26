package controllers.ot.forms;

import play.data.validation.Constraints;
import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amd on 11/26/15.
 */
public class PasswordResetForm {

    public static final String PASSWORD_MISMATCH = "Password are not matching";
    public static final String REQUIRED_FIELD = "This field is Required";
    public static final String INVALID_PASSWORD_LENGTH = "Password should be atleast 7 chaacters long";
    public static final String INVALID_TOKEN = "Unable to validate the Password reset";

    @Constraints.Required(message = REQUIRED_FIELD)
    @Constraints.MinLength(value = 7, message = INVALID_PASSWORD_LENGTH)
    private String password;

    @Constraints.Required(message = REQUIRED_FIELD)
    private String confirmPassword;

    @Constraints.Required(message =INVALID_TOKEN )
    private String token;


    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        if (!this.password.equals(confirmPassword)) {
            errors.add(new ValidationError("password", PASSWORD_MISMATCH));
        }
        return errors.isEmpty() ? null : errors;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

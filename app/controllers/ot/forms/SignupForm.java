package controllers.ot.forms;

import play.data.validation.Constraints;
import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amd on 11/26/15.
 */
public class SignupForm  {

    public static final String INVALID_EMAIL = "Email is not valid";
    public static final String INVALID_USERNAME = "Only alphabets , numbers and _ are allowed";
    public static final String INVALID_PASSWORD_LENGTH = "Password should be atleast 7 chaacters long";
    public static final String PASSWORD_MISMATCH = "Password are not matching";
    public static final String REQUIRED_FIELD = "This field is Required";
    public static final String FNAME_LENGTH = "First name should be atleast 3 characters long";
    public static final String LNAME_LENGTH = "Last name should be atleast 2 characters long";

    @Constraints.Required(message = REQUIRED_FIELD)
    @Constraints.Email(message = INVALID_EMAIL )
    private String email;


    @Constraints.Required(message = REQUIRED_FIELD)
    @Constraints.Pattern(value = "[a-zA-Z0-9_]+", message = INVALID_USERNAME)
    private String username;

    @Constraints.Required(message = REQUIRED_FIELD)
    @Constraints.MinLength(value = 7, message = INVALID_PASSWORD_LENGTH)
    private String password;

    @Constraints.Required(message = REQUIRED_FIELD)
    private String confirmPassword;


    @Constraints.Required(message = REQUIRED_FIELD)
    @Constraints.MinLength(value = 3, message = FNAME_LENGTH)
    private String firstName;

    @Constraints.Required(message = REQUIRED_FIELD)
    @Constraints.MinLength(value = 2, message = LNAME_LENGTH)
    private String lastName;


    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        if (!this.password.equals(confirmPassword)) {
            errors.add(new ValidationError("password", PASSWORD_MISMATCH));
        }
        return errors.isEmpty() ? null : errors;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

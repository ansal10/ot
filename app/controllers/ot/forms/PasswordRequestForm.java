package controllers.ot.forms;

import models.ot.Users;
import play.data.validation.Constraints;
import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amd on 11/26/15.
 */
public class PasswordRequestForm {

    public static final String CREDENTIALS_UNMATCHED = "Credentials does not match";
    public static final String INVALID_EMAIL = "Email is not valid";
    public static final String INVALID_USERNAME = "Only alphabets , numbers and _ are allowed";
    public static final String FNAME_LENGTH = "First name should be atleast 3 characters long";
    public static final String REQUIRED_FIELD = "This field is Required";

    @Constraints.Required(message = REQUIRED_FIELD)
    @Constraints.Email(message = INVALID_EMAIL )
    private String email;


    @Constraints.Required(message = REQUIRED_FIELD)
    @Constraints.Pattern(value = "[a-zA-Z0-9_]+", message = INVALID_USERNAME)
    private String username;

    @Constraints.Required(message = REQUIRED_FIELD)
    @Constraints.MinLength(value = 3, message = FNAME_LENGTH)
    private String firstName;


    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        Users users = Users.find.where().eq("username", this.username)
                .eq("email", this.email)
                .eq("first_name", this.firstName).findUnique();

        if(users==null)
            errors.add(new ValidationError("User", CREDENTIALS_UNMATCHED));

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}

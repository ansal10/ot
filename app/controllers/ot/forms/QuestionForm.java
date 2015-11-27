package controllers.ot.forms;

import play.data.validation.Constraints;

/**
 * Created by amd on 11/27/15.
 */
public class QuestionForm {

    public static final String REQUIRED = "This field is required";
    @Constraints.Required(message = REQUIRED)
    public String question;


}

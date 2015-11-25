package formstest;

import controllers.ot.forms.SignupForm;
import org.junit.Assert;
import org.junit.Test;
import play.data.Form;
import play.data.validation.ValidationError;
import play.test.FakeApplication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by amd on 11/26/15.
 */
public class SignupformTest {

    public FakeApplication fakeApplication;


//    @Before
//    public  void startFakeApplication(){
//        fakeApplication = fakeApplication(inMemoryDatabase());
//        start(fakeApplication);
//
//    }
//
//    @After
//    public  void shutDownApplication(){
//        stop(fakeApplication);
//    }


    @Test
    public void testValidSignupForm(){
        Form<SignupForm> signupFormForm = Form.form(SignupForm.class);
        Map<String, String> formData = new HashMap<String, String>();
        formData.put("username", "ansal10");
        formData.put("password", "123456");
        formData.put("confirmPassword", "12345678");
        formData.put("email", "a@gmail.com");
        formData.put("firstName", "anas");
        formData.put("lastName", "md");
        signupFormForm.bind(formData, "username", "password");

        Assert.assertTrue(signupFormForm.hasErrors());

    }

    @Test
    public void testPasswordMismatch(){
        Form<SignupForm> signupFormForm = Form.form(SignupForm.class);
        Map<String, String> formData = new HashMap<String, String>();
        formData.put("username", "ansal10");
        formData.put("password", "12345");
        formData.put("confirmPassword", "123456789");
        formData.put("email", "a@gmail.com");
        formData.put("firstName", "anas");
        formData.put("lastName", "md");
        signupFormForm.bind(formData, "username", "password");
        List<ValidationError> errors = signupFormForm.globalErrors();

        Assert.assertTrue(signupFormForm.hasErrors());
    }


}

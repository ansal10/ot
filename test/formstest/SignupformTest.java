package formstest;

import controllers.ot.forms.SignupForm;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import play.data.Form;
import play.test.FakeApplication;

import java.util.HashMap;
import java.util.Map;

import static play.test.Helpers.*;

/**
 * Created by amd on 11/26/15.
 */
public class SignupformTest {

    public FakeApplication fakeApplication;


    @Before
    public  void startFakeApplication(){
        fakeApplication = fakeApplication(inMemoryDatabase());
        start(fakeApplication);

    }

    @After
    public  void shutDownApplication(){
        stop(fakeApplication);
    }


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

        Form<SignupForm> boundSignupForm = signupFormForm.bind(formData);

        Assert.assertTrue(boundSignupForm.hasErrors());

    }

    @Test
    public void testPasswordMismatch(){
        Form<SignupForm> signupFormForm = Form.form(SignupForm.class);
        Map<String, String> formData = new HashMap<String, String>();
        formData.put("username", "ansal10");
        formData.put("password", "12345678");
        formData.put("confirmPassword", "123456789");
        formData.put("email", "a@gmail.com");
        formData.put("firstName", "anas");
        formData.put("lastName", "md");

        Form<SignupForm> boundSignupForm = signupFormForm.bind(formData);


        Assert.assertTrue(boundSignupForm.hasErrors());
        Assert.assertEquals(boundSignupForm.error("password").message(),  SignupForm.PASSWORD_MISMATCH );
    }


}

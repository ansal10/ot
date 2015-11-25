package formstest;

import controllers.ot.forms.LoginForm;
import models.ot.Users;
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
public class LoginFormTest {

    public FakeApplication fakeApplication;


    @Before
    public  void startFakeApplication(){
        fakeApplication = fakeApplication(inMemoryDatabase());
        start(fakeApplication);
        Users u1 = new Users("anas", "1234","a@a.com",true,false);
        Users u2 = new Users("anas2", "1234","a2@a.com",false,false);
        u1.save();
        u2.save();

    }

    @After
    public  void shutDownApplication(){
        stop(fakeApplication);
    }

    @Test
    public void testValidLogin(){
        Form<LoginForm> loginForm = Form.form(LoginForm.class);
        Map<String, String> data = new HashMap<>();
        data.put("username", "anas");
        data.put("password", "1234");

        Form<LoginForm> boundLoginForm = loginForm.bind(data);

        Assert.assertTrue(!boundLoginForm.hasErrors());
    }

    @Test
    public void testInvalidPassword(){
        Form<LoginForm> loginForm = Form.form(LoginForm.class);
        Map<String, String> data = new HashMap<>();
        data.put("username", "anas");
        data.put("password", "12345");

        Form<LoginForm> boundLoginForm = loginForm.bind(data);

        Assert.assertTrue(boundLoginForm.hasErrors());
        Assert.assertEquals(boundLoginForm.error("Error").message(), Users.USERNAME_PASSWORD_NOT_MATCHED);
    }

    @Test
    public void testInactiveUser(){
        Form<LoginForm> loginForm = Form.form(LoginForm.class);
        Map<String, String> data = new HashMap<>();
        data.put("username", "anas2");
        data.put("password", "1234");

        Form<LoginForm> boundLoginForm = loginForm.bind(data);

        Assert.assertTrue(boundLoginForm.hasErrors());
        Assert.assertEquals(boundLoginForm.error("Error").message(), Users.USER_NOT_ACTIVE);
    }

    @Test
    public void testInvalidUser(){
        Form<LoginForm> loginForm = Form.form(LoginForm.class);
        Map<String, String> data = new HashMap<>();
        data.put("username", "anas21");
        data.put("password", "1234");

        Form<LoginForm> boundLoginForm = loginForm.bind(data);

        Assert.assertTrue(boundLoginForm.hasErrors());
        Assert.assertEquals(boundLoginForm.error("Error").message(), Users.USER_NOT_EXIST);
    }
}

package controllertest;

import controllers.ot.forms.SignupForm;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.core.j.JavaResultExtractor;
import play.data.Form;
import play.mvc.Http;
import play.mvc.Result;
import play.test.FakeApplication;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static play.test.Helpers.*;

/**
 * Created by amd on 11/30/15.
 */
public class RegisterTest {
    FakeApplication fakeApplication ;

    @Before
    public void init(){
        fakeApplication = fakeApplication();
        start(fakeApplication);
    }

    @After
    public void destroy(){
        stop(fakeApplication);
    }



    public String getCSRFToken(String url){
        Result result = route(fakeRequest(GET, url));
        String prefix = "<input type=\"hidden\" name=\"csrfToken\" value=\"";
        String getBody = new String(JavaResultExtractor.getBody(result, 0L));
        int indexOfCsrfToken = getBody.indexOf(prefix);
        int start = indexOfCsrfToken + prefix.length();
        String csrfToken = "";
        for(; getBody.charAt(start)!='\"' ; start++ ){
            csrfToken = csrfToken + getBody.substring(start, start+1);
        }
        return csrfToken;
    }

    @Test
    public void testReistrationWithAuthenticUser() throws IOException {



        String token = getCSRFToken("/register");

        Map<String, String> map = new HashMap<String, String>();
        map.put("email", "a@gmail.com");
        map.put("username", "ansal10");
        map.put("password", "ansal10");
        map.put("confirmPassword", "ansal10");
        map.put("firstName", "anas");
        map.put("lastName", "md");
        map.put("csrfToken", token);

        Http.RequestBuilder fakeRequest = fakeRequest(POST, "/register").bodyForm(map);
//       fakeRequest.session("csrfToken", token);
        Result result = route(fakeRequest);
        String body = new String(JavaResultExtractor.getBody(result, 0L));
        System.out.print(body);
//        assertTrue(body.contains("error"));
        assertEquals(result.status(), 403);

        Form<SignupForm> signupForm= new Form<SignupForm>(SignupForm.class);
        //signupForm.
    }
}

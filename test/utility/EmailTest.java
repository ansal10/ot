package utility;

import controllers.ot.emailer.Emailer;
import org.junit.Test;

import java.net.MalformedURLException;

/**
 * Created by amd on 11/26/15.
 */
public class EmailTest {

    @Test
    public void testEmailSender() throws MalformedURLException {
        Emailer emailer = Emailer.getInstance();
        emailer.sendUserActivationMail("anas.ansal10@gmail.com", "213123123213");
    }
}

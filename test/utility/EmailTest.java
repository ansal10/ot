package utility;

import controllers.ot.emailer.Emailer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.MalformedURLException;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;

/**
 * Created by amd on 11/26/15.
 */
@RunWith(PowerMockRunner.class)
public class EmailTest {

    @PrepareForTest({ Emailer.class })
    @Test
    public void testEmailSender() throws MalformedURLException {

        Emailer mockEmailer = Mockito.mock(Emailer.class);

        doNothing().when(mockEmailer).sendEmail(anyString(), anyString(), anyString());

        PowerMockito.mockStatic(Emailer.class);
        PowerMockito.when(Emailer.getInstance()).thenReturn(mockEmailer);

        Emailer emailer = Emailer.getInstance();
        emailer.sendUserActivationMail("anas.ansal10@gmail.com", "213123123213");
    }
}

package controllers.ot.emailer;

import play.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

/**
 * Created by amd on 11/26/15.
 */
public class Emailer {

    private static final String USERNAME = "noreply.ot@gmail.com";
    private static final String PASSWORD = "Ansaal007";
    private static Emailer emailerInstance;
    private Session session;
    private MimeMessage message;
    private static final String LOGKEY = "Emailer : ";
    private static final String URL_PREFIX = "http://localhost:9000/";

    private Emailer(){

        Logger.info(LOGKEY + "Constructing Email Object");

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

         session = Session.getDefaultInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USERNAME, PASSWORD);//change accordingly
                    }
                });

        message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress("yourgmailid@gmail.com"));//change accordingly
        } catch (MessagingException e) {
            Logger.error(LOGKEY + e.getMessage());
        }
        Logger.info(LOGKEY + "EMail object constructed successfully");

    }

    public  void sendEmail(String userEmail, String subject, String body){

        try {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
            message.setSubject(subject);
            message.setContent(body, "text/html; charset=utf-8");
            Transport.send(message);
            Logger.info(LOGKEY + "email sended successfully! ");
        }catch (MessagingException e){
            Logger.error(LOGKEY + "error sending mail " + e.getMessage());
        }
    }

    public static Emailer getInstance(){
        if (emailerInstance == null)
            emailerInstance = new Emailer();
        return emailerInstance;
    }

    public boolean sendUserActivationMail(String userEmail , String token) {
        try {
            String subject = "Activate your Account";
            String template = "Welcome to OT , plesae click on below link to activate your account" +
                    " you can alternatively also copy and paste the link in your browser <br><br>" +
                    "<a href=\"{{LINK}}\"> CLICK HERE TO ACTIVE </a>";

            String link = URL_PREFIX + "activate?token=" + token;

            URL url = new URL(link);

            template = template.replace("{{LINK}}", url.toString());

            sendEmail(userEmail, subject, template);
            Logger.info(LOGKEY + "User activation email sended successfully");
            return true;
        }catch (MalformedURLException e){
            Logger.error(LOGKEY + "Email sending failed");
            return false;
        }
    }

    public boolean sendResetPasswordMail(String userEmail , String token) {
        try {
            String subject = "Reset your password";
            String template = "Welcome to OT , plesae click to reset password" +
                    " you can alternatively also copy and paste the link in your browser <br><br>" +
                    "<a href=\"{{LINK}}\"> CLICK HERE TO ACTIVE </a>";

            String link = URL_PREFIX + "reset_password?token=" + token;

            URL url = new URL(link);

            template = template.replace("{{LINK}}", url.toString());

            sendEmail(userEmail, subject, template);
            Logger.info(LOGKEY + "User activation email sended successfully");
            return true;
        }catch (MalformedURLException e){
            Logger.error(LOGKEY + "Email sending failed");
            return false;
        }
    }


}

package modeltests;

import models.ot.Enums.PermissionType;
import models.ot.Permission;
import models.ot.Users;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.test.FakeApplication;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import static org.junit.Assert.*;
import static play.test.Helpers.*;

/**
 * Created by amd on 11/21/15.
 */
public class UsersTest {

    FakeApplication fakeApplication;
    Users u1, u2 , u3;

    @Before
    public void init(){

        fakeApplication = fakeApplication(inMemoryDatabase());
        start(fakeApplication);
        u1 = new Users("anas", "pass", "a@gmail.com", "fname", "lname", true, false);
        u2 = new Users("anas2","pass","a@hotmail.com","fname", "lname", false, false);
        u1.save();
        u2.save();
        u3 = new Users("anas3", "pass", "a3@gmail.com","fname", "lname", true, true);
    }

    @After
    public void destroy(){
        stop(fakeApplication);
    }

    @Test
    public void testUserExist(){



        assertTrue(u1.exist());
        assertTrue(u2.exist());
        assertTrue(!u3.exist());
        assertEquals(u1.getResults().size(), 0);
        assertEquals(u1.getPermissions().size(), 0);

    }

    @Test
    public void testAuthentication(){
        try {
            Boolean authenticate = Users.authenticate(u2.getUsername(), "pass");
            assertTrue(false);
        }catch (Exception e){
            assertTrue(e.getMessage().equals(Users.USER_NOT_ACTIVE));
        }

        try {
            Boolean authenticate = Users.authenticate(u2.getUsername(), "password");
            assertTrue(false);
        }catch (Exception e){
            assertTrue(e.getMessage().equals(Users.USERNAME_PASSWORD_NOT_MATCHED));
        }

        try {
            Boolean authenticate = Users.authenticate("random", "password");
            assertTrue(false);
        }catch (Exception e){
            assertTrue(e.getMessage().equals(Users.USER_NOT_EXIST));
        }

        try {
            u3.setPasswordExpiredOn(new DateTime());
            u3.save();
            Thread.sleep(100);
            Boolean authenticate = Users.authenticate(u3.getUsername(), "pass");
            assertTrue(false);
        }catch (Exception e){
            assertTrue(e.getMessage().equals(Users.PASSWORD_EXPIRED));
        }

        try {
            Boolean authenticate = Users.authenticate("anas", "pass");
            assertTrue(authenticate);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void testPermissions(){
        PermissionType p1 = PermissionType.CAN_ADD_QUESTIONS;
        PermissionType p2 = PermissionType.CAN_CREATE_RANDOM_TEST;
        PermissionType p3 = PermissionType.CAN_GIVE_TEST;
        PermissionType p4 = PermissionType.CAN_RESET_TEST;

        u1.addPermission(p1);
        u1.addPermission(p2);
        u1.addPermission(p3);
        u2.addPermission(p3);
        u2.addPermission(p4);


        assertEquals(Permission.find.all().size(), 5);
        assertEquals(u1.getPermissions().size(), 3);
        assertTrue(u1.isPermitted(p1));
        assertFalse(u2.isPermitted(p1));

    }

    @Test
    public void testTokengeneration() throws NoSuchAlgorithmException, NoSuchProviderException, UnsupportedEncodingException {
        String token1 = u1.createToken();
        String token2 = u1.createToken();
        assertNotEquals(token1, token2);
    }

    @Test
    public void testActivateByToken() throws Exception {
        assertTrue(!u2.isActive());
        String token = u2.getToken();
        System.out.println(token);
        assertEquals(token.length(), 256);
        Users.activate(token);
        u2.refresh();
        assertTrue(u2.isActive());

    }

    @Test
    public void testActivateWithInvalidToken(){
        try {
            Users.activate("e2e32e23e23");
            assertTrue(false);
        }catch (Exception e){
            assertEquals(e.getMessage(), Users.TOKEN_NOT_EXIST);
        }
    }

    @Test
    public void testActivateWithExpiredToken(){
        try {
            String token = u2.getToken();
            u2.setTokenExpiredOn(DateTime.now().minusMinutes(10));
            u2.save();
            Users.activate(token);
            assertTrue(false);
        }catch (Exception e){
            assertEquals(e.getMessage(), Users.TOKEN_EXPIRED);
        }
    }

    @Test
    public void testPasswordReset() throws Exception {
        String token = u2.getToken();
        Users.resetPassword(token, "newpass");
        u2.refresh();
        assertTrue(Users.authenticate(u2.getUsername(), "newpass"));
    }

    @Test
    public void testfindUserbyUsername(){
        Users users1 = Users.findUserByUsername(u1.getUsername());
        Users users2 = Users.findUserByUsername(u2.getUsername());

        assertNotNull(users1);
        assertNull(users2);
    }
}

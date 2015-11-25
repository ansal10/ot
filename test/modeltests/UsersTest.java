package modeltests;

import models.ot.Enums.PermissionType;
import models.ot.Permission;
import models.ot.Users;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.test.FakeApplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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
        u1 = new Users("anas", "pass", "a@gmail.com", true, false);
        u2 = new Users("anas2","pass","a@hotmail.com", false, false);
        u1.save();
        u2.save();
        u3 = new Users("anas3", "pass", "a3@gmail.com", true, true);
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
}

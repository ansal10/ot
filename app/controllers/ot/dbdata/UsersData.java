package controllers.ot.dbdata;

import models.ot.Enums.PermissionType;
import models.ot.Users;

/**
 * Created by amd on 11/27/15.
 */
public class UsersData {

    public UsersData() throws Exception {
        if(Users.find.all().size()==0){
            Users users = new Users("anas", "ansal10", "anas.ansal10@gmail.com", "anas", "md", false, false);
            users.save();
            String token = users.getToken();
            Users.activate(token);
            users.addPermission(PermissionType.CAN_ADD_QUESTIONS);
        }
    }
}

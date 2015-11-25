package models.ot;

import com.avaje.ebean.Model;
import models.ot.Enums.PermissionType;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by amd on 11/20/15.
 */

@Entity
public class Permission extends Model {


    @Id
    public Long id;

    @ManyToOne
    private Users users;

    private PermissionType permission;

    public void save(){
        super.save();
        super.refresh();
    }

    public Permission(Users user , PermissionType permission){
        this.users = user;
        this.permission = permission;
    }

    public void addPermission(Users user , PermissionType permission){
        Permission permissions = new Permission(user, permission);
        permissions.save();
    }

    public static Finder<String, Permission> find = new Finder<String, Permission>(Permission.class);


    public boolean equal(Permission perm){
        return this.getPermission().equals(perm.getPermission());
    }

    public Users getUsers() {
        return users;
    }

    public void setUser(Users user) {
        this.users = user;
    }

    public PermissionType getPermission() {
        return permission;
    }

    public void setPermission(PermissionType permission) {
        this.permission = permission;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

package models.ot;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Model;
import com.google.common.hash.Hashing;
import models.ot.Enums.PermissionType;
import org.apache.commons.codec.Charsets;
import org.joda.time.DateTime;
import play.data.validation.Constraints;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
/**
 * Created by amd on 11/20/15.
 */

@Entity(name = "Users")
public class Users extends Model {

    @Id
    private Long id ;

    @Column(unique = true)
    private String username;

    @Column(length = 256)
    private String usernameHash;

    @Column(length = 256)
    private String passwordHash;

    @Column(length = 512)
    private String passwordSalt;

    private Boolean Active;
    private Boolean SuperUser;
    private DateTime createdOn;
    private DateTime passwordExpiredOn;

    @Constraints.Email(message = "Not a valid Email")
    private String email;

    @OneToMany
    public List<Result> results;

    @OneToMany
    public List<Permission> permissions;


    public Users(String username, String password, String email, Boolean Active, Boolean SuperUser) {

        DateTime now = new DateTime();

        this.username = username;
        this.Active = Active;
        this.SuperUser = SuperUser;
        this.createdOn = now;
        this.passwordExpiredOn = now.plusYears(1);
        this.email = email;
        this.passwordHash = Hashing.sha256().hashString(password, Charsets.UTF_8).toString();
        this.passwordSalt = Hashing.sha512().hashString(this.passwordHash, Charsets.UTF_8).toString();
    }

    public static Finder<String, Users> find = new Finder<String, Users>(Users.class);

    public boolean exist(){
        Users user = Users.find.where().disjunction().add(Expr.eq("username", this.username))
                .add(Expr.eq("email", this.email)).findUnique();

        return user != null;
    }

    public static boolean authenticate(String username, String password) throws Exception {
        String passwordHash = Hashing.sha256().hashString(password, Charsets.UTF_8).toString();
        String passwordSalt = Hashing.sha512().hashString(passwordHash, Charsets.UTF_8).toString();
        Users user = Users.find.where().eq("username",username).findUnique();
        if(user == null)
            throw new Exception(USER_NOT_EXIST);
        if(user.getPasswordHash().equals(passwordHash) && user.getPasswordSalt().equals(passwordSalt)){
            if(!user.isActive())
                throw new Exception(USER_NOT_ACTIVE);
            if(user.getPasswordExpiredOn().getMillis() < new DateTime().getMillis())
                throw new Exception(PASSWORD_EXPIRED);
            return true;
        }else throw new Exception(USERNAME_PASSWORD_NOT_MATCHED);
    }

    public void save(){
        super.save();
        super.refresh();
    }

    public void addPermission(PermissionType permission){

        List<Permission> permissionList = this.getPermissions();
        if(permissionList.contains(new Permission(null, permission)))
            return;
        Permission permissions = new Permission(this, permission);
        permissions.save();
        this.refresh();
    }

    public boolean isPermitted(PermissionType permission){


        Permission perm = Permission.find.where().eq("users_id",String.valueOf(this.getId()))
                .eq("permission", permission).findUnique();
        return perm != null;

    }


    public static final String USER_NOT_EXIST =  "User does not exist";
    public static final String USER_NOT_ACTIVE =  "User is not active";
    public static final String PASSWORD_EXPIRED = "Password expired";
    public static final String USERNAME_PASSWORD_NOT_MATCHED = "Username and password not matched";


    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getUsernameHash() {
        return usernameHash;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public Boolean getActive() {
        return Active;
    }

    public Boolean getSuperUser() {
        return SuperUser;
    }

    public DateTime getCreatedOn() {
        return createdOn;
    }

    public DateTime getPasswordExpiredOn() {
        return passwordExpiredOn;
    }

    public String getEmail() {
        return email;
    }

//    public List<Test> getTests() {
//        return tests;
//    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUsernameHash(String usernameHash) {
        this.usernameHash = usernameHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public void setActive(Boolean active) {
        this.Active = active;
    }

    public void setSuperUser(Boolean superUser) {
        this.SuperUser = superUser;
    }

    public void setCreatedOn(DateTime createdOn) {
        this.createdOn = createdOn;
    }

    public void setPasswordExpiredOn(DateTime passwordExpiredOn) {
        this.passwordExpiredOn = passwordExpiredOn;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    public void setTests(List<Test> tests) {
//        this.tests = tests;
//    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public boolean isActive(){
        return this.getActive();
    }

    public boolean isSuperUser(){
        return this.getSuperUser();
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }
}

package models.ot;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.Index;
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
import java.util.UUID;

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

    @Column(length = 256)
    private String passwordSalt;

    private Boolean Active;
    private Boolean SuperUser;
    private DateTime createdOn;
    private DateTime passwordExpiredOn;

    @Column(length = 256)
    @Index
    private String token;

    private DateTime tokenExpiredOn;

    @Constraints.Email(message = "Not a valid Email")
    private String email;

    @OneToMany
    public List<Result> results;

    @OneToMany
    public List<Permission> permissions;

    public String firstName;
    public String lastName;


    public Users(String username, String password, String email, String firstName, String lastName, Boolean Active, Boolean SuperUser) {

        DateTime now = new DateTime();

        this.username = username;
        this.Active = Active;
        this.SuperUser = SuperUser;
        this.createdOn = now;
        this.passwordExpiredOn = now.plusYears(1);
        this.email = email;
        this.passwordHash = Hashing.sha256().hashString(password, Charsets.UTF_8).toString();
        this.passwordSalt = Hashing.sha512().hashString(this.passwordHash, Charsets.UTF_8).toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.usernameHash = Hashing.sha256().hashString(this.username, Charsets.UTF_8).toString();
        this.setToken( createToken() );
        this.setTokenExpiredOn(DateTime.now().plusMinutes(30));

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

    public String createToken() {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        String token = Hashing.sha512().hashString(uuid1.toString(), Charsets.UTF_8).toString() +
                Hashing.sha512().hashString(uuid2.toString(), Charsets.UTF_8).toString() ;
        return token;
    }

    public static boolean activate(String token) throws Exception {
        Users users = Users.find.where().eq("token", token).findUnique();
        if (users == null)
            throw new Exception(Users.TOKEN_NOT_EXIST);
        if(DateTime.now().getMillis() > users.getTokenExpiredOn().getMillis())
            throw new Exception(TOKEN_EXPIRED);
        users.setActive(true);
        users.save();
        return true;
    }

    public static boolean resetPassword(String token, String password) throws Exception {
        Users users = Users.find.where().eq("token", token).findUnique();
        if (users == null)
            throw new Exception(Users.TOKEN_NOT_EXIST);
        if(DateTime.now().getMillis() > users.getTokenExpiredOn().getMillis())
            throw new Exception(TOKEN_EXPIRED);
        users.setRawPassword(password);
        users.setActive(true);
        users.save();
        return true;
    }

    public static final String USER_NOT_EXIST =  "User does not exist";
    public static final String USER_NOT_ACTIVE =  "User is not active";
    public static final String PASSWORD_EXPIRED = "Password expired";
    public static final String USERNAME_PASSWORD_NOT_MATCHED = "Username and password not matched";
    public static final String TOKEN_NOT_EXIST = "The token does not exist";
    public static final String TOKEN_EXPIRED = "The token expired";


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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public DateTime getTokenExpiredOn() {
        return tokenExpiredOn;
    }

    public void setTokenExpiredOn(DateTime tokenExpiredOn) {
        this.tokenExpiredOn = tokenExpiredOn;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void resetToken(){
        this.setToken(createToken());
        this.setTokenExpiredOn(DateTime.now().plusMinutes(30));
        this.save();
    }

    public void setRawPassword(String password){
        this.passwordHash = Hashing.sha256().hashString(password, Charsets.UTF_8).toString();
        this.passwordSalt = Hashing.sha512().hashString(this.passwordHash, Charsets.UTF_8).toString();
    }
}

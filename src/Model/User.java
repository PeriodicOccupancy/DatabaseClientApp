package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Timestamp;

/** Class for initializing and controlling user objects.
 @author  Mike Fasnacht, mfasnac@wgu.edu
 @version  1.0, September 26th, 2022
 */
public class User {

    private int userID;
    private String username;
    private String password;
    private Timestamp createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;

    /** Constructor for creating User objects. */
    public User(int userID, String username, String password, Timestamp createDate,String createdBy, Timestamp lastUpdate, String lastUpdatedBy){

        this.userID = userID;
        this.username = username;
        this.password = password;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /** Alternate constructor for creating an empty user object. */
    public User() {

    }

    /** Static User object/pointer to track the currently active user. */
    public static User activeUser = new User(); //static user class to track the current Active User

    private static ObservableList<User> userList = FXCollections.observableArrayList();

        /** Method to add a user to the userList. */
        public static void addUser(User user){
        userList.add(user);
    }

    /** Method to return the userList. */
        public static ObservableList<User> getUserList(){
        return userList;
    }

    /** Method to delete a user from the userList. */
        public static void deleteUser(User user){
        userList.remove(user);
    }

    /** Gets the creation date. */
    public Timestamp getCreateDate() {
        return createDate;
    }

    /** Sets the creation date. */
    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    /** Gets the creator's username. */
    public String getCreatedBy() {
        return createdBy;
    }

    /** Sets the creator's username. */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /** Gets the time of last update. */
    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    /** Sets the time of last update. */
    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /** Gets the user's Password. */
    public String getPassword() {
        return password;
    }

    /** Sets the user's Password. */
    public void setPassword(String password) {
        this.password = password;
    }

    /** Gets the username. */
    public String getUsername() {
        return username;
    }

    /** Sets the username. */
    public void setUsername(String username) {
        this.username = username;
    }

    /** Gets the userID. */
    public int getUserID() {
        return userID;
    }

    /** Sets the userID. */
    public void setUserID(int userID) {
        this.userID = userID;
    }

}

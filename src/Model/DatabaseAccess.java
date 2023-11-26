package Model;

import Model.Enums.AccessType;
import Model.Enums.ObjAccessed;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static helper.Helper.toLocalFromZoned;

/** Class for initializing and controlling DatabaseAccess objects.
 @author  Mike Fasnacht, mfasnac@wgu.edu
 @version  1.0, September 26th, 2022
 */
public class DatabaseAccess implements Serializable {

    private String username;
    private int userID;
    private AccessType accessType;
    private ObjAccessed objManipulated;
    private LocalDate date;
    private LocalTime time;
    private ZonedDateTime zonedDateTime;
    private LocalDateTime dateTime;
    private int objID;

    //this constructor is for passing the current activeUser and other arguments easily from my sql methods to create
    // DatabaseAccess objects and then add them to accessLis to display as my custom report
    /** DatabaseAccess constructor that also adds the object immediately to the accessList.
     * @param user The user that preformed the Database Access.
     * @param accessType The type of access performed.
     * @param  objManipulated The object affected (Customer or Appointment).
     * @param objID The unique int identifier of the object affected. */
    public DatabaseAccess(User user, AccessType accessType, ObjAccessed objManipulated, int objID){

        this.userID = user.getUserID();
        this.username = user.getUsername();
        this.accessType = accessType;
        this.objManipulated = objManipulated;
        this.objID = objID;
        this.zonedDateTime = ZonedDateTime.now();
        accessList.add(this);

    }

    private static ObservableList<DatabaseAccess> accessList = FXCollections.observableArrayList();

        /** Method to return the accessList. */
        public static ObservableList<DatabaseAccess> getAccessList () {
        return accessList;
        }

        /** Method to add a DatabaseAccess object to the accessList. */
        public static void addAccessList (DatabaseAccess dba){
        accessList.add(dba);
        }

    /** Gets the Associated username. */
    public String getUsername() {
        return username;
    }

    /** Sets the associated username. */
    public void setUsername(String username) {
        this.username = username;
    }

    /** Gets the associated User ID. */
    public int getUserID() {
        return userID;
    }

    /** Sets the associated User ID. */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /** Gets the zoned date and time. */
    public ZonedDateTime getZonedDateTime() {
        return zonedDateTime;
    }

    /** Sets the zoned date and time. */
    public void setZonedDateTime(ZonedDateTime zonedDateTime) {
        this.zonedDateTime = zonedDateTime;
    }

    /** Gets the zoned date and time. */
    public LocalDateTime getDateTime() {
        LocalDateTime localDateTime = toLocalFromZoned(zonedDateTime);
        return localDateTime;
    }

    /** Gets the Access Type. */
    public AccessType getAccessType() {
        return accessType;
    }

    /** Sets the Access Type. */
    public void setAccessType(AccessType accessType) {
        this.accessType = accessType;
    }

    //gets a LocalDate object from the stored ZonedDateTime object
    /** Method to return a string of LocalDate, in the system default timezone from the zonedDateTime of the object. */
    public String getDate() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("LLL dd, uuuu");
            LocalDateTime localDateTime = toLocalFromZoned(zonedDateTime);
            LocalDate returnDate = localDateTime.toLocalDate();
            return returnDate.format(dateFormatter);
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    //gets a LocalTime object from the stored ZonedDateTime object
    /** Method to return a string of LocalTime, in the system default timezone from the zonedDateTime of the object. */
    public String getTime() {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm:ss a");
            LocalDateTime localDateTime = toLocalFromZoned(zonedDateTime);
            LocalTime returnTime = localDateTime.toLocalTime();

            return timeFormatter.format(returnTime);
    }

    /** Returns the Class type associated with this object. */
    public ObjAccessed getObjManipulated() {
        return objManipulated;
    }

    /** Returns the ID of the Customer or Appointment this access modified. */
    public int getObjID() {
        return objID;
    }

    /** Overridden toString method for the DatabaseAccess object. */
    @Override
    public String toString() {
        return "UserID:" + userID + "\nUsername: " + username + "\nAccess Type: " + accessType + "\nObject Manipulated: " + objManipulated + "\n Object ID: " + objID + "\nZoned Date Time: " + zonedDateTime + "\n";
    }
}

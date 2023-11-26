package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Timestamp;

/** Class for initializing and controlling division objects.
 @author  Mike Fasnacht, mfasnac@wgu.edu
 @version  1.0, September 26th, 2022
 */
public class FirstLevelDivision {

    private int divisionID;
    private String division;
    private int countryID;
    private Timestamp createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;

    /** Constructor to create division objects. */
    public FirstLevelDivision(int divisionID, String division, int countryID, Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy) {

        this.divisionID = divisionID;
        this.division = division;
        this.countryID = countryID;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    private static ObservableList<FirstLevelDivision> divisionList = FXCollections.observableArrayList();

        /** Method to add a contact to the contactList. */
        public static void addDivision(FirstLevelDivision division) {
        divisionList.add(division);
        }

    /** Method to return the divisionList. */
        public static ObservableList<FirstLevelDivision> getDivisionList(){
            return divisionList;
        }

    /** Gets the Division ID. */
    public int getDivisionID() {
        return divisionID;
    }

    /** Sets the Division ID. */
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    /** Gets the Division Name. */
    public String getDivision() {
        return division;
    }

    /** Sets the Division Name. */
    public void setDivision(String division) {
        this.division = division;
    }

    /** Gets the Country ID. */
    public int getCountryID() {
        return countryID;
    }

    /** Sets the Country ID. */
    public void setCountryID(int countryID) {
        this.countryID = countryID;
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

}


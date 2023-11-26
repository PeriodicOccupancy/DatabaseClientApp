package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Timestamp;

/** Class for initializing and controlling country objects.
 @author  Mike Fasnacht, mfasnac@wgu.edu
 @version  1.0, September 26th, 2022
 */
public class Country {
    private int countryID;
    private String country;
    private Timestamp createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;

    /** Constructor for Country class objects. */
    public Country(int countryID, String country, Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdateBy){
        this.countryID = countryID;
        this.country = country;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
    }

    private static ObservableList<Country> countryList = FXCollections.observableArrayList();

        /** Method to add a contact to the countryList. */
        public static void addCountry(Country country){
            countryList.add(country);
        }

        /** Method to return the countryList. */
        public static ObservableList<Country> getCountryList(){
            return countryList;
        }

    /** Gets the Country ID. */
    public int getCountryID() {
        return countryID;
    }

    /** Sets the Country ID. */
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    /** Gets the Country name. */
    public String getCountry() {
        return country;
    }

    /** Sets the Country name. */
    public void setCountry(String country) {
        this.country = country;
    }
}

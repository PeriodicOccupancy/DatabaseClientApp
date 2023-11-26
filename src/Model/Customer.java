package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Timestamp;

import static helper.Helper.getDivisionName;

/** Class for initializing and controlling customer objects.
 @author  Mike Fasnacht, mfasnac@wgu.edu
 @version  1.0, September 26th, 2022
 */
public class Customer {

    private int customerID;
    private int divisionID;

    private String customerName;
    private String address;
    private String postalCode;
    private String phoneNum;
    private Timestamp createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;
    private String customerDivisionName;

    /** Constructor for creating Customer objects.
     * @param customerID Unique ID identifier per Customer.
     * @param divisionID Unique ID of the division (state or territory) where this customer resides.
     * @param customerName The Customer's name.
     * @param address Street and City address of the customer.
     * @param postalCode Customer's post code.
     * @param phoneNum Customer's phone Number.
     * @param createDate Date of customer object creation.
     * @param  createdBy User that entered the Customers data.
     * @param lastUpdate Date of the last update to customer information.
     * @param  lastUpdatedBy User that last updated the Customer data. */
    public Customer(int customerID, int divisionID, String customerName, String address, String postalCode, String phoneNum, Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy){

        this.customerID = customerID;
        this.divisionID = divisionID;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNum = phoneNum;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.customerDivisionName = getDivisionName(divisionID);
    }
    /** Constructor for creating new customer objects without a Customer ID.
     *  The customer ID is set after the fact by a separate method, pulling the ID from the database.
     * @param divisionID Unique ID of the division (state or territory) where this customer resides.
     * @param customerName The Customer's name.
     * @param address Street and City address of the customer.
     * @param postalCode Customer's post code.
     * @param phoneNum Customer's phone Number.
     * @param createDate Date of customer object creation.
     * @param  createdBy User that entered the Customers data.
     * @param lastUpdate Date of the last update to customer information.
     * @param  lastUpdatedBy User that last updated the Customer data. */
    public Customer(int divisionID, String customerName, String address, String postalCode, String phoneNum, Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy){

        this.divisionID = divisionID;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNum = phoneNum;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.customerDivisionName = getDivisionName(divisionID);
    }
    /** Constructor method for updating an existing customer.
     * @param divisionID Unique ID of the division (state or territory) where this customer resides.
     * @param customerName The Customer's name.
     * @param address Street and City address of the customer.
     * @param postalCode Customer's post code.
     * @param phoneNum Customer's phone Number.
     * @param lastUpdate Date of the last update to customer information.
     * @param  lastUpdatedBy User that last updated the Customer data. */
    public void UpdateCustomer(int divisionID, String customerName, String address, String postalCode, String phoneNum, Timestamp lastUpdate, String lastUpdatedBy){


        this.setDivisionID(divisionID);
        this.setCustomerName(customerName);
        this.setAddress(address);
        this.setPostalCode(postalCode);
        this.setPhoneNum(phoneNum);
        this.setLastUpdate(lastUpdate);
        this.setLastUpdatedBy(lastUpdatedBy);
        this.setCustomerDivisionName(getDivisionName(divisionID));
    }

    /** Static customer Pointer/Object for tracking the original details of a customer before an update. */
    public static Customer customerUpdate;

    private static ObservableList<Customer> customerList = FXCollections.observableArrayList();

        /** Method to add a customer to the customerList. */
        public static void addCustomer(Customer customer){
        customerList.add(customer);
    }

    /** Method to return the customerList. */
        public static ObservableList<Customer> getCustomerList(){
        return customerList;
    }

    /** Method to delete a customer from the customerList. */
        public static void deleteCustomer(Customer customer){
        customerList.remove(customer);
    }

    /** Gets the Customer ID. */
    public int getCustomerID() {
        return customerID;
    }

    /** Sets the Customer ID. */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /** Gets the Division ID. */
    public int getDivisionID() {
        return divisionID;
    }

    /** Sets the Division ID. */
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    /** Gets the Customer name. */
    public String getCustomerName() {
        return customerName;
    }

    /** Sets the Customer name. */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /** Gets the Customer address. */
    public String getAddress() {
        return address;
    }

    /** Sets the Customer address. */
    public void setAddress(String address) {
        this.address = address;
    }

    /** Gets the Customer post code. */
    public String getPostalCode() {
        return postalCode;
    }

    /** Sets the Customer post code. */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /** Gets the Customer phone number. */
    public String getPhoneNum() {
        return phoneNum;
    }

    /** Sets the Customer phone number. */
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
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

    /** Gets the username of the last person to update. */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /** Sets the username of the last person to update. */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /** Gets the Customers Division Name. */
    public String getCustomerDivisionName() {
        return customerDivisionName;
    }

    /** Sets the Customers Division Name. */
    public void setCustomerDivisionName(String customerDivisionName) {
        this.customerDivisionName = customerDivisionName;
    }
}

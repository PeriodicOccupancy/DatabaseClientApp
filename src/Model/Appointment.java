package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.*;
import java.time.format.DateTimeFormatter;

/** Class for initializing and controlling appointment objects.
 @author  Mike Fasnacht, mfasnac@wgu.edu
 @version  1.0, September 26th, 2022
 */
public class Appointment {

    private int appointmentID;
    private int customerID;
    private int userID;
    private int contactID;

    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdateBy;
    private String startString;
    private String endString;
    private Month month;
    private Year year;
    private YearMonth yearMonth;

    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("h:mm a \nM/d/uu");

    /** Constructor for creating Appointment objects.
     * @param appointmentID int for tracking unique ID per appointment.
     * @param contactID  int for tracking unique Contact object associated with an appointment.
     * @param userID int for tracking unique User object associated with an appointment.
     * @param customerID int for tracking unique Customer object associated with an appointment.
     * @param title String value for naming appointments.
     * @param description String value for giving a short description of an appointment.
     * @param location String value for declaring where an appointment takes place.
     * @param type String value for assigning a type to an appointment.
     * @param start LocalDateTime for tracking when an appointment starts.
     * @param end LocalDate time for tracking when an appointment ends.
     * @param createDate LocalDateTime for tracking when an appointment was created.
     * @param createdBy String for tracking who (usually a username) that created the appointment.
     * @param lastUpdate LocalDateTime for tracking when an appointment was last updated.
     * @param lastUpdateBy String for tracking who (usually a username) last updated the appointment. */
    public Appointment (int appointmentID, int customerID, int userID, int contactID, String title, String description, String location, String type,
                        LocalDateTime start, LocalDateTime end, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdateBy){
        this.appointmentID = appointmentID;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
        this.setStartString(start);
        this.setEndString(end);
        this.month = start.getMonth();
        this.year = Year.of(start.getYear());
        this.yearMonth = YearMonth.from(start.toLocalDate());
        if(!typeList.contains(type))
            addTypeList(type);
    }
    /** Empty constructor for creating a new blank appointment. */
    public Appointment(){

    }

    //Updater/Constructor method to be used inside CreateAppointmentController
    /** Constructor/update method for updating an existing appointment object.
     * @param contactID  int for tracking unique Contact object associated with an appointment.
     * @param userID int for tracking unique User object associated with an appointment.
     * @param customerID int for tracking unique Customer object associated with an appointment.
     * @param title String value for naming appointments.
     * @param description String value for giving a short description of an appointment.
     * @param location String value for declaring where an appointment takes place.
     * @param type String value for assigning a type to an appointment.
     * @param start LocalDateTime for tracking when an appointment starts.
     * @param end LocalDate time for tracking when an appointment ends.
     * @param lastUpdate LocalDateTime for tracking when an appointment was last updated.
     * @param lastUpdateBy String for tracking who (usually a username) last updated the appointment. */
    public void UpdateAppointment (int customerID, int userID, int contactID, String title, String description, String location, String type,
                                   LocalDateTime start, LocalDateTime end, LocalDateTime lastUpdate, String lastUpdateBy) {
        setCustomerID(customerID);
        setUserID(userID);
        setContactID(contactID);
        setTitle(title);
        setDescription(description);
        setLocation(location);
        setType(type);
        setStart(start);
        setEnd(end);
        setLastUpdate(lastUpdate);
        setLastUpdateBy(lastUpdateBy);
        setStartString(start);
        setEndString(end);
    }

    public static Appointment appointmentUpdate;    //Static Appointment object for passing a selected appointment from
                                                    //AppointmentsController to CreateAppointmentsController

    private static ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        /** Method for adding appointments to the appointList.
         * @param appointment The appointment to be added to the list. */
        public static void addAppointment (Appointment appointment){
        appointmentList.add(appointment);
    }

        /** Method for returning the appointmentList.
         * @return  returns the static appointmentList. */
        public static ObservableList<Appointment> getAppointmentList () {
        return appointmentList;
    }

        /** Method for deleting an appointment from the appointmentList.
         * @param appointment  appointment to be removed from the list. */
        public static void deleteAppointment (Appointment appointment){
        appointmentList.remove(appointment);
    }

    private static ObservableList<Appointment> weeklyAppointmentList = FXCollections.observableArrayList();

        /** Method to add an appointment to the weeklyAppointmentList.
         * @param appointment appointment to add to the weekly appointment list. */
        public static void addWeeklyAppointment(Appointment appointment){
        weeklyAppointmentList.add(appointment);
    }

        /** Method to return the weeklyAppointmentList.
         * @return returns the weekly appointment list. */
        public static ObservableList<Appointment> getWeeklyAppointmentList(){
        return weeklyAppointmentList;
    }

        /** Method for deleting an appointment from the weeklyAppointmentList.
         * @param appointment  appointment to delete from the weekly appointment list. */
        public static void deleteWeeklyAppointment(Appointment appointment){
            weeklyAppointmentList.remove(appointment);
    }

    private static ObservableList<Appointment> monthlyAppointmentList = FXCollections.observableArrayList();

        /** Method for adding an appointment to the monthlyAppointmentList.
         * @param appointment appointment to add to the monthly appointment list. */
        public static void addMonthlyAppointment(Appointment appointment){
        monthlyAppointmentList.add(appointment);
    }

        /** Method for returning the monthlyAppointmentList.
         * @return returns the static monthly appointment list. */
        public static ObservableList<Appointment> getMonthlyAppointmentList(){
        return monthlyAppointmentList;
    }

        /** Method for deleting an appointment from the monthlyAppointmentList.
         * @param appointment appointment to delete from the monthly appointment list. */
        public static void deleteMonthlyAppointment(Appointment appointment){
        monthlyAppointmentList.remove(appointment);
    }

    private static ObservableList<String> typeList = FXCollections.observableArrayList();

        /** Method for adding a type String to the typelist. */
        public static void addTypeList(String type){
            typeList.add(type);
        }

        /** Method for returning the typeList. */
        public static ObservableList<String> getTypeList(){
            return typeList;
        }

        /** Unused Method for deleting the type String from the typeList. */
        public static void deleteFromTypeList(String type){
            typeList.remove(type);
        }

        /** Method for deleting a selected appointment from all appointment lists. */
    public static void deleteFromAllAppointmentLists(Appointment appointment){
            if(getAppointmentList().contains(appointment))
                deleteAppointment(appointment);
            if(getWeeklyAppointmentList().contains(appointment))
                deleteWeeklyAppointment(appointment);
            if(getMonthlyAppointmentList().contains(appointment))
                deleteMonthlyAppointment(appointment);
    }


    /** Returns the appointment ID. */
    public int getAppointmentID() {
        return appointmentID;
    }

    /** Sets the appointment ID. */
    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    /** Gets the Customer ID. */
    public int getCustomerID() {
        return customerID;
    }

    /** Sets the Customer ID. */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /** Gets the User ID. */
    public int getUserID() {
        return userID;
    }

    /** Sets the User ID. */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /** Gets the Contact ID. */
    public int getContactID() {
        return contactID;
    }

    /** Sets the Contact ID. */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    /** Gets the Title. */
    public String getTitle() {
        return title;
    }

    /** Sets the Title. */
    public void setTitle(String title) {
        this.title = title;
    }

    /** Gets the Description. */
    public String getDescription() {
        return description;
    }

    /** Sets the Description. */
    public void setDescription(String description) {
        this.description = description;
    }

    /** Gets the Location. */
    public String getLocation() {
        return location;
    }

    /** Sets the Location. */
    public void setLocation(String location) {
        this.location = location;
    }

    /** Gets the Type. */
    public String getType() {
        return type;
    }

    /** Method to set the type string.
     * like the constructor also adds the type String to the typeList if not already contained. */
    public void setType(String type) {
        this.type = type;
        if(!typeList.contains(type))
            addTypeList(type);
    }

    /** Gets the Start time. */
    public LocalDateTime getStart() {
        return start;
    }

    /** Set Start Method, also sets the startString, year(Unused), month(Unused), and yearMonth(Unused).  */
    public void setStart(LocalDateTime start) {
        this.start = start;
        this.setStartString(start);
        this.setYear(Year.from(start));
        this.setMonth(Month.from(start));
        this.setYearMonth(YearMonth.from(start));
    }

    /** Gets the end time. */
    public LocalDateTime getEnd() {
        return end;
    }

    /** Sets the end time. */
    public void setEnd(LocalDateTime end) {
        this.end = end;
        this.setEndString(end);
    }

    /** Gets the creation date. */
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    /** Sets the creation date. */
    public void setCreateDate(LocalDateTime createDate) {
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
    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    /** Sets the time of last update. */
    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /** Gets the username of the last person to update. */
    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    /** Sets the username of the last person to update. */
    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    /** Gets the start time as a string. */
    public String getStartString() {
        return startString;
    }

    /** Sets the start time as a string. */
    public void setStartString(String startString) {
        this.startString = endString;
    }

    /** Method to set the start string in the specified format. */
    public void setStartString(LocalDateTime localDateTime){
            this.startString = dateTimeFormatter.format(localDateTime);
    }

    /** Gets the end time as a string. */
    public String getEndString() {
        return endString;
    }

    /** Sets the end time as a string. */
    public void setEndString(String endString) {
        this.endString = endString;
    }

    /** Method to set the End string in the specified format. */
    public void setEndString(LocalDateTime localDateTime){
        this.endString = dateTimeFormatter.format(localDateTime);
    }

    /** Gets the Year and month combination. */
    public YearMonth getYearMonth() {
        return yearMonth;
    }

    /** Sets the Year and month combination. */
    public void setYearMonth(YearMonth yearMonth) {
        this.yearMonth = yearMonth;
    }

    /** Gets the month. */
    public Month getMonth() {
        return month;
    }

    /** Sets the month. */
    public void setMonth(Month month) {
        this.month = month;
    }

    /** Gets the year. */
    public Year getYear() {
        return year;
    }

    /** Sets the year. */
    public void setYear(Year year) {
        this.year = year;
    }
}

package helper;

import Model.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static Model.Appointment.addTypeList;
import static Model.Appointment.getAppointmentList;
import static Model.Contact.getContactList;
import static Model.Country.getCountryList;
import static Model.Customer.getCustomerList;
import static Model.FirstLevelDivision.getDivisionList;
import static Model.User.activeUser;
import static Model.User.getUserList;
import static helper.AppointmentCRUD.populateAppointments;
import static helper.ContactCRUD.populateContacts;
import static helper.CountryCRUD.populateCountries;
import static helper.CustomerCRUD.populateCustomers;
import static helper.DatabaseAccessIO.databaseAccessReadFile;
import static helper.DivisionCRUD.populateDivisions;
import static helper.UserCRUD.populateUsers;

/** General Helper Class to assist with many operations.
 @author  Mike Fasnacht, mfasnac@wgu.edu
 @version  1.0, September 26th, 2022
 */
public class Helper {

    /** Method that contains all the methods and operations needed to run at application startup. */
    public static void  populateLists() throws SQLException, IOException, ClassNotFoundException {
        populateDivisions();
        populateCountries();
        addTypeList(null);
        populateAppointments();
        populateContacts();
        populateCustomers();
        populateUsers();
        databaseAccessReadFile();
    }

    /** Method to return a name String from an id int.
     * Takes an ID, and compares it to divisions in the divisionList, to find a match and return the name String. */
    public static String getDivisionName(int divisionID){
        String divisionName;
        for(FirstLevelDivision currentDivision : getDivisionList()){
            if (currentDivision.getDivisionID() == divisionID){
                divisionName = currentDivision.getDivision();
                return divisionName;
            }
        }
        return null;
    }

    /** Method to return a name String from an id int.
     * Takes an ID, and compares it to customers in the customerList, to find a match and return the name String. */
    public static String getCustomerName(int customerID){
        String customerName;
        for(Customer customer : getCustomerList()) {
            if(customer.getCustomerID() == customerID) {
                customerName = customer.getCustomerName();
                return customerName;
            }
        }
        System.out.println("Customer not found");
        return  null;
    }
    /** Method to return a name String from an id int.
     * Takes an ID, and compares it to users in the userList, to find a match and return the name String. */
    public static String getUserName (int userID){
        String username;
        for(User user : getUserList()){
            if(user.getUserID() == userID){
                username = user.getUsername();
                return username;
            }
        }
        System.out.println("User not found");
        return null;
    }
    /** Method to return a name String from an id int.
     * Takes an ID, and compares it to contacts in the contactList, to find a match and return the name String. */
    public static String getContactName(int contactID){
        String contactName;
        for(Contact contact : getContactList()){
            if(contact.getContactID() == contactID){
                contactName = contact.getContactName();
                return contactName;
            }
        }
        System.out.println("Contact not found");
        return null;
    }
    /** Method to return an id int, from a name String.
     * Takes a String, and compares it to contact names in the contactList, to find a match and return the id int. */
    public static int getContactID(String contactName){
        for(Contact contact : getContactList()){
            if(contact.getContactName().equals(contactName)){
                return contact.getContactID();
            }
        }
        return -1;
    }

    /** Method to return a name String from an id int.
     * Takes an ID, and compares it to countries in the countryList, to find a match and return the name String. */
    public static String getCountryName(int divisionID){
        int countryID = -1;
        for(FirstLevelDivision division : getDivisionList()){
            if(division.getDivisionID() == divisionID){
                countryID = division.getCountryID();
                break;
            }
        }
        if(countryID == -1) {
            System.out.println("An error occurred in Helper.getCountryName; division not found");
            return null;
        }
        for(Country country : getCountryList()){
            if(country.getCountryID() == countryID)
                return country.getCountry();
        }
        return null;
    }

    /** Method to return an id int, from a name String.
     * Takes a String, and compares it to division names in the divisionList, to find a match and return the id int. */
    public static int getDivisionID(String name) {
        for(FirstLevelDivision division : getDivisionList()){
            if(division.getDivision().equals(name)){
                return division.getDivisionID();
            }
        }
        System.out.println("Something went wrong in Helper.getDivisionID() could not find division");
        return -1;
    }

    //static immutable zoneId objects used for converting to and from UTC when interacting with the database
    static final ZoneId zoneUTC = ZoneId.of("UTC");
    static final ZoneId zoneEST = ZoneId.of("America/New_York");
    static final ZoneId zoneLocal = ZoneId.systemDefault();

    //2 methods for converting between local and UTC timezones to enable easy conversions of Timestamp/LocalDateTime
    // objects when working in the database
    /** Method for converting UTC times to the system's local time. */
    public static LocalDateTime toLocal(LocalDateTime localDateTime){

        ZonedDateTime convertedZoneDateTime = localDateTime.atZone(zoneUTC).withZoneSameInstant(zoneLocal);
        LocalDateTime convertedLocalDateTime = convertedZoneDateTime.toLocalDateTime();

        return convertedLocalDateTime;
    }

    /** Method for converting the system's local time to UTC time. */
    public static  LocalDateTime toUTC(LocalDateTime localDateTime){
        ZonedDateTime convertedZoneDateTime = localDateTime.atZone(zoneLocal).withZoneSameInstant(zoneUTC);
        LocalDateTime convertedLocalDateTime = convertedZoneDateTime.toLocalDateTime();

        return convertedLocalDateTime;
    }

    /** Method to convert a ZonedDateTime to a LocalDateTime. */
    public static LocalDateTime toLocalFromZoned(ZonedDateTime zonedDateTime){
        ZonedDateTime localConverted = zonedDateTime.withZoneSameInstant(zoneLocal);
        LocalDateTime returnDateTime = localConverted.toLocalDateTime();
        return returnDateTime;
    }


    //this method is used for input validation when created an appointment to ensure it is within business hours
    //of the Company's Main Office
    /** Method for input validation to ensure appointment time is within business hours. */
    public static boolean isInsideBusinessHours(LocalDateTime localDateTime){
        //LocalTime dayStart = LocalTime.parse("08:00");
        //LocalTime dayEnd = LocalTime.parse("22:00");
        //ZonedDateTime convertedZoneDateTime = localDateTime.atZone(zoneLocal).withZoneSameInstant(zoneEST);
        //LocalDateTime convertedLocalDateTime = convertedZoneDateTime.toLocalDateTime();
        //LocalTime localTime = convertedLocalDateTime.toLocalTime();

        LocalTime localTime = localDateTime.toLocalTime();
        if((localTime.isAfter(estStart()) || localTime.equals(estStart())) && (localTime.isBefore(estEnd()) || localTime.equals(estEnd()))){
            return true;
        } else{
            return false;
        }
    }
    //Returns the main Office start time in the system's default timezone

    /** Method to return the Main office start time in the system's local timezone. */
    public static LocalTime estStart(){
        //ZonedDateTime zonedDateTime = LocalTime.parse("08:00").atDate(LocalDate.now()).atZone(zoneEST);
        LocalTime dayStart = LocalTime.parse("08:00");
        ZonedDateTime zonedDayStart = (dayStart.atDate(LocalDate.now())).atZone(zoneEST).withZoneSameInstant(zoneLocal);
        dayStart = zonedDayStart.toLocalTime();

        return dayStart;
    }
    //Returns the main Office end time in the system's default timezone
    /** Method to return the Main office end time in the system's local timezone. */
    public static LocalTime estEnd(){
        //ZonedDateTime zonedDateTime = LocalTime.parse("22:00").atDate(LocalDate.now()).atZone(zoneEST);
        LocalTime dayEnd = LocalTime.parse("22:00");
        ZonedDateTime zonedDayStart = (dayEnd.atDate(LocalDate.now())).atZone(zoneEST).withZoneSameInstant(zoneLocal);
        dayEnd = zonedDayStart.toLocalTime();

        return dayEnd;
    }

    //Appointment input validation to ensure appointment time does not overlap another appointment
    //with the same customer
    /** Method for input validation to ensure the appointment time does not overlap with another appointment for the same customer. */
    public static boolean isOverlapped(int customerID, LocalDateTime start, LocalDateTime end, int id){

        for(Appointment apt : getAppointmentList()){
            if(apt.getCustomerID() == customerID && apt.getAppointmentID() != id) {
                if ((start.isAfter(apt.getStart()) && start.isBefore(apt.getEnd())) || start.equals(apt.getStart())) {
                    return true;
                } else if ((end.isAfter(apt.getStart()) && end.isBefore(apt.getEnd())) || end.isEqual(apt.getEnd())) {
                    return true;
                }
            }
        }
        return false;
    }
    /** Method to check for upcoming appointments on login.
     * Displays an alert to notify user if they have upcoming appointments or not. */
    public static Alert appointmentReminder(){
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("LLL d, uuuu");
        Alert reminder = new Alert(Alert.AlertType.INFORMATION);
        reminder.setTitle("Welcome " + activeUser.getUsername());
        int i = 0;
        int id;
        String title;
        LocalTime time;
        LocalDate date;
        LocalDateTime soonestAppointment = LocalDateTime.now();
        for(Appointment appointment : getAppointmentList()) {
            if (appointment.getUserID() == activeUser.getUserID()){
                if (appointment.getStart().isBefore(LocalDateTime.now().plusMinutes(15).plusSeconds(30)) && appointment.getStart().isAfter(LocalDateTime.now())) {
                    i++;
                    reminder.setHeaderText("You have " + i + " Appointment(s) in the next 15 minutes");
                    if ( i == 1 || appointment.getStart().isBefore(soonestAppointment)) {
                        soonestAppointment = appointment.getStart();
                        id = appointment.getAppointmentID();
                        time = appointment.getStart().toLocalTime();
                        date = appointment.getStart().toLocalDate();
                        title = appointment.getTitle();
                        reminder.setContentText("Soonest Appointment: #" + id + ", " + title + " starts at " + timeFormatter.format(time) + " on " + dateFormatter.format(date));
                    }
                }
            }
        }
        if(i == 0){
            reminder.setHeaderText("No upcoming Appointments");
            reminder.setContentText("You have no upcoming Appointments within the next 15 minutes.");
        }

        Optional<ButtonType> ok = reminder.showAndWait();
        return reminder;}
}













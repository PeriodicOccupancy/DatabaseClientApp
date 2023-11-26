package Controller;

import Model.Appointment;
import Model.Contact;
import Model.Customer;
import Model.Enums.AccessType;
import Model.Enums.ObjAccessed;
import Model.User;
import helper.Helper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

import static Controller.AppointmentsController.updatingAppointment;
import static Model.Appointment.addAppointment;
import static Model.Appointment.appointmentUpdate;
import static Model.User.activeUser;
import static helper.DatabaseAccessIO.databaseAccessSaveFile;
import static helper.Helper.*;
import static helper.JDBC.connection;

/** This class Controls the GUI for creating an updating appointments.
 @author  Mike Fasnacht, mfasnac@wgu.edu
 @version  1.0, September 26th, 2022
 */
public class CreateAppointmentController implements Initializable {
    public Label createAptTitle;
    public Button cancelAptCreation;
    public Button saveAppointment;
    public TextField idTxt;
    public TextField titleTxt;
    public TextField descriptionTxt;
    public TextField locationTxt;
    public ComboBox<String> contactSelection;
    public TextField typeTxt;
    public DatePicker startDatePicker;
    public DatePicker endDatePicker;
    public TextField startTimeTxt;
    public TextField endTimeTxt;
    public TextField customerIDTxt;
    public TextField userIDTxt;
    public ComboBox<String> userSelection;
    public ComboBox<String> customerSelection;
    DateTimeFormatter aTimeFormatter = DateTimeFormatter.ofPattern("h:mm a");

    /** This method initializes the GUI for creating or updating appointments.
     * The GUI changes depending on the status of the updatingAppointment boolean. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Initializing ComboBox Selections
        fillContactList();
        fillCustomerList();
        fillUserList();

        //if creating a new appointment and not updating, the User text field and comboBox will start with the
        // activeUser object's ID and username.
        if(updatingAppointment) {
            createAptTitle.setText("Update Appointment");
            saveAppointment.setText("Update");

            idTxt.setText(String.valueOf(appointmentUpdate.getAppointmentID()));
            titleTxt.setText(appointmentUpdate.getTitle());
            descriptionTxt.setText(appointmentUpdate.getDescription());
            locationTxt.setText(appointmentUpdate.getLocation());
            contactSelection.getSelectionModel().select(Helper.getContactName(appointmentUpdate.getContactID()));
            typeTxt.setText(appointmentUpdate.getType());
            startDatePicker.setValue(appointmentUpdate.getStart().toLocalDate());
            startTimeTxt.setText(appointmentUpdate.getStart().toLocalTime().format(aTimeFormatter));
            endDatePicker.setValue(appointmentUpdate.getEnd().toLocalDate());
            endTimeTxt.setText(appointmentUpdate.getEnd().toLocalTime().format(aTimeFormatter));
            customerIDTxt.setText(String.valueOf(appointmentUpdate.getCustomerID()));
            customerSelection.getSelectionModel().select(Helper.getCustomerName(appointmentUpdate.getCustomerID()));
            userIDTxt.setText(String.valueOf(appointmentUpdate.getUserID()));
            userSelection.getSelectionModel().select(Helper.getUserName(appointmentUpdate.getUserID()));
        } else{
            createAptTitle.setText("Create Appointment");
            saveAppointment.setText("Save");

            userSelection.getSelectionModel().select(activeUser.getUsername());
            userIDTxt.setText(String.valueOf(activeUser.getUserID()));
        }
    }

    //If creating takes the current entries in all fields and creates a new Appointment Object in the program
    // and also in the database, if any fields are entered correctly, does nothing and shows an Alert
    // If updating verifies valid data entry before modifying the appointment object and pushing changes to
    // the database
    /** Saves changes to either create or update an appointment.
     * If creating takes the current entries in all fields and creates a new Appointment Object in the program
     * and also in the database, if any fields are entered correctly, does nothing and shows an Alert
     * If updating verifies valid data entry before modifying the appointment object and pushing changes to
     * the database*/
    public void saveAppointmentBtn(ActionEvent actionEvent) throws SQLException, IOException {
        int appointmentID = -1;
        int customerID;
        int userID;
        int contactID;
        String title;
        String description;
        String location;
        String type;
        LocalDateTime start;
        LocalDateTime end;
        LocalDateTime createDate;
        String createdBy;
        LocalDateTime lastUpdate;
        String lastUpdateBy;
        try {
            customerID = Integer.parseInt(customerIDTxt.getText());
            userID = Integer.parseInt(userIDTxt.getText());
            contactID = Helper.getContactID(contactSelection.getSelectionModel().getSelectedItem());
            Exception InvalidContact = new Exception();
            if(contactID == -1)
                throw InvalidContact;
            title = titleTxt.getText();
            description = descriptionTxt.getText();
            location = locationTxt.getText();
            type = typeTxt.getText();
            start = startDatePicker.getValue().atTime(LocalTime.parse(startTimeTxt.getText(), aTimeFormatter));
            end = endDatePicker.getValue().atTime(LocalTime.parse(endTimeTxt.getText(), aTimeFormatter));
            if(updatingAppointment) {
                createDate = appointmentUpdate.getCreateDate();
                createdBy = appointmentUpdate.getCreatedBy();
                appointmentID = Integer.parseInt(idTxt.getText());
            }else{
                createDate = LocalDateTime.now();
                createdBy = activeUser.getUsername();
            }
            lastUpdate = LocalDateTime.now();
            lastUpdateBy = activeUser.getUsername();
        } catch (Exception e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Data Entry");
            alert.setHeaderText("Invalid Formatting");
            alert.setContentText("Please ensure all Fields have inputs, all ID fields are integers, and all time fields are in 'h:mm a' format. ex: '7:00 AM' or '12:00 PM' ");
            Optional<ButtonType> result = alert.showAndWait();
            return;
        }

        if(!isInsideBusinessHours(start) || !isInsideBusinessHours(end)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Appointment Time");
            alert.setHeaderText("Appointment time not within Main Office business hours");
            alert.setContentText("Please choose a start and end time between " + aTimeFormatter.format(estStart()) + " and " + aTimeFormatter.format(estEnd()) + ".");
            Optional<ButtonType> result = alert.showAndWait();
            return;
        }

        if (isOverlapped(customerID, start, end, appointmentID)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Overlapped Appointment");
            alert.setHeaderText("Appointment time & customer conflict.");
            alert.setContentText("Appointment overlaps with an existing appointment for the same Customer ID, please change the associated customer, or the start & end times to resolve conflict.");
            Optional<ButtonType> result = alert.showAndWait();
            return;
        }

        if(updatingAppointment){
            appointmentID = appointmentUpdate.getAppointmentID();
            if(appointmentID == -1){
                System.out.println("Something went wrong Appointment is null or has -1 ID");
                return;
            }
            Statement statement = connection.createStatement();
            int rowsAffected =
                    statement.executeUpdate("UPDATE client_schedule.appointments " +
                    "SET Title = '" + title + "', Description = '" + description + "', Location = '" + location + "', Type = '" + type + "', Start = '" + Timestamp.valueOf(toUTC(start)) + "', End = '" + Timestamp.valueOf(toUTC(end))
                            + "', Last_Update = '" + Timestamp.valueOf(toUTC(lastUpdate)) + "', Last_Updated_By = '" + lastUpdateBy + "' " + "WHERE Appointment_ID = " + appointmentID + ";");
            System.out.println(rowsAffected + " Rows affected");
            databaseAccessSaveFile(activeUser, AccessType.UPDATE, ObjAccessed.Appointment,appointmentUpdate.getAppointmentID());
            appointmentUpdate.UpdateAppointment(customerID,userID,contactID,title,description,location,type,start,end,lastUpdate,lastUpdateBy);

        } else {

            Statement statement = connection.createStatement();
            int rowsAffected =
            statement.executeUpdate("INSERT INTO client_schedule.appointments (Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES ('" +
                    title + "', '" + description + "', '" + location + "', '" + type + "', '" + Timestamp.valueOf(toUTC(start)) + "', '" + Timestamp.valueOf(toUTC(end)) + "', '" + Timestamp.valueOf(toUTC(createDate)) + "', '"+ createdBy
                    + "', '" + Timestamp.valueOf(toUTC(lastUpdate)) + "', '" + lastUpdateBy + "', " + customerID + ", " + userID + ", " + contactID + ");");
            System.out.println(rowsAffected + " Rows affected");
            ResultSet results =
                    statement.executeQuery("SELECT Appointment_ID FROM client_schedule.appointments WHERE Title = '" + title + "' AND Start = '" + Timestamp.valueOf(toUTC(start)) + "';");
            //System.out.println(results);
            while (results.next()){
                System.out.println(results);
                appointmentID = results.getInt(1);
                break;
            }
            if(appointmentID == -1){
                System.out.println("Something went wrong Appointment_ID is set to -1");
                return;
            }
            Appointment appointment = new Appointment(appointmentID,customerID,userID,contactID,title,description,location,type,start,end,createDate,createdBy,lastUpdate,lastUpdateBy);
            databaseAccessSaveFile(activeUser, AccessType.INSERT,ObjAccessed.Appointment,appointmentID);
            addAppointment(appointment);
            statement.close();
            results.close();
        }

        updatingAppointment = false;
        appointmentUpdate = null;

        Parent root = FXMLLoader.load(getClass().getResource("/View/Appointments.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Appointments");
        stage.setScene(scene);
        stage.show();
    }

    /** Method to cancel current create or update operation.
     * Simply shows a dynamic alert consistent with updatingAppointment to confirm cancellation and
     * prevent loss of data entry accidentally. also resets the boolean and Appointment object pointer. */
    public void cancelAptCreationBtn(ActionEvent actionEvent) throws IOException {
        //code to alter Alert text based on the updatingAppointment boolean
        String statement;
        if (updatingAppointment){
            statement = "updating appointment";
        } else{
            statement = "appointment creation";
        }

        //alert to confirm cancellation, to prevent accidental lost progress on update/creation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you wish to cancel " + statement + "? Changes will not be saved.");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {

            //resetting boolean and static class used for tracking update/creation status
            updatingAppointment = false;
            appointmentUpdate = null;

            Parent root = FXMLLoader.load(getClass().getResource("/View/Appointments.fxml"));
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Appointments");
            stage.setScene(scene);
            stage.show();
        }
    }



    /** Method tied to Customer combo box selection.
     * this onAction method sets the text in the connected textField to the ID of the selected object in
     * the comboBox. */
    public void onActionCustomerSelection(ActionEvent actionEvent) {
        String selCustomer = customerSelection.getSelectionModel().getSelectedItem();
        for (Customer customer : Customer.getCustomerList()){
            if(selCustomer.equals(customer.getCustomerName())){
                customerIDTxt.setText(String.valueOf(customer.getCustomerID()));
                break;
            }
        }
    }
    /** Method tied to the User combo box selection.
     * this onAction method sets the text in the connected textField to the ID of the selected object in
     * the comboBox. */
    public void onActionUserSelection(ActionEvent actionEvent) {
        String selUser = userSelection.getSelectionModel().getSelectedItem();
        for (User user : User.getUserList()){
            if (selUser.equals(user.getUsername())){
                userIDTxt.setText(String.valueOf(user.getUserID()));
                break;
            }
        }
    }

    /** Method to fill the Contact String list.
     * This fill method populates the ComboBox selection*/
    private void fillContactList(){
        ObservableList<String> contacts = FXCollections.observableArrayList();
        for(Contact contact: Contact.getContactList()) {
            contacts.add(contact.getContactName());
                }
        contactSelection.setItems(contacts);
    }

    /** Method to fill the Customer String List.
     * This fill method populates the ComboBox selection. */
    private void fillCustomerList(){
        ObservableList<String> customers = FXCollections.observableArrayList();
        for (Customer customer : Customer.getCustomerList()){
            customers.add(customer.getCustomerName());
        }
        customerSelection.setItems(customers);
    }

    /** Method to fill the User String List.
     * This fill method populates the ComboBox selection. */
    private  void fillUserList(){
        ObservableList<String> users = FXCollections.observableArrayList();
        for (User user : User.getUserList()){
            users.add(user.getUsername());
        }
        userSelection.setItems(users);
    }
}

package Controller;

import Model.Appointment;
import Model.Contact;
import Model.DatabaseAccess;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.time.Month;
import java.util.ResourceBundle;

import static Model.Appointment.getAppointmentList;
import static Model.Appointment.getTypeList;
import static Model.Contact.getContactList;
import static Model.DatabaseAccess.getAccessList;
import static Model.User.getUserList;

/** Class to control the GUI for the reports page.
 @author  Mike Fasnacht, mfasnac@wgu.edu
 @version  1.0, September 26th, 2022
 */
public class ReportsController implements Initializable {

    //Appointment Reports Tab
    public Tab appointmentTab;
    public ComboBox<String> aptTypeSelection;
    public ComboBox<Month> aptMonthSelection;
    public RadioButton radioAllAppointments;
    public RadioButton radioAptMonthType;
    public RadioButton radioAptAllType;
    public Label totalAppointmentsTxt;
    public TableView<Appointment> totalAppointmentsTable;
    public TableColumn colAptID;
    public TableColumn colAptTitle;
    public TableColumn colAptType;
    public TableColumn colAptDescription;
    public TableColumn colAptStart;
    public TableColumn colAptCustomer;
    public TableColumn colAptUser;
    public TableColumn colAptContact;
    //Contact Reports Tab
    public Tab contactsTab;
    public ComboBox<Contact> contactSelection;
    public Label contactSelectionPrompt;
    public TableView<Appointment> contactScheduleTable;
    public TableColumn colContactAptID;
    public TableColumn colContactAptTitle;
    public TableColumn colContactAptType;
    public TableColumn colContactAptDescription;
    public TableColumn colContactAptStart;
    public TableColumn colContactAptEnd;
    public TableColumn colContactAptCustID;
    //Database Reports Tab
    public Tab databaseAccessTab;
    public ComboBox<User> userSelection;
    public TableView<DatabaseAccess> dataAccessTable;
    public TableColumn colUser;
    public TableColumn colAccessType;
    public TableColumn colObjManip;
    public TableColumn colDate;
    public TableColumn colTime;
    public TableColumn colObjID;

    public Button returnBtn;

    ObservableList<Contact> contactSelectionList = FXCollections.observableArrayList();
    ObservableList<Appointment> filteredAppointments = FXCollections.observableArrayList();
    ObservableList<Appointment> contactsAppointments = FXCollections.observableArrayList();
    ObservableList<DatabaseAccess> userAccessesList = FXCollections.observableArrayList();
    ObservableList<User> reportsUserList = FXCollections.observableArrayList();

    /** Initialize method for the ReportsController.
     * Assigns objects and data values to the tables initially on page load. and populates combo boxes.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        totalAppointmentsTable.setItems(getAppointmentList());
        colAptID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        colAptTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAptType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colAptDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colAptStart.setCellValueFactory(new PropertyValueFactory<>("startString"));
        colAptCustomer.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        colAptUser.setCellValueFactory(new PropertyValueFactory<>("userID"));
        colAptContact.setCellValueFactory(new PropertyValueFactory<>("contactID"));

        contactScheduleTable.setItems(contactsAppointments);
        colContactAptID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        colContactAptCustID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        colContactAptTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colContactAptType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colContactAptDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colContactAptStart.setCellValueFactory(new PropertyValueFactory<>("startString"));
        colContactAptEnd.setCellValueFactory(new PropertyValueFactory<>("endString"));
        colContactAptCustID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        colContactAptStart.setSortType(TableColumn.SortType.ASCENDING);

        dataAccessTable.setItems(getAccessList());
        colUser.setCellValueFactory(new PropertyValueFactory<>("username"));
        colAccessType.setCellValueFactory(new PropertyValueFactory<>("accessType"));
        colObjManip.setCellValueFactory(new PropertyValueFactory<>("objManipulated"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colObjID.setCellValueFactory(new PropertyValueFactory<>("objID"));

        reportsUserList.add(null);
        for(User user : getUserList()){
            reportsUserList.add(user);
        }

        contactSelectionList.add(null);
        for(Contact contact : getContactList()){
            contactSelectionList.add(contact);
        }

        aptTypeSelection.setItems(getTypeList());
        aptMonthSelection.setItems(monthList());
        contactSelection.setItems(contactSelectionList);
        userSelection.setItems(reportsUserList);

        contactSelection.setConverter(contactConverter);
        userSelection.setConverter(userStringConverter);

        onActionAllAppointments(new ActionEvent());

    }
/*
Defunct method, no longer sorting appointment reports by year, only by month and type
    public ObservableList<Year> yearList(){
        Year addYear = Year.now();
        ObservableList<Year> years = FXCollections.observableArrayList();
        for(int i = -5; years.size() < 11; i++){
            if (years.size() < 6) {
                years.add(addYear.plusYears(i));
            }else {
                years.add(addYear.plusYears(i));
            }
        }
        System.out.println(years);
        return years;
    }

 */
    /** Method to initialize an observable list of months.
     * returns a list of months in the year, starting with null, to allow for deselection in the combo box.
     * @return ObservableList of Months. */
    public ObservableList<Month> monthList(){
        ObservableList<Month> months = FXCollections.observableArrayList();
        months.add(null);
        for(Month month = Month.JANUARY; months.size() < 12 ; month = month.plus(1))
            months.add(month);
        //System.out.println(months);
        return months;
    }

    //
    //String Converter for the contactSelection ComboBox

    /** String Converter for Contacts.
     * Uses a lambda function for fromString to make the code much cleaner. */
    StringConverter<Contact> contactConverter = new StringConverter<>() {
        /** Override method to convert Contacts to Strings. */
        @Override
        public String toString(Contact contact) {
            return contact.getContactName();
        }
        /** Override method to convert strings to Contacts.
         * The lambda offers flexibility and simplify the code block. */
        @Override
        public Contact fromString(String string) {
            return contactSelection.getItems().stream().filter(contactName ->
                    contactName.getContactName().equals(string)).findFirst().orElse(null);
        }
    };

    //
    //String converter for the userSelection ComboBox
    /** String Converter for Users.
     * Uses a lambda function for fromString to make the code much cleaner. */
    StringConverter<User> userStringConverter = new StringConverter<User>() {
        /** Override method to convert Users to Strings. */
        @Override
        public String toString(User user) {
            return user.getUsername();
        }
        /** Override method to convert strings to Users.
         * The lambda offers flexibility and simplify the code block. */
        @Override
        public User fromString(String s) {
            return userSelection.getItems().stream().filter(username ->
                    username.getUsername().equals(s)).findFirst().orElse(null);
        }
    };

    /** Method to initialize the Appointment report tab when selected. */
    public void onSelectionAppointmentReport(Event event) {
        if(appointmentTab.isSelected())
            onActionAllAppointments(new ActionEvent());
    }

    /** Method to initialize the Contact report tab when selected. */
    public void onSelectionContactReport(Event event) {
        if(contactsTab.isSelected())
            contactSelection.getSelectionModel().clearSelection();
    }

    /** Method to initialize the Database Access report tab when selected.*/
    public void onSelectionDatabaseReport(Event event) {
        if(databaseAccessTab.isSelected())
            userSelection.getSelectionModel().clearSelection();
    }

    /** Method to return to the Directory page GUI. */
    public void onActionReturn(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/Directory.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Directory");
        stage.setScene(scene);
        stage.show();
    }

    /*
    *
    *
    *       Appointment Tab Functions
     */
    //  Radio Buttons
    /** Radio Button method filter the appointment table to all appointments. */
    public void onActionAllAppointments(ActionEvent actionEvent) {
        radioAllAppointments.selectedProperty().setValue(true);
        radioAptMonthType.selectedProperty().setValue(false);
        radioAptAllType.selectedProperty().setValue(false);
        aptTypeSelection.getSelectionModel().clearSelection();
        aptMonthSelection.getSelectionModel().clearSelection();
        aptTypeSelection.setVisible(false);
        aptMonthSelection.setVisible(false);
        totalAppointmentsTable.setItems(getAppointmentList());
        totalAppointmentsTxt.setText("Total Appointments: " + getAppointmentList().size());
    }

    /** Radio button method to allow filtering the appointment table by type. */
    public void onActionAptAllType(ActionEvent actionEvent) {
        radioAptMonthType.selectedProperty().setValue(false);
        radioAllAppointments.selectedProperty().setValue(false);
        aptTypeSelection.getSelectionModel().clearSelection();
        aptMonthSelection.getSelectionModel().clearSelection();
        aptTypeSelection.setVisible(true);
        aptMonthSelection.setVisible(false);
        aptTypeSelection.setPromptText("Select Type");
        totalAppointmentsTable.setItems(getAppointmentList());
        totalAppointmentsTxt.setText("Total Appointments: " + getAppointmentList().size());
    }

    /** Radio button method to allow filtering the appointment table by type and month. */
    public void onActionAptMonthType(ActionEvent actionEvent) {
        radioAllAppointments.selectedProperty().setValue(false);
        radioAptAllType.selectedProperty().setValue(false);
        aptTypeSelection.getSelectionModel().clearSelection();
        aptMonthSelection.getSelectionModel().clearSelection();
        aptTypeSelection.setVisible(true);
        aptMonthSelection.setVisible(true);
        totalAppointmentsTable.setItems(getAppointmentList());
        totalAppointmentsTxt.setText("Total Appointments: " + getAppointmentList().size());
    }

    //Combo Boxes
    /** Method to call typeTableInitializer() on combo box selection. */
    public void onSelectionType(ActionEvent actionEvent) {
        typeTableInitializer();
    }

    /** Method to call typeTableInitializer() on combo box selection. */
    public void onSelectionMonth(ActionEvent actionEvent) {
        typeTableInitializer();
    }

    /** Method to filter the appointment table based on the month and type combo box selections. */
    public void typeTableInitializer(){
        filteredAppointments.clear();
        Month selMonth = aptMonthSelection.getSelectionModel().getSelectedItem();
        String selType = aptTypeSelection.getSelectionModel().getSelectedItem();
        if(selType == null && selMonth == null){ //both null
            totalAppointmentsTxt.setText("Total Appointments: " + getAppointmentList().size());
            totalAppointmentsTable.setItems(getAppointmentList());
        }else if(selMonth == null){ //only type
            for(Appointment appointment : getAppointmentList()){
                if(appointment.getType().equals(selType)){
                    filteredAppointments.add(appointment);
                }
            }
            totalAppointmentsTxt.setText("Total Appointments of Type " + selType + ": " + filteredAppointments.size());
            totalAppointmentsTable.setItems(filteredAppointments);
        } else if(selType != null){ //both selected
            for(Appointment appointment : getAppointmentList()){
                if(appointment.getType().equals(selType) && appointment.getStart().getMonth().equals(selMonth)){
                    filteredAppointments.add(appointment);
                }
            }
            totalAppointmentsTxt.setText("Total Appointments in " + selMonth + " of Type " + selType + ": " + filteredAppointments.size());
            totalAppointmentsTable.setItems(filteredAppointments);
        } else{ //only month
            for(Appointment appointment : getAppointmentList()){
                if(appointment.getStart().getMonth().equals(selMonth)){
                    filteredAppointments.add(appointment);
                }
            }
            totalAppointmentsTxt.setText("Total Appointments in " + selMonth + ": " + filteredAppointments.size());
            totalAppointmentsTable.setItems(filteredAppointments);
        }
    }

    /*
    *
    *
    *       Contact Tab Functions
     */
    /** Method to filter the appointment schedule table based on Contact. */
    public void onSelectionContacts(ActionEvent actionEvent) {
        contactsAppointments.clear();
        if(contactSelection.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        int contactID = contactSelection.getSelectionModel().getSelectedItem().getContactID();
        for(Appointment appointment : getAppointmentList()){
            if(contactID == appointment.getContactID())
                contactsAppointments.add(appointment);
        }
        contactScheduleTable.setItems(contactsAppointments);
    }

    /*
    *
    *
    *       Database Tab Functions
     */
    /** Method to filter the Database Access record table based on User. */
    public void onSelectionUser(ActionEvent actionEvent) {
        if(userSelection.getSelectionModel().getSelectedItem() == null){
            dataAccessTable.setItems(getAccessList());
        }else {
            userAccessesList.clear();
            int userID = userSelection.getSelectionModel().getSelectedItem().getUserID();
            for(DatabaseAccess dba : getAccessList()) {
                if (dba.getUserID() == userID) {
                    userAccessesList.add(dba);
                }
            }
            dataAccessTable.setItems(userAccessesList);
        }
    }
}

package Controller;

import Model.Appointment;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

import static Model.Appointment.*;
import static helper.AppointmentCRUD.deleteAppointmentFromDB;

/** This class controls the appointments.FXML GUI.
 * It declares all the GUI objects in the FXML and controls them.
 @author  Mike Fasnacht, mfasnac@wgu.edu
 @version  1.0, September 26th, 2022
 */

public class AppointmentsController implements Initializable {
    public TableColumn AptID;
    public TableColumn UserID;
    public TableColumn CustomerID;
    public TableColumn Title;
    public TableColumn Description;
    public TableColumn Location;
    public TableColumn Type;
    public TableColumn Start;
    public TableColumn End;
    public TableView<Appointment> aptTable;
    public Tab weeklyView;
    public Tab monthlyCase;
    public Tab allView;

/** This initializes() method sets the items to be displayed in the appointment Table. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        aptTable.setItems(getAppointmentList());
        AptID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        CustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        UserID.setCellValueFactory(new PropertyValueFactory<>("userID"));
        Title.setCellValueFactory(new PropertyValueFactory<>("title"));
        Description.setCellValueFactory(new PropertyValueFactory<>("description"));
        Location.setCellValueFactory(new PropertyValueFactory<>("location"));
        Type.setCellValueFactory(new PropertyValueFactory<>("type"));
        Start.setCellValueFactory(new PropertyValueFactory<>("startString"));
        End.setCellValueFactory(new PropertyValueFactory<>("endString"));

        setAppointmentView();

    }

    /** The returnBtn() method returns the user to the Directory GUI page.
     * It's tied to the return button within the GUI
     * @param  actionEvent the event that triggers  this method is when the return button is pressed. */
    public void returnBtn(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/Directory.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Directory");
        stage.setScene(scene);
        stage.show();
    }

    /** The deleteBtn() method deletes the selected appointment.
     * It checks if an appointment is selected and displays an alert if no appointment is selected.
     * it calls a separate method to delete the appointment from the list and from the database, displays a
     * confirmation alert to delete, and if deleted shows a message displaying data of the deleted appointment, and
     * confirming its deletion.
     * @param  actionEvent this event triggers when the delete button is pressed. */
    public void deleteBtn(ActionEvent actionEvent) throws SQLException, IOException {
        if(aptTable.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Appointment selected.");
            alert.setHeaderText("No Appointment selected.");
            alert.setContentText("Please select an appointment before attempting to delete.");
            Optional<ButtonType> result = alert.showAndWait();
            return;
        }
        Appointment selectedAppointment = aptTable.getSelectionModel().getSelectedItem();

        int id = selectedAppointment.getAppointmentID();
        String title = selectedAppointment.getTitle();
        String type = selectedAppointment.getType();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you wish to delete the selected appointment? This will delete it from the database.");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK){
            int rowsAffected = deleteAppointmentFromDB(selectedAppointment);
            System.out.println(rowsAffected + " Rows Affected");

            Appointment.deleteFromAllAppointmentLists(selectedAppointment);
            resetListView();
            Alert deletionSuccessful = new Alert(Alert.AlertType.INFORMATION);
            deletionSuccessful.setHeaderText("Appointment deleted successfully");
            deletionSuccessful.setContentText("Appointment " + title + ", #" + id + " deleted, of Type: " + type + ".");
            Optional<ButtonType> acknowledge = deletionSuccessful.showAndWait();
        }

    }

    //static boolean for tracking if creating or updating an appointment in the CreateAppointment Controller
    static boolean updatingAppointment;

    /** The createBtn() method takes the user to the CreateAppointment GUI.
     * The method sets a boolean for tracking update/create status to false, and initializes the create
     * appointment page.
     * @param  actionEvent This event trigger when the create button is pressed. */
    public void createBtn(ActionEvent actionEvent) throws IOException {
        updatingAppointment = false;
        Parent root = FXMLLoader.load(getClass().getResource("/View/CreateAppointment.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Create Appointment");
        stage.setScene(scene);
        stage.show();
    }

    /** The updateBtn() method takes you to the Create Appointment GUI.
     * The method checks to see if an appointment is selected and displays an alert if not. If so it sets the
     * boolean for tracking appointment update/creation status to true, assigns a pointer to the selected
     * object and initializes the GUI for Updating appointments.
     * @param actionEvent This even triggers when the update button is pressed. */
    public void updateBtn(ActionEvent actionEvent) throws IOException {
        if(aptTable.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("No Appointment selected.");
            alert.setContentText("Please select an appointment before attempting to update.");
            Optional<ButtonType> result = alert.showAndWait();
            return;
        }

        updatingAppointment = true;
        appointmentUpdate = aptTable.getSelectionModel().getSelectedItem();

        Parent root = FXMLLoader.load(getClass().getResource("/View/CreateAppointment.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Update Appointment");
        stage.setScene(scene);
        stage.show();
    }


    /** This method initializes the appointment Lists for weekly and Monthly view.
     * It goes through all appointments and adds any that are within the next 7 days to the weekly list,
     * and any that are within the same month and year as now(). This method is called when the Appointments
     * GUI is initialized. */
    public  void setAppointmentView(){
        LocalDateTime now = LocalDateTime.now();

        for(Appointment appointment : getAppointmentList()){
            LocalDateTime start = appointment.getStart();

            if(start.isBefore(now.plusDays(7)) && start.isAfter(now)){
                if(!getWeeklyAppointmentList().contains(appointment))
                    addWeeklyAppointment(appointment);
            }
            if(start.getMonth().equals(now.getMonth()) && start.getYear() == now.getYear()){
                if(!getMonthlyAppointmentList().contains(appointment))
                    addMonthlyAppointment(appointment);
            }
        }
    }
    /** This method filters the tableview to show all Appointments. */
    public void allOnAction(Event event) {
        if(allView.isSelected()) {
            aptTable.setItems(getAppointmentList());
            System.out.println("All");
        }
    }

    /** This method filters the tableview to show only appointments within the next 7 days. */
    public void weeklyOnAction(Event event) {
        if(weeklyView.isSelected()) {

            aptTable.setItems(getWeeklyAppointmentList());
            System.out.println("Weekly");
        }
    }

    /** This method filters the tableview to show only appointments within the current month. */
    public void monthlyOnAction(Event event) {
        if(monthlyCase.isSelected()) {
            aptTable.setItems(getMonthlyAppointmentList());
            System.out.println("Monthly");
        }
    }


    /** This method resets the table view depending on the currently selected tab in the GUI. */
    public void resetListView() {
        if(allView.isSelected()) {
            aptTable.setItems(getAppointmentList());
            System.out.println("All");
        }
        if(weeklyView.isSelected()) {

            aptTable.setItems(getWeeklyAppointmentList());
            System.out.println("Weekly");
        }
        if(monthlyCase.isSelected()) {
            aptTable.setItems(getMonthlyAppointmentList());
            System.out.println("Monthly");
        }


    }


}

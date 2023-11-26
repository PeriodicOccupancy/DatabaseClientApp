package Controller;

import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import static Model.User.activeUser;
/** This is the Directory Controller.
 It controls the GUI for the Directory page.
 @author  Mike Fasnacht, mfasnac@wgu.edu
 @version  1.0, September 26th, 2022
 */
public class DirectoryController implements Initializable {

    public Label welcomeTxt;
    public Button AppointmentsBtn;
    public Button CustomersBtn;

    /** Method to initialize the Directory GUI.
     * Changes a label to display a welcome message to the current activeUser.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        welcomeTxt.setText("Welcome " + activeUser.getUsername());

    }

    /** Method to bring the user to the Appointment view GUI.
     * Brings the user to the Appointment page to view and manipulate existing appointments in the database. */
    public void appointmentsBtn(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/Appointments.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Appointments");
        stage.setScene(scene);
        stage.show();
    }

    /** Method to Initialize the Customers Page.
     * Brings the user to the Customer page to view and manipulate existing customer information.*/
    public void customersBtn(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/Customers.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Customers");
        stage.setScene(scene);
        stage.show();
    }

    /** Method to log the active user out.
     * Empties the tracked activeUser information and brings the user back to the login page
     * to prompt for authentication.*/
    public void logOutBtn(ActionEvent actionEvent) throws IOException {
        activeUser = new User();
        ResourceBundle rb = ResourceBundle.getBundle("Model/Nat", Locale.getDefault());
        Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        try {
            stage.setTitle(rb.getString("login"));
        } catch(MissingResourceException e){
            stage.setTitle("Login");
        }
        stage.setScene(new Scene(root));
        stage.show();
    }

    /** Method to initialize the GUI for viewing reports. */
    public void onActionReports(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/Reports.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Reports");
        stage.setScene(scene);
        stage.show();
    }
}

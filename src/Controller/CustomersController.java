package Controller;

import Model.Customer;
import helper.CustomerCRUD;
import javafx.event.ActionEvent;
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
import java.util.Optional;
import java.util.ResourceBundle;

import static Model.Customer.deleteCustomer;
import static Model.Customer.getCustomerList;

/** This class controls the customer view GUI.
 * It allows the user to view, create, select and update or delete appointments.
 @author  Mike Fasnacht, mfasnac@wgu.edu
 @version  1.0, September 26th, 2022
 */
public class CustomersController implements Initializable {


    public TableView<Customer> customerTable;
    public TableColumn id;
    public TableColumn name;
    public TableColumn address;
    public TableColumn postalCode;
    public TableColumn phone;
    public TableColumn divisionID;
    public TableColumn divisionName;


    /** The initialize method for the Customer view GUI. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerTable.setItems(getCustomerList());
        id.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        name.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        divisionID.setCellValueFactory(new PropertyValueFactory<>("divisionID"));
        divisionName.setCellValueFactory(new PropertyValueFactory<>("customerDivisionName"));
    }

    /** Return method for returning to the Directory View. */
    public void returnBtn(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/Directory.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Directory");
        stage.setScene(scene);
        stage.show();
    }

    public static boolean updatingCustomer = false;

    /** Method for creating a new customer object. */
    public void onActionCreate(ActionEvent actionEvent) throws IOException {
        updatingCustomer = false;
        Parent root = FXMLLoader.load(getClass().getResource("/View/CreateCustomer.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Create Customer");
        stage.setScene(scene);
        stage.show();
    }

    /** Method for updating a selected customer object. */
    public void onActionUpdate(ActionEvent actionEvent) throws IOException {
        if(customerTable.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("No Customer selected.");
            alert.setContentText("Please select a Customer before attempting to update.");
            Optional<ButtonType> result = alert.showAndWait();
            return;
        }
        updatingCustomer = true;
        Customer.customerUpdate = customerTable.getSelectionModel().getSelectedItem();
        Parent root = FXMLLoader.load(getClass().getResource("/View/CreateCustomer.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Update Customer");
        stage.setScene(scene);
        stage.show();
    }

    /** Method for deleting a selected customer object. */
    public void onActionDelete(ActionEvent actionEvent) throws SQLException, IOException {
        if(customerTable.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("No Customer selected.");
            alert.setContentText("Please select a Customer before attempting to delete.");
            Optional<ButtonType> result = alert.showAndWait();
            return;
        }
        Customer deleteCustomer = customerTable.getSelectionModel().getSelectedItem();
        String name = deleteCustomer.getCustomerName();
        int id = deleteCustomer.getCustomerID();
        if(CustomerCRUD.sqlDeleteCustomer(deleteCustomer) == 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Customer not found");
            alert.setHeaderText("Cannot delete customer");
            alert.setContentText("Customer could not be found in database, and therefore could not be deleted.");
            Optional<ButtonType> result = alert.showAndWait();
            return;
        }
        if(CustomerCRUD.sqlDeleteCustomer(deleteCustomer) == -1){
            System.out.println("Customer not Deleted due to matching Appointments found.");
            return;
        }
        deleteCustomer(deleteCustomer);
        System.out.println("Customer Deletion Successful.");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successful Delete Operation");
        alert.setHeaderText("Customer record deleted successfully");
        alert.setContentText("Customer " + name + " with ID of " + id + " has been successfully deleted.");
        Optional<ButtonType> result = alert.showAndWait();

    }
}

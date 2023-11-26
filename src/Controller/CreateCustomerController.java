package Controller;

import Model.Country;
import Model.Customer;
import Model.FirstLevelDivision;
import helper.CustomerCRUD;
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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

import static Controller.CustomersController.updatingCustomer;
import static Model.Country.getCountryList;
import static Model.Customer.customerUpdate;
import static Model.FirstLevelDivision.getDivisionList;
import static Model.User.activeUser;
import static helper.Helper.*;

/** Controller for the create/update Customer GUI.
 @author  Mike Fasnacht, mfasnac@wgu.edu
 @version  1.0, September 26th, 2022
 */
public class CreateCustomerController implements Initializable {
    public Label titleTxt;
    public TextField idTxt;
    public TextField nameTxt;
    public ComboBox<String> countrySelection;
    public ComboBox<String> divisionSelection;
    public TextField addressTxt;
    public TextField postalCodeTxt;
    public TextField phoneTxt;
    public Button cancelBtn;
    public Button createBtn;
    ObservableList<String> usDivisionList = FXCollections.observableArrayList();
    ObservableList<String> ukDivisionList = FXCollections.observableArrayList();
    ObservableList<String> canadaDivisionList = FXCollections.observableArrayList();

    /** Initialize method for the create/update Customer GUI. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(updatingCustomer){
            titleTxt.setText("Update Customer");
            createBtn.setText("Update");
            idTxt.setText(String.valueOf(customerUpdate.getCustomerID()));
            nameTxt.setText(customerUpdate.getCustomerName());
            countrySelection.getSelectionModel().select(getCountryName(customerUpdate.getDivisionID()));
            divisionSelection.getSelectionModel().select(getDivisionName(customerUpdate.getDivisionID()));
            addressTxt.setText(customerUpdate.getAddress());
            postalCodeTxt.setText(customerUpdate.getPostalCode());
            phoneTxt.setText(customerUpdate.getPhoneNum());
        } else {
            titleTxt.setText("Create Customer");
            createBtn.setText("Create");
        }
        fillCountryBox();
        fillDivisionLists();
    }

    /** Method to cancel Customer creation/updating.
     * Displays a confirmation alert with text depending on update/create status. */
    public void onActionCancel(ActionEvent actionEvent) throws IOException {
        String statement;
        String header;
        if (updatingCustomer){
            statement = "updating customer";
            header = "Cancel Update?";
        } else{
            statement = "customer creation";
            header = "Cancel Creation?";
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(header);
        alert.setContentText("Are you sure you wish to cancel " + statement + "? Changes will not be saved.");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {

            //resetting boolean and static class used for tracking update/creation status
            updatingCustomer = false;
            customerUpdate = null;

            Parent root = FXMLLoader.load(getClass().getResource("/View/Customers.fxml"));
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Customers");
            stage.setScene(scene);
            stage.show();
        }
    }

    /** Method to save Customer changes.
     * Method to save changes as a new Customer object or to push inputs to update an existing one. */
    public void onActionSave(ActionEvent actionEvent) throws IOException {
        String name = nameTxt.getText();
        String address = addressTxt.getText();
        String post = postalCodeTxt.getText();
        String phone = phoneTxt.getText();
        Timestamp create = null;
        String createBy = null;
        Timestamp update = Timestamp.valueOf(toUTC(LocalDateTime.now()));
        String updateBy = activeUser.getUsername();
        int division = getDivisionID(divisionSelection.getSelectionModel().getSelectedItem());
        if(updatingCustomer){
            try {
                customerUpdate.UpdateCustomer(division, name, address, post, phone, update, updateBy);
                CustomerCRUD.sqlUpdateCustomer(customerUpdate);
                customerUpdate = null;
                updatingCustomer = false;
                System.out.println("Customer updated successfully.");
            }catch (Exception e){
                System.out.println("There is a problem in Controller/CreateCustomerController method onActionSave() for updating an existing Customer");
                return;
            }

        }else{
            try {
                create = Timestamp.valueOf(toUTC(LocalDateTime.now()));
                createBy = activeUser.getUsername();
                Customer created = new Customer(division, name, address, post, phone, create, createBy, update, updateBy);

                //The following methods add the new customer to the sql database, pull the auto-gen ID for the new
                // customer and set the new Customer object's ID to it, and finally add the customer to the
                // observable list used in the customer view page
                CustomerCRUD.sqlInsertCustomer(created);
                Customer.addCustomer(created);

                System.out.println("Customer created successfully.");
            }catch (Exception e){
                System.out.println("There is a problem in Controller/CreateCustomerController method onActionSave() for creating a new Customer");
                System.out.println(e);
                return;
            }
        }
        Parent root = FXMLLoader.load(getClass().getResource("/View/Customers.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Customers");
        stage.setScene(scene);
        stage.show();
    }


    /** Method to filter the division combo box based on Country Selection. */
    public void onActionCountrySelected(ActionEvent actionEvent) {
        countrySelection.setPromptText("Select Division");
        if(countrySelection.getSelectionModel().getSelectedItem().equals("U.S")){
            divisionSelection.setItems(usDivisionList);
            addressTxt.setPromptText("ex: 123 ABC Street, White Plains");
        }
        if(countrySelection.getSelectionModel().getSelectedItem().equals("UK")){
            divisionSelection.setItems(ukDivisionList);
            addressTxt.setPromptText("ex: 123 ABC Street, Newmarket");
        }
        if(countrySelection.getSelectionModel().getSelectedItem().equals("Canada")){
            divisionSelection.setItems(canadaDivisionList);
            addressTxt.setPromptText("ex: 123 ABC Street, Greenwich, London");
        }
    }

    /** Method to fill the Country Combo Box. */
    public void fillCountryBox(){
        ObservableList<String> countryBox = FXCollections.observableArrayList();
        for(Country country : getCountryList()){
            countryBox.add(country.getCountry());
        }
        countrySelection.setItems(countryBox);
    }

    /** Method to fill the Division combo box. */
    public void fillDivisionLists(){

        for(FirstLevelDivision division : getDivisionList()){
            if (division.getCountryID() == 1)
                usDivisionList.add(division.getDivision());

            if(division.getCountryID() == 2)
                ukDivisionList.add(division.getDivision());

            if(division.getCountryID() == 3)
                canadaDivisionList.add(division.getDivision());
        }
    }

}

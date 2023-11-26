package helper;

import Model.Appointment;
import Model.Customer;
import Model.Enums.AccessType;
import Model.Enums.ObjAccessed;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import static Model.Appointment.getAppointmentList;
import static Model.User.activeUser;
import static helper.DatabaseAccessIO.databaseAccessSaveFile;
import static helper.JDBC.connection;

/** Class to control database accesses to the Customer table.
 @author  Mike Fasnacht, mfasnac@wgu.edu
 @version  1.0, September 26th, 2022
 */
public class CustomerCRUD {

    /** Method to populate the observable customer list from customers in the database when the program launches. */
    public static void populateCustomers() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet results =
                statement.executeQuery("SELECT * FROM client_schedule.customers");

        while (results.next()){
            Customer customer = new Customer(
                    results.getInt("Customer_ID"),
                    results.getInt("Division_ID"),
                    results.getString("Customer_Name"),
                    results.getString("Address"),
                    results.getString("Postal_Code"),
                    results.getString("Phone"),
                    results.getTimestamp("Create_Date"),
                    results.getString("Created_By"),
                    results.getTimestamp("Last_Update"),
                    results.getString("Last_Updated_By")
            );
            Customer.addCustomer(customer);
        }
        statement.close();
        results.close();
    }
    /** Method to Update an existing customer in the sql database. */
    public static int sqlUpdateCustomer(Customer customer) throws SQLException, IOException {
        String update = "UPDATE client_schedule.customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?;";
        PreparedStatement statement = connection.prepareStatement(update);
        statement.setString(1, customer.getCustomerName());
        statement.setString(2,customer.getAddress());
        statement.setString(3,customer.getPostalCode());
        statement.setString(4,customer.getPhoneNum());
        statement.setString(5,customer.getLastUpdate().toString());
        statement.setString(6,customer.getCreatedBy());
        statement.setInt(7,customer.getDivisionID());
        statement.setInt(8,customer.getCustomerID());
        int ra = statement.executeUpdate();
        statement.close();

        databaseAccessSaveFile(activeUser, AccessType.UPDATE, ObjAccessed.Customer,customer.getCustomerID());

        return ra;
    }

    /** Method to create a new customer in the sql database. */
    public static int sqlInsertCustomer(Customer customer) throws SQLException, IOException {
        String insert = "INSERT INTO client_schedule.customers (Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement statement = connection.prepareStatement(insert);
        statement.setString(1,customer.getCustomerName());
        statement.setString(2,customer.getAddress());
        statement.setString(3,customer.getPostalCode());
        statement.setString(4,customer.getPhoneNum());
        statement.setString(5,customer.getCreateDate().toString());
        statement.setString(6,customer.getCreatedBy());
        statement.setString(7,customer.getLastUpdate().toString());
        statement.setString(8,customer.getLastUpdatedBy());
        statement.setInt(9,customer.getDivisionID());
        int ra = statement.executeUpdate();
        statement.close();
        sqlSetID(customer);
        databaseAccessSaveFile(activeUser, AccessType.INSERT, ObjAccessed.Customer,customer.getCustomerID());

        return ra;
    }
    /** Method to get the id for a newly created customer. */
    public static void sqlSetID(Customer customer) throws SQLException {
        String query = "SELECT Customer_ID FROM client_schedule.customers WHERE Customer_Name = ? AND Phone = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, customer.getCustomerName());
        statement.setString(2, customer.getPhoneNum());
        ResultSet results =
                statement.executeQuery();
        while (results.next()){
            customer.setCustomerID(results.getInt(1));
            System.out.println("New Customer ID is: " + results.getInt(1));
        }
        statement.close();
        results.close();
    }
    /** Method to delete a customer from the sql database. */
    public static int sqlDeleteCustomer(Customer customer) throws SQLException, IOException {
        for(Appointment appointment : getAppointmentList()){
            if(appointment.getCustomerID() == customer.getCustomerID()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cannot delete customer record");
                alert.setHeaderText("Matching appointments found.");
                alert.setContentText("Cannot delete a customer record with matching appointments on file, " +
                        "please delete associated appointments before attempting customer deletion.");
                Optional<ButtonType> button = alert.showAndWait();
                return -1;
            }
        }
        String delete = "DELETE FROM client_schedule.customers WHERE Customer_ID = ?;";
        PreparedStatement statement = connection.prepareStatement(delete);
        statement.setInt(1, customer.getCustomerID());
        int ra = statement.executeUpdate();
        statement.close();

        databaseAccessSaveFile(activeUser,AccessType.DELETE,ObjAccessed.Customer,customer.getCustomerID());

        return ra;
    }
}

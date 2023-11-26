package helper;

import Model.Contact;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static helper.JDBC.connection;

/** Class to control database accesses to the Contacts table.
 @author  Mike Fasnacht, mfasnac@wgu.edu
 @version  1.0, September 26th, 2022
 */
public class ContactCRUD {

    /** Method to populate the observable contact list from contacts in the database when the program launches. */
    public static void populateContacts() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet results =
                statement.executeQuery("SELECT * FROM client_schedule.contacts");

        while (results.next()){
            Contact contact = new Contact(
                    results.getInt("Contact_ID"),
                    results.getString("Contact_Name"),
                    results.getString("Email")
            );
            Contact.addContact(contact);
        }
        statement.close();
        results.close();

    }
}

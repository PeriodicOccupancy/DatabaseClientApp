package helper;

import Model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static helper.JDBC.connection;

/** Class to control database accesses to the Division table.
 @author  Mike Fasnacht, mfasnac@wgu.edu
 @version  1.0, September 26th, 2022 */
public class UserCRUD {

    /** Method to populate the observable customer list from users in the database when the program launches. */
    public static void populateUsers() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet results =
                statement.executeQuery("SELECT * FROM client_schedule.users");

        while (results.next()){
            User user = new User(
                    results.getInt("User_ID"),
                    results.getString("User_Name"),
                    results.getString("Password"),
                    results.getTimestamp("Create_Date"),
                    results.getString("Created_By"),
                    results.getTimestamp("Last_Update"),
                    results.getString("Last_Updated_By")
            );
            User.addUser(user);
        }

        statement.close();
        results.close();
    }
}

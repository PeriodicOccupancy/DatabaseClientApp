package helper;

import Model.FirstLevelDivision;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import static helper.JDBC.connection;
/** Class to control database accesses to the Division table.
 @author  Mike Fasnacht, mfasnac@wgu.edu
 @version  1.0, September 26th, 2022
 */
public class DivisionCRUD {

    /** Method to populate the observable customer list from divisions in the database when the program launches. */
    public static void populateDivisions() throws SQLException {
        //ResultSet results = sqlQuery("*", "first_level_divisions");
        Statement statement = connection.createStatement();
        ResultSet results =
                statement.executeQuery("SELECT * FROM client_schedule.first_level_divisions");

        while (results.next()) {
            int divisionID = results.getInt(1);
            String divisionName = results.getString(2);
            int countryID = results.getInt(7);
            Timestamp createDate = results.getTimestamp(3);
            String createdBy = results.getString(4);
            Timestamp lastUpdate = results.getTimestamp(5);
            String lastUpdatedBy = results.getString(6);

            FirstLevelDivision division = new FirstLevelDivision(divisionID, divisionName, countryID, createDate, createdBy, lastUpdate, lastUpdatedBy);
            FirstLevelDivision.addDivision(division);
        }

        statement.close();
        results.close();
    }
}

package helper;

import Model.Country;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import static helper.JDBC.connection;

/** Class to control database accesses to the Country table.
 @author  Mike Fasnacht, mfasnac@wgu.edu
 @version  1.0, September 26th, 2022
 */
public class CountryCRUD {

    /** Method to populate the observable country list from countries in the database when the program launches. */
    public static void populateCountries() throws SQLException {
        //ResultSet results = sqlQuery("*", "countries");
        Statement statement = connection.createStatement();
        ResultSet results =
                statement.executeQuery("SELECT * FROM client_schedule.countries");

        while (results.next()){
            int id = results.getInt(1);
            String countryName = results.getString(2);
            Timestamp createDate = results.getTimestamp(3);
            String createdBy = results.getString(4);
            Timestamp lastUpdate = results.getTimestamp(5);
            String lastUpdateBy = results.getString(6);

            Country country = new Country(id, countryName, createDate, createdBy, lastUpdate, lastUpdateBy);
            Country.addCountry(country);
        }

        statement.close();
        results.close();
    }
}

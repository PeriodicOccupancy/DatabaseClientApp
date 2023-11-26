package helper;

import Model.Appointment;
import Model.Enums.AccessType;
import Model.Enums.ObjAccessed;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

import static Model.User.activeUser;
import static helper.DatabaseAccessIO.databaseAccessSaveFile;
import static helper.Helper.toLocal;
import static helper.JDBC.connection;

/** Class to control accesses to the Appointment table in the Database.
 @author  Mike Fasnacht, mfasnac@wgu.edu
 @version  1.0, September 26th, 2022
 */
public class AppointmentCRUD {

    /** Method to populate the observable appointment list from appointments in the database when the program launches. */
    public static void populateAppointments() throws SQLException {
        //ResultSet results = sqlQuery("*", "appointments");
        Statement statement = connection.createStatement();
        ResultSet results =
                statement.executeQuery("SELECT * FROM client_schedule.appointments");

        while (results.next()){
            int appointmentID = results.getInt(1);
            int customerID = results.getInt(12);
            int userID = results.getInt(13);
            int contactID = results.getInt(14);
            String title = results.getString(2);
            String location = results.getString(3);
            String description = results.getString(4);
            String type = results.getString(5);
            LocalDateTime startUTC = results.getTimestamp(6).toLocalDateTime();
            LocalDateTime endUTC = results.getTimestamp(7).toLocalDateTime();
            LocalDateTime createDateUTC = results.getTimestamp(8).toLocalDateTime();
            String createdBy = results.getString(9);
            LocalDateTime lastUpdateUTC = results.getTimestamp(10).toLocalDateTime();
            String lastUpdatedBy = results.getString(11);

            //System.out.println("StartUTC: " + startUTC + "\nEndUTC:" + endUTC);
            LocalDateTime start = toLocal(startUTC);
            LocalDateTime end = toLocal(endUTC);
            //System.out.println("Start: " + start + "\nEnd:" + end);
            LocalDateTime createDate = toLocal(createDateUTC);
            LocalDateTime lastUpdate = toLocal(lastUpdateUTC);

            Appointment appointment = new Appointment(appointmentID, customerID, userID, contactID, title, location, description, type, start, end, createDate,createdBy, lastUpdate, lastUpdatedBy);
            Appointment.addAppointment(appointment);
        }

        statement.close();
        results.close();
    }

    /** Method to delete an appointment frm the database. */
    public static int deleteAppointmentFromDB(Appointment appointment) throws SQLException, IOException {
        String sql = "DELETE FROM client_schedule.appointments WHERE Appointment_ID = ?;";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, appointment.getAppointmentID());
        int rowsAffected = statement.executeUpdate();

        databaseAccessSaveFile(activeUser, AccessType.DELETE, ObjAccessed.Appointment,appointment.getAppointmentID());

        return rowsAffected;
    }


}

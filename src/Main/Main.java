package Main;

import helper.Helper;
import helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/** This class initializes the necessary methods and launches the application.
  Any code that is necessary for the program to work, but only need to be run once,
 is executed here before the launch method to initialize the first GUI page.
 @author  Mike Fasnacht, mfasnac@wgu.edu
 @version  1.0, September 26th, 2022
 */
public class Main extends Application{


    /** This Override method initializes the login page for the application.
      It  initializes the Login page and calls a resource bundle for translation and sets the Title of the login page
     to the correct translation, if no translation is found it defaults to "Login".
      @param primaryStage The first GUI page of the program, the login page
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        ResourceBundle rb = ResourceBundle.getBundle("Model/Nat", Locale.getDefault());
        Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
        try {
            primaryStage.setTitle(rb.getString("login"));
        } catch(MissingResourceException e){
            primaryStage.setTitle("Login");
        }
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /** This is the main method, where the program starts.
      It opens a connection to the MySQL database as defined in the helper.JDBC class with the openConnection() method,
     it then runs the Helper.populateLists() method to create a set of observable lists from all the tables in the
     database and a DatabaseAccess list from "DatabaseReport.txt". It then executes a launch argument to create and
     display the login GUI page. On the eventual close of the application it will then close the connection to the
     database before fully finishing execution.*/
    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        //Locale.setDefault(new Locale("fr"));  //For testing translation
        JDBC.openConnection();//opens up the connection to the mySQL database
        Helper.populateLists(); //creates a persistent Observable list for objects from the database, for the duration of the program
        launch(args);
        JDBC.closeConnection(); //closes the database connection
    }
}

package Controller;

import Model.User;
import helper.JDBC;
import helper.LoginLog;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;

import static Model.User.activeUser;
import static Model.User.getUserList;
import static helper.Helper.appointmentReminder;


/** The login controller.
 This controller maintains the Login.fxml GUI.
 @author  Mike Fasnacht, mfasnac@wgu.edu
 @version  1.0, September 26th, 2022
 */
public class LoginController implements Initializable {

    public TextField usernameTxt;
    public PasswordField passwordTxt;
    public Button loginBtn;
    public Label userLabel;
    public Label passLabel;
    public Button exitBtn;
    public Label loginPgLabel;
    public Label zoneLabel;
    DateTimeFormatter logFormatter = DateTimeFormatter.ofPattern("EEE, h:mm:ss a, LLL/d/uuuu");


    /** The Override initialize() method for the LoginController.
     This method initializes the login page and sets all text to translate in a try/catch block. if a Missing Resource
     Exception is thrown, all text remains as it's english default.
     @param resourceBundle  the Model/Nat resource bundle to allow for French translation of the program.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        //usernameTxt.setText("test");    //temporary code for quicker testing
        //passwordTxt.setText("test");    //temporary code for quicker testing

        //changes a label on the login page to display the systems default time zone
        zoneLabel.setText(String.valueOf(ZoneId.systemDefault()));
        try{
            //try/catch block to translate the login page to french based on the key provided in Nat_fr.properties
            ResourceBundle rb = ResourceBundle.getBundle("Model/Nat", Locale.getDefault());
            loginPgLabel.setText(rb.getString("title"));
            userLabel.setText(rb.getString("username"));
            passLabel.setText(rb.getString("password"));
            usernameTxt.setPromptText(rb.getString("userPrompt"));
            passwordTxt.setPromptText(rb.getString("passPrompt"));
            loginBtn.setText(rb.getString("login"));
            exitBtn.setText(rb.getString("exit"));

        } catch(MissingResourceException e){
            System.out.println("No translation available");
        }

    }

    //method tied to the login button on the login page, pulls user entered text from a text and password field and
    // compares them to users in the database to find a match, if a match is found, the user, password, and ID are
    //saved to activeUser to track the current user, and the application grants access to the Directory page
    //if the credentials are incorrect displays an alert notifying the user
    /** Login Method tied to the login button on the login page.
     This method pulls the User entered text from the username and password text fields, initializes a boolean
     'validPass' as false (for later use) and then runs through the list of users in a for loop. if a match to both
     username and password is found a static pointer 'activeUser' is assigned to that user object, 'validPass' is set
     to true, and it breaks the loop. Then an if/else statement checks the status of 'validPass'.

     *IF TRUE*
     The loginLog() method executes with 'activeUser' as an argument to write the successful login to
     "login_activity.txt" before initializing the Directory page. After initializing the Directory page, the
     appointmentReminder() method is called from the helper.Helper class to check for upcoming appointments and display
     an alert

     *IF FALSE*
     The method loginLog() gets called with 2 strings passed, the user entered text for username and password, to
     create a record of the unsuccessful login attempt. It then displays an alert notifying the user that the username
     and password combination is invalid, which can be translated to french based on the systems default locale. */
    public void login(ActionEvent actionEvent) throws IOException {
        String user = usernameTxt.getText();
        String pass = passwordTxt.getText();
        boolean validPass = false;

        activeUser.setUsername(user);
        activeUser.setPassword(pass);
        for(User forUser : getUserList()){
            if (forUser.getUsername().equals(user) && forUser.getPassword().equals(pass)){
                validPass = true;
                activeUser = forUser;
                break;
            }
        }
        logAttempt(lambdaLog, activeUser, validPass);
        if (validPass){
            System.out.println("Login Successful");

            Parent root = FXMLLoader.load(getClass().getResource("/View/Directory.fxml"));
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Directory");
            stage.setScene(scene);
            stage.show();

            appointmentReminder();
        }else{

            ResourceBundle rb = ResourceBundle.getBundle("Model/Nat", Locale.getDefault());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            try{
                alert.setTitle(rb.getString("invalid"));
                alert.setHeaderText(rb.getString("alertHeader"));
                alert.setContentText(rb.getString("alertContent"));
                Optional<ButtonType> result = alert.showAndWait();

            } catch(MissingResourceException e) {
                alert.setTitle("Invalid Login");
                alert.setHeaderText("Incorrect Username & Password combination.");
                alert.setContentText("Please enter a valid Username and Password.");
                Optional<ButtonType> result = alert.showAndWait();
            }
        }
    }

    // This is my lambda expression referenced in the logAttempt() JavaDoc.
    /** Lambda statement object.
     * Passed to logAttempt(). */
    LoginLog lambdaLog = (u, b) -> {
        FileWriter loginLogTxt = new FileWriter("login_activity.txt",true);
        PrintWriter loginLogWriter = new PrintWriter(loginLogTxt);
        String time = logFormatter.format(LocalDateTime.now());
        if(b)
            loginLogWriter.print("User " + u.getUsername() + " Logged in successfully at " + time + " in the " + ZoneId.systemDefault() + " timezone. \n");
        else
            loginLogWriter.print("Unsuccessful login attempt at " + time + " in the " + ZoneId.systemDefault() + " timezone, with credentials Username: '" + u.getUsername() + "' & Password: '" + u.getPassword() + "'.\n");

        loginLogWriter.close();
    };

    /** Lambda Method to record successful login attempts.
     appends a string to a new line in "login_activity.txt" with a username, time, and zoneID. Creates the file if it
     doesn't already exist.
     The contained lambda function allowed me to deprecate the loginLog methods, in favor of a single code block with
     an if/else statement and replace the 2 separate function calls in the login() method with a single call to this
     function that passes a user and a boolean to the lambda function to execute
     @param lambda Contains a predefined lambda statement that gets passed the user and boolean objects to execute its code
     @param user always gets passed a pointer to the current active user, to get their username.
     @param success  boolean value to determine what the lambda function does*/
    void logAttempt(LoginLog lambda, User user, boolean success) throws IOException {
        lambda.loginLog(user, success);
    }

    /** Program Exit Button method.
     Simply closes the database connection and exits the program when the Exit button is pressed. */
    public void exitProgram(ActionEvent actionEvent) {
        JDBC.closeConnection();
        System.exit(0);
    }



    /** Deprecated Method to record successful login attempts.
      Replaced by a lambda function.
     appends a string to a new line in "login_activity.txt" with a username, time, and zoneID. Creates the file if it
     doesn't already exist.
     @param user always gets passed a pointer to the current active user, to get their username.
     @deprecated */
    public void loginLog(User user) throws IOException {
        FileWriter loginLogTxt = new FileWriter("login_activity.txt",true);
        PrintWriter loginLog = new PrintWriter(loginLogTxt);
        String time = logFormatter.format(LocalDateTime.now());
        loginLog.print("User " + user.getUsername() + " Logged in successfully at " + time + " in the " + ZoneId.systemDefault() + " timezone. \n");
        loginLog.close();
    }

    /** Deprecated Method to record unsuccessful login attempts.
      Replaced by a lambda function
     @param user a username string passed based on the users entered text
     @param pass  a password string passed based on the users entered text
     @deprecated */
    public void loginLog(String user, String pass) throws IOException {
        FileWriter loginLogTxt = new FileWriter("login_activity.txt",true);
        PrintWriter loginLog = new PrintWriter(loginLogTxt);
        String time = logFormatter.format(LocalDateTime.now());
        loginLog.print("Unsuccessful login attempt at " + time + " in the " + ZoneId.systemDefault() + " timezone, with credentials Username: '" + user + "' & Password: '" + pass + "'.\n");
        loginLog.close();
    }

}

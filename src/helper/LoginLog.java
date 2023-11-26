package helper;

import Model.User;

import java.io.IOException;

/** Functional interface for lambda function in LoginController.
 @author  Mike Fasnacht, mfasnac@wgu.edu
 @version  1.0, September 26th, 2022
 */
@FunctionalInterface
public interface LoginLog {
    void loginLog(User user, boolean attempt) throws IOException;
}

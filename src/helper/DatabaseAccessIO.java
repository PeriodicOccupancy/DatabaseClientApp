package helper;

import Model.DatabaseAccess;
import Model.Enums.AccessType;
import Model.Enums.ObjAccessed;
import Model.User;

import java.io.*;

import static Model.DatabaseAccess.addAccessList;
import static Model.DatabaseAccess.getAccessList;

/** Class to control File I/O of the DatabaseAccess object.
 @author  Mike Fasnacht, mfasnac@wgu.edu
 @version  1.0, September 26th, 2022
 */
public class DatabaseAccessIO {

    /** Method to create a DatabaseAccess object.
     * Adds the object to a list and serializes the list to maintain data records across application shutdown and startup. */
    public static void databaseAccessSaveFile(User user, AccessType accessType, ObjAccessed objAccessed, int id ) throws IOException {
        new DatabaseAccess(user, accessType, objAccessed, id);

        FileOutputStream fos = new FileOutputStream("DatabaseReport.txt");
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        int i = 0;
        for(DatabaseAccess dba : getAccessList()) {
            try {
                oos.writeObject(dba);
                i++;
            } catch (NotSerializableException e) {
                System.out.println("Object not serializable");
                e.printStackTrace();
            }
        }
        fos.close();
        oos.close();
        System.out.println("Saved " + i + " Database Access records");
        System.out.println("Access List size = " + getAccessList().size());
    }


    //Run once method for program startup, Grabs a serialized list of DatabaseAccess objects
    // and populates the list used for tracking them all on startup
    /** Method to read a list of serialized DatabaseAccess objects from DatabaseReport.txt.
     * Runs once at program startup to populate a list of DatabaseAccess objects. */
    public static void databaseAccessReadFile() throws IOException, ClassNotFoundException {
        try{
            FileInputStream fis = new FileInputStream("DatabaseReport.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);

            DatabaseAccess dba;

            int initial = fis.available();
            System.out.println(fis.available());

            while (fis.available() != 0) {
                dba = (DatabaseAccess) ois.readObject();
                addAccessList(dba);
                double percentage = (fis.available() / initial) * 100;
                System.out.println(percentage + "% complete");
            }
            System.out.println("Success!");

            ois.close();
            fis.close();
            System.out.println("DatabaseReport loaded successfully.");

        }catch (FileNotFoundException e){
            System.out.println("DatabaseReport.txt not found, loading without.");
            //e.printStackTrace();
        }catch (InvalidClassException e){
            System.out.println("Something went wrong DatabaseReport.txt is corrupt");
            e.printStackTrace();
        }
    }
}

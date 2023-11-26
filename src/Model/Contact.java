package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** Class for initializing and controlling contact objects.
 @author  Mike Fasnacht, mfasnac@wgu.edu
 @version  1.0, September 26th, 2022
 */
public class Contact {
    private int contactID;
    private String contactName;
    private String email;

    /** Constructor method for the Contact object.
     * @param contactID unique integer identifier for a Contact.
     * @param contactName String of the Contact's Name.
     * @param email Email to get in touch with the Contact. */
    public Contact(int contactID, String contactName, String email){
        this.contactID = contactID;
        this.contactName = contactName;
        this.email = email;
    }
    private static ObservableList<Contact> contactList = FXCollections.observableArrayList();

        /** Method to add a contact to the contactList. */
        public static void addContact(Contact contact){
        contactList.add(contact);
    }

        /** Method to return the contactList. */
        public static ObservableList<Contact> getContactList(){
        return contactList;
    }

        /** Method to delete a contact from the contactList. */
        public static void deleteContact(Contact contact){
        contactList.remove(contact);
    }


    /** Gets the Contact ID. */
    public int getContactID() {
        return contactID;
    }

    /** Sets the Contact ID. */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    /** Gets the Contact Name. */
    public String getContactName() {
        return contactName;
    }

    /** Sets the Contact Name. */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /** Gets the Contact's Email. */
    public String getEmail() {
        return email;
    }

    /** Gets the Contact's Email. */
    public void setEmail(String email) {
        this.email = email;
    }
}

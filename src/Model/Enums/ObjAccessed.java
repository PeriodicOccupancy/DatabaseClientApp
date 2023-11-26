package Model.Enums;

/** Enumerated data class for storing the type of object accessed by a DatabaseAccess.
 @author  Mike Fasnacht, mfasnac@wgu.edu
 @version  1.0, September 26th, 2022
 */
public enum ObjAccessed {
    /** Enum type representing the Appointment Class type.
     * Used for identifying that a Database Access affected an Appointment Object. */
    Appointment,

    /** Enum type representing the Customer Class type.
     * Used for identifying that a Database Access affected a Customer Object. */
    Customer;

}

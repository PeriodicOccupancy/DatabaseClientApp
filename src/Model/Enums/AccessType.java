package Model.Enums;

/** Enumerated data class for storing access types for the DatabaseAccess Object.
 *
 @author  Mike Fasnacht, mfasnac@wgu.edu
 @version  1.0, September 26th, 2022
 */
public enum AccessType {
    /** INSERT enum data type representing an INSERT (object creation) operation in the sql database.
     *  Used with the DatabaseAccess class. */
    INSERT,

    /** UPDATE enum data type representing an UPDATE operation in the sql database.
     * Used with the DatabaseAccess class. */
    UPDATE,

    /** DELETE enum data type representing a DELETE operation in the sql database.
     * Used with the DatabaseAccess class. */
    DELETE,

    /** SELECT enum data type representing a SELECT (search) operation in the sql database.
     * Used with the DatabaseAccess class. This value is unused in the current program version. */
    SELECT;


}

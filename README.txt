#Title:      Customer Appointment Database Management Client

Class project written mostly in Java and JavaFX, and some MySQL, meant to be used alongside a SQL database which is not included here.

Purpose:    To provide an easy and intuitive way to view, add, update, or delete Customer and Appointment Records from
the database. As well as provide some basic reporting information, including a record of logins into the application
provided in a txt file, and a dynamic record of Appointments sorted by any combination of type or month. Lastly, a record of
database changes made from the application, including what user performed the operation, what Table was manipulated,
the object's ID number, and a Zoned Date and Time for the operation, shown in the local time zone.

Author:     Mike Fasnacht
    Contact:    mfasnac@gmail.com
    Student App. #:     Version 2
    Date:       9/28/2022

IDE:    IntelliJ IDEA 2021.3.1 (Community Edition)
    JDK Version:    17 (Oracle OpenJDK version 17.0.2)
    JavaFX Version: javafx-sdk-17.0.2

Directions:     The program launches into a login screen, and provides valid credentials to proceed to the directory page.
From the Directory page, you can click on Appointments to view, create, update, or delete appointments, or the Customers
button to do the same for Customers. You can also click on reports to view one of several reports. Appointment reports
display the total number of appointments altogether, or of any month & type combination, and list them in a table view.
Contact reports list a schedule for each contact, based on a combo box selection, the scheduled appointments relevant
to the selected contact are listed in a table view. Finally, Database reports in the final tab display a persistent list
of INSERT (create), UPDATE & DELETE operations performed, which user performed them, whether it was an appointment or
customer affected, the ID associated with the relevant object, and the date & time the operation was performed.

Custom Report:      My chosen report, is an I/O maintained a record of database Accesses performed within the application,
serialized and recorded to "DatabaseReport.txt", to be loaded back into memory on the next startup of the application.

MySQL Connector driver version:     mysql-connector-java-8.0.30

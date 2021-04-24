package Presentation;

import javafx.beans.property.SimpleStringProperty;

public class MyRow {

    private String personName;
    private String occupation;
    private String role;
    private String programName;
    private String email;

    MyRow(String personName, String occupation, String role, String programName, String email) {
        this.personName = personName;
        this.occupation = occupation;
        this.role = role;
        this.programName = programName;
        this.email = email;
    }

}

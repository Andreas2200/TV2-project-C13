module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires postgresql;

    opens Presentation to javafx.fxml;
    exports Presentation;
}
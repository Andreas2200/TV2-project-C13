module org.example {
    requires javafx.controls;
    requires javafx.fxml;

    opens Presentation to javafx.fxml;
    exports Presentation;
}
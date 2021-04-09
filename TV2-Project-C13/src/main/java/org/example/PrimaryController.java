package org.example;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import CLI.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Module.DatabaseSystem;

public class PrimaryController implements Initializable {

    public Button signUpCloseButton;
    public Button signInCloseButton;
    public Button signInLoginButton;
    public TextField signInPassword;
    public TextField signInUsername;

    static DatabaseSystem dbSys = new DatabaseSystem();
    static User activeUser;


    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        activeUser = null;
    }

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    public void closeButtonHandler(ActionEvent actionEvent)
    {
        Stage stage = (Stage) signInCloseButton.getScene().getWindow();
        stage.close();
    }

    public void signInSignUpHandler(ActionEvent actionEvent) throws IOException {
        activeUser = dbSys.getUser(signInUsername.getText(),signInPassword.getText());
        switchToSecondary();
    }
}

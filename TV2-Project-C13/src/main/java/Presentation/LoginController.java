package Presentation;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.Locale;
import java.util.ResourceBundle;

import Domain.ConsumerSystem;
import Domain.User;
import Persistence.DatabaseSystem;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

public class LoginController implements Initializable {

    @FXML
    public DatePicker birthdayDatePicker;
    @FXML
    public PasswordField passwordField;
    @FXML
    public TextField userNameField, createUserNameField, createUserUsernameField, createUserEmailField, createUserPasswordField;
    @FXML
    private Button loginButton, signInScreenButton, signUpScreenButton, signInCloseButton, signUpCloseButton, signInLoginButton, signUpLoginButton;
    @FXML
    private ImageView signUpImageView, signInImageView, signInCloseImageView, signUpCloseImageView;
    @FXML
    private AnchorPane signUpPane, signInPane;
    @FXML
    private Label invalidPasswordLabel, missingInfoLabel;

    private Button button;
    private Image signInImage;
    private Image closeImage;

    private static final String NOT_CLICKED = "fx-background-color: transparent; -fx-base: #FFFF; -fx-border-color: #FFFF; -fx-text-fill: #AFAFAF";
    private static final String CLICKED = "-fx-background-color: #d21e1e; -fx-text-fill: #FFFF";
    private static final String INVALID = "-fx-text-fill: #d21e1e";
    private static final String VALID = "-fx-text-fill: GREEN";

    public static User activeUser = null;
    public static ConsumerSystem cs = new ConsumerSystem();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        invalidPasswordLabel.setVisible(false);
        missingInfoLabel.setVisible(false);

        DropShadow shadow = new DropShadow();
        signInImage = new Image(getClass().getResource("arrow-right.png").toString());
        signUpImageView.setImage(signInImage);
        signInImageView.setImage(signInImage);

        closeImage = new Image(getClass().getResource("times.png").toString());
        signInCloseImageView.setImage(closeImage);
        signUpCloseImageView.setImage(closeImage);

        //Changes the black image to the tv2-red colour
        Lighting lighting = new Lighting(new Light.Distant(45, 90, Color.valueOf("#d21e1e")));
        ColorAdjust bright = new ColorAdjust(0, 1, 1, 1);
        lighting.setContentInput(bright);
        lighting.setSurfaceScale(0.0);
        //applies that effect to the images
        signUpImageView.setEffect(lighting);
        signInImageView.setEffect(lighting);
        signInCloseImageView.setEffect(lighting);
        signUpCloseImageView.setEffect(lighting);

        signInScreenButton.toFront();
        signUpScreenButton.toFront();
        signInScreenButton.setStyle(CLICKED);
        signUpScreenButton.setStyle(NOT_CLICKED);

        //creating cool shadow effect on button, when hovered
        buttonShadower(signInScreenButton);
        buttonShadower(signUpScreenButton);
        buttonPopper(signUpLoginButton);
        buttonPopper(signInLoginButton);
        buttonPopper(signInCloseButton);
        buttonPopper(signUpCloseButton);
    }

    @FXML
    private void signInSignUpHandler(ActionEvent event) {

        if(event.getSource() == signInScreenButton) {
            signInPane.toFront();
            signInScreenButton.toFront();
            signUpScreenButton.toFront();
            signInScreenButton.setStyle(CLICKED);
            signUpScreenButton.setStyle(NOT_CLICKED);
            invalidPasswordLabel.setVisible(false);
            invalidPasswordLabel.setStyle(INVALID);
            invalidPasswordLabel.setText("Ugyldigt brugernavn/password!");
        }
        else if(event.getSource() == signUpScreenButton) {
            signUpPane.toFront();
            signInScreenButton.toFront();
            signUpScreenButton.toFront();
            signUpScreenButton.setStyle(CLICKED);
            signInScreenButton.setStyle(NOT_CLICKED);
        }
    }

    @FXML
    private void switchToSecondary(ActionEvent event) throws IOException
    {
        activeUser = cs.logIn(userNameField.getText().toLowerCase(),passwordField.getText());

        if(activeUser == null)
        {
            invalidPasswordLabel.setVisible(true);
            invalidPasswordLabel.setStyle(INVALID);
            invalidPasswordLabel.setText("Ugyldigt brugernavn/password!");
            return;
        }
        else {
            App.setRoot("secondary");
        }
    }

    public void handlelogInEnter(javafx.scene.input.KeyEvent keyEvent) throws IOException {
        var key = keyEvent.getCode();
        switch (key) {
            case ENTER:
                activeUser = cs.logIn(userNameField.getText().toLowerCase(),passwordField.getText());

                if(activeUser == null)
                {
                    invalidPasswordLabel.setVisible(true);
                    invalidPasswordLabel.setStyle(INVALID);
                    invalidPasswordLabel.setText("Ugyldigt brugernavn/password!");
                    return;
                }
                else {
                    App.setRoot("secondary");
                }
        }
    }

    public void handleCreateUserEnter(javafx.scene.input.KeyEvent keyEvent) throws IOException {
        var key = keyEvent.getCode();
        switch (key) {
            case ENTER:
                if(createUserNameField.getText() != null && createUserUsernameField.getText() != null && birthdayDatePicker.getValue() != null
                        && createUserPasswordField.getText() != null && createUserEmailField.getText() != null) {
                    //Convert datepicker value to an age
                    LocalDate today = LocalDate.now();
                    LocalDate birthdate = birthdayDatePicker.getValue();
                    Period p = Period.between(birthdate,today);
                    int age = p.getYears();
                    // Create the user
                    cs.createUser(createUserNameField.getText(), createUserUsernameField.getText(), createUserPasswordField.getText(), age, createUserEmailField.getText());
                    //GUI controls
                    signInPane.toFront();
                    signInScreenButton.toFront();
                    signUpScreenButton.toFront();
                    invalidPasswordLabel.setVisible(true);
                    invalidPasswordLabel.setStyle(VALID);
                    invalidPasswordLabel.setText("Succesfuld oprettelse af bruger");
                    missingInfoLabel.setVisible(false);
                    signInScreenButton.setStyle(CLICKED);
                    signUpScreenButton.setStyle(NOT_CLICKED);
                    createUserNameField.setText(null);
                    createUserUsernameField.setText(null);
                    createUserPasswordField.setText(null);
                    createUserEmailField.setText(null);
                    birthdayDatePicker.setValue(null);
                }
                else {
                    missingInfoLabel.setVisible(true);
                }
        }
    }


    @FXML
    private void createUser(ActionEvent event){
        if(event.getSource() == signUpLoginButton) {
            if(createUserNameField.getText() != null && createUserUsernameField.getText() != null && birthdayDatePicker.getValue() != null
                    && createUserPasswordField.getText() != null && createUserEmailField.getText() != null) {
                //Convert datepicker value to an age
                LocalDate today = LocalDate.now();
                LocalDate birthdate = birthdayDatePicker.getValue();
                Period p = Period.between(birthdate,today);
                int age = p.getYears();
                // Create the user
                cs.createUser(createUserNameField.getText(), createUserUsernameField.getText(), createUserPasswordField.getText(), age, createUserEmailField.getText());
                //GUI controls
                signInPane.toFront();
                signInScreenButton.toFront();
                signUpScreenButton.toFront();
                invalidPasswordLabel.setVisible(true);
                invalidPasswordLabel.setStyle(VALID);
                invalidPasswordLabel.setText("Succesfuld oprettelse af bruger");
                missingInfoLabel.setVisible(false);
                signInScreenButton.setStyle(CLICKED);
                signUpScreenButton.setStyle(NOT_CLICKED);
                createUserNameField.setText(null);
                createUserUsernameField.setText(null);
                createUserPasswordField.setText(null);
                createUserEmailField.setText(null);
                birthdayDatePicker.setValue(null);
            }
            else {
               missingInfoLabel.setVisible(true);
            }
        }
    }


    @FXML
    private void closeButtonHandler() {
        Stage stage = (Stage) signInCloseButton.getScene().getWindow();
        stage.close();
        Stage stage1 = (Stage) signUpCloseButton.getScene().getWindow();
        stage1.close();
    }

    private void buttonShadower(Button button) {
        this.button = button;
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.valueOf("#d21e1e"));
        shadow.setHeight(20);
        button.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                button.setEffect(shadow);
            }
        });
        button.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                button.setEffect(null);
            }
        });
    }

    private void buttonPopper(Button button) {
        Scale bigScale = new Scale();
        bigScale.setX(1.15);
        bigScale.setY(1.15);

        DropShadow shadow = new DropShadow(BlurType.values()[3],Color.valueOf("#BFBFBF"), 4, 0.0f, -2.0f, -2.0f);

        button.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                button.setEffect(shadow);
                button.getTransforms().add(bigScale);
            }
        });
        button.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                button.setEffect(null);
                button.getTransforms().remove(bigScale);
            }
        });
    }
}

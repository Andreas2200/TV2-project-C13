package org.example;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class PrimaryController implements Initializable {

    @FXML
    private Button loginButton;
    @FXML
    private Button signInScreenButton;
    @FXML
    private Button signUpScreenButton;
    @FXML
    private ImageView signUpImageView;
    @FXML
    private ImageView signInImageView;
    @FXML
    private AnchorPane signUpPane, signInPane;


    private Image signInImage;

    private static final String NOT_CLICKED = "fx-background-color: transparent";
    private static final String CLICKED = "-fx-background-color: #d21e1e; -fx-text-fill: #FFFF";

    @FXML
    private void signInSignUpHandler(ActionEvent event) {

        if(event.getSource() == signInScreenButton) {
            signInPane.toFront();
            signInScreenButton.toFront();
            signUpScreenButton.toFront();
            signInScreenButton.setStyle(CLICKED);
            signUpScreenButton.setStyle(NOT_CLICKED);
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
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        signInImage = new Image(getClass().getResource("arrow-circle-right.png").toString());
        signUpImageView.setImage(signInImage);
        signInImageView.setImage(signInImage);

        //Changes the black image to the tv2-red colour
        Lighting lighting = new Lighting(new Light.Distant(45, 90, Color.valueOf("#d21e1e")));
        ColorAdjust bright = new ColorAdjust(0, 1, 1, 1);
        lighting.setContentInput(bright);
        lighting.setSurfaceScale(0.0);
        //applies that effect to the images
        signUpImageView.setEffect(lighting);
        signInImageView.setEffect(lighting);
    }
}

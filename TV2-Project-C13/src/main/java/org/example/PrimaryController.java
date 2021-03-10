package org.example;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class PrimaryController implements Initializable {

    @FXML
    private ImageView keyImageView;
    @FXML
    private ImageView userImageView;
    @FXML
    private ImageView loginImageView;
    @FXML
    private ImageView someImageView;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Button loginButton;

    private Image keyImage;
    private Image userImage;
    private Image loginImage;
    private Image someImage;

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        keyImage = new Image(getClass().getResource("key.png").toString());
        keyImageView.setImage(keyImage);
        userImage = new Image(getClass().getResource("user.png").toString());
        userImageView.setImage(userImage);
        loginImage = new Image(getClass().getResource("TV_2_Hvid_RGB.png").toString());
        loginImageView.setImage(loginImage);
        someImage = new Image(getClass().getResource("TV_2_RGB.png").toString());
        someImageView.setImage(someImage);

        //Changes the black image to the tv2-red colour
        Lighting lighting = new Lighting(new Light.Distant(45, 90, Color.valueOf("#d21e1e")));
        ColorAdjust bright = new ColorAdjust(0, 1, 1, 1);
        lighting.setContentInput(bright);
        lighting.setSurfaceScale(0.0);
        //applies that effect to the images
        userImageView.setEffect(lighting);
        keyImageView.setEffect(lighting);


    }
}

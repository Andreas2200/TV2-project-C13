package org.example;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class SecondaryController implements Initializable {

    @FXML
    private ImageView userImageView, closeButtonImageView;
    @FXML
    private Button closeButton;

    private Image userImage;
    private Image closeButtonImage;
    private Image minimizeImage;
    private Circle circle = new Circle(75);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userImage = new Image(getClass().getResource("Dancingkid.jpg").toString());
        userImageView.setImage(userImage);
        circle.setCenterX(userImageView.getFitHeight()/2);
        circle.setCenterY(userImageView.getFitWidth()/2.75);
        userImageView.setClip(circle);

        closeButtonImage = new Image(getClass().getResource("times.png").toString());
        closeButtonImageView.setImage(closeButtonImage);
    }

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    @FXML
    private void closeButtonHandler(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
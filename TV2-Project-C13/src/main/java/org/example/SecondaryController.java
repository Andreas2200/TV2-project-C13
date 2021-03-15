package org.example;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SecondaryController implements Initializable {

    @FXML
    private ImageView userImageView, closeButtonImageView;
    @FXML
    private Button closeButton;
    @FXML
    private Button secondaryButton;

    private Image userImage;
    private Image closeButtonImage;
    private Image minimizeImage;
    private Circle circle = new Circle(75);
    private double xOffset;
    private double yOffset;

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
    private void switchToPrimary(ActionEvent event) throws IOException {
        App.setRoot("primary");
        /*Parent sceneParent = FXMLLoader.load(getClass().getResource("primary.fxml"));
        Scene scene = new Scene(sceneParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.initStyle(StageStyle.TRANSPARENT);
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                xOffset = mouseEvent.getSceneX();
                yOffset = mouseEvent.getSceneY();
            }
        });
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                window.setX(mouseEvent.getScreenX() - xOffset);
                window.setY(mouseEvent.getScreenY() - yOffset);
            }
        });
        scene.setFill(Color.TRANSPARENT);
        window.setScene(scene);
        window.show();*/
    }

    @FXML
    private void closeButtonHandler(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
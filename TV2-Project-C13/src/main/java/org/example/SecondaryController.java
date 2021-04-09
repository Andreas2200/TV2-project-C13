package org.example;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import CLI.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class SecondaryController implements Initializable {

    @FXML
    private ImageView userImageView, closeButtonImageView;
    @FXML
    private Button closeButton, manageCreditsButton, findPersonButton, findProgramButton, manageUsersButton,
            programButton1, programButton2, programButton3, programButton4, programButton5, programButton6, toSearchButton;
    @FXML
    private TitledPane addCreditTitledPane, createCreditTitledPane, createProgramTitledPane, createPersonTitledPane,
            viewUsersTitledPane, requestsTitledPane, editUserTitledPane, deleteUserTitledPane;
    @FXML
    private Accordion managementAccordion, managementAccordionUsers;
    @FXML
    private VBox personVBox, programVBox;
    @FXML
    private AnchorPane programInfoAnchorPane, programSearchAnchorPane;


    private Image userImage;
    private Image closeButtonImage;
    private Button buttoon;
    private Circle circle = new Circle(75);

    public Label userRoleField;
    public Label userNameField;

    private User activeUser = null;

    private static final String NON_CLICKED_TITLED_PANE = "-fx-background-color: #FFFF; -fx-color: #FFFF; -fx-border-color: #FFFF";
    private static final String CLICKED_TITLED_PANE = "-fx-background-color: #d21e1e; -fx-color: #d21e1e; -fx-border-color: #d21e1e;";
    private static final String NOT_CLICKED_BUTTON = "-fx-background-color: #d21e1e; -fx-text-fill: #FFFF";
    private static final String CLICKED_BUTTON = "-fx-background-color: #FFFF; -fx-background-color: white !important; -fx-text-fill: #AFAFAF;";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        activeUser = PrimaryController.activeUser;

        userImage = new Image(getClass().getResource("Dancingkid.jpg").toString());
        userImageView.setImage(userImage);
        circle.setCenterX(userImageView.getFitHeight()/2);
        circle.setCenterY(userImageView.getFitWidth()/2.75);
        userImageView.setClip(circle);

        closeButtonImage = new Image(getClass().getResource("times.png").toString());
        closeButtonImageView.setImage(closeButtonImage);

        personVBox.toFront();
        programSearchAnchorPane.toFront();

        addCreditTitledPane.setStyle(NON_CLICKED_TITLED_PANE);
        createCreditTitledPane.setStyle(NON_CLICKED_TITLED_PANE);
        createProgramTitledPane.setStyle(NON_CLICKED_TITLED_PANE);
        createPersonTitledPane.setStyle(CLICKED_TITLED_PANE);

        managementAccordion.setExpandedPane(createPersonTitledPane);
        managementAccordionUsers.setExpandedPane(viewUsersTitledPane);

        findPersonButton.setStyle(CLICKED_BUTTON);

        buttonShadower(findPersonButton);
        buttonShadower(manageCreditsButton);
        buttonShadower(manageUsersButton);
        buttonShadower(findProgramButton);

        //Paints the close button red
        Lighting lighting = new Lighting(new Light.Distant(45, 90, Color.valueOf("#d21e1e")));
        ColorAdjust bright = new ColorAdjust(0, 1, 1, 1);
        lighting.setContentInput(bright);
        lighting.setSurfaceScale(0.0);

        closeButtonImageView.setEffect(lighting);

        //Sets the user permissions and the text for user info
        userNameField.setText(activeUser.getName());
        userRoleField.setText(activeUser.getRole());

        setUserPermission();
    }

    private void setUserPermission()
    {
        if(activeUser.getRole().equals("User"))
        {
            manageCreditsButton.setVisible(false);
            manageUsersButton.setVisible(false);
        }
    }

    @FXML
    private void switchToPrimary(ActionEvent event) throws IOException
    {
        activeUser = null;
        App.setRoot("primary");
    }

    @FXML
    private void closeButtonHandler(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void toFrontHandler(ActionEvent event) {
        if(event.getSource() == manageCreditsButton) {
            managementAccordion.toFront();

            manageCreditsButton.setStyle(CLICKED_BUTTON);
            manageUsersButton.setStyle(NOT_CLICKED_BUTTON);
            findPersonButton.setStyle(NOT_CLICKED_BUTTON);
            findProgramButton.setStyle(NOT_CLICKED_BUTTON);
        }
        if(event.getSource() == findPersonButton) {
            personVBox.toFront();
            manageCreditsButton.setStyle(NOT_CLICKED_BUTTON);
            manageUsersButton.setStyle(NOT_CLICKED_BUTTON);
            findPersonButton.setStyle(CLICKED_BUTTON);
            findProgramButton.setStyle(NOT_CLICKED_BUTTON);
        }
        if(event.getSource() == manageUsersButton) {
            managementAccordionUsers.toFront();
            manageCreditsButton.setStyle(NOT_CLICKED_BUTTON);
            manageUsersButton.setStyle(CLICKED_BUTTON);
            findPersonButton.setStyle(NOT_CLICKED_BUTTON);
            findProgramButton.setStyle(NOT_CLICKED_BUTTON);
        }
        if(event.getSource() == findProgramButton) {
            programVBox.toFront();
            manageCreditsButton.setStyle(NOT_CLICKED_BUTTON);
            manageUsersButton.setStyle(NOT_CLICKED_BUTTON);
            findPersonButton.setStyle(NOT_CLICKED_BUTTON);
            findProgramButton.setStyle(CLICKED_BUTTON);
        }
        if(event.getSource() == programButton1); {
            programInfoAnchorPane.toFront();
        }
        if(event.getSource() == toSearchButton) {
            programSearchAnchorPane.toFront();
        }
    }

    @FXML
    private void titledPaneHandler(MouseEvent event) {

        if(event.getSource() == addCreditTitledPane) {
            createCreditTitledPane.setStyle(NON_CLICKED_TITLED_PANE);
            createPersonTitledPane.setStyle(NON_CLICKED_TITLED_PANE);
            createProgramTitledPane.setStyle(NON_CLICKED_TITLED_PANE);
            addCreditTitledPane.setStyle(CLICKED_TITLED_PANE);
        }
        if(event.getSource() == createCreditTitledPane) {
            createCreditTitledPane.setStyle(CLICKED_TITLED_PANE);
            createPersonTitledPane.setStyle(NON_CLICKED_TITLED_PANE);
            createProgramTitledPane.setStyle(NON_CLICKED_TITLED_PANE);
            addCreditTitledPane.setStyle(NON_CLICKED_TITLED_PANE);
        }
        if(event.getSource() == createPersonTitledPane) {
            createCreditTitledPane.setStyle(NON_CLICKED_TITLED_PANE);
            createPersonTitledPane.setStyle(CLICKED_TITLED_PANE);
            createProgramTitledPane.setStyle(NON_CLICKED_TITLED_PANE);
            addCreditTitledPane.setStyle(NON_CLICKED_TITLED_PANE);
        }
        if(event.getSource() == createProgramTitledPane) {
            createCreditTitledPane.setStyle(NON_CLICKED_TITLED_PANE);
            createPersonTitledPane.setStyle(NON_CLICKED_TITLED_PANE);
            createProgramTitledPane.setStyle(CLICKED_TITLED_PANE);
            addCreditTitledPane.setStyle(NON_CLICKED_TITLED_PANE);
        }
        if(event.getSource() == viewUsersTitledPane) {
            viewUsersTitledPane.setStyle(CLICKED_TITLED_PANE);
            requestsTitledPane.setStyle(NON_CLICKED_TITLED_PANE);
            editUserTitledPane.setStyle(NON_CLICKED_TITLED_PANE);
            deleteUserTitledPane.setStyle(NON_CLICKED_TITLED_PANE);
        }
        if(event.getSource() == requestsTitledPane) {
            viewUsersTitledPane.setStyle(NON_CLICKED_TITLED_PANE);
            requestsTitledPane.setStyle(CLICKED_TITLED_PANE);
            editUserTitledPane.setStyle(NON_CLICKED_TITLED_PANE);
            deleteUserTitledPane.setStyle(NON_CLICKED_TITLED_PANE);
        }
        if(event.getSource() == editUserTitledPane) {
            viewUsersTitledPane.setStyle(NON_CLICKED_TITLED_PANE);
            requestsTitledPane.setStyle(NON_CLICKED_TITLED_PANE);
            editUserTitledPane.setStyle(CLICKED_TITLED_PANE);
            deleteUserTitledPane.setStyle(NON_CLICKED_TITLED_PANE);
        }
        if(event.getSource() == deleteUserTitledPane) {
            viewUsersTitledPane.setStyle(NON_CLICKED_TITLED_PANE);
            requestsTitledPane.setStyle(NON_CLICKED_TITLED_PANE);
            editUserTitledPane.setStyle(NON_CLICKED_TITLED_PANE);
            deleteUserTitledPane.setStyle(CLICKED_TITLED_PANE);
        }
    }



    private void buttonShadower(Button button) {
        this.buttoon = button;
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.valueOf("#AFAFAF"));
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
}
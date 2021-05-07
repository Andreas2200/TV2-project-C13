package Presentation;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Domain.*;
import Interfaces.CreditInterface;
import Interfaces.GenreInterface;
import Interfaces.ProgramInterface;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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

public class LogicController implements Initializable {

    public Button secondaryButton11,secondaryButton1;
    public Label pageCounter;
    public TextArea searchedProgramCreditsTXT;
    public TextArea searchedProgramDescriptionTXT;
    public Label searchedProgramReleaseDate;
    public Label searchedProgramGenre;
    public Label searchedProgramDuration;
    public Label searchedProgramTitle;
    public TextField searchProgramField;
    @FXML
    private CheckBox deleteUserCheckBox;
    @FXML
    private ComboBox<String> editUserRoleCB;
    @FXML
    private ComboBox<User> editUserUsersCB, deleteUserCB;
    @FXML
    private ComboBox<Program> addCreditProgramProgramCB;
    @FXML
    private ComboBox<Credits> addCreditProgramCreditCB;
    @FXML
    private ComboBox<Person> creditPersonCB;
    @FXML
    private ComboBox<String> creditOccupationCB;

    @FXML
    private ImageView userImageView, closeButtonImageView;
    @FXML
    private Button closeButton, manageCreditsButton, findPersonButton, findProgramButton, manageUsersButton,
            programButton1, programButton2, programButton3, programButton4, programButton5, programButton6, programButton7, programButton8,
            programButton9, programButton10, programButton11, programButton12, toSearchButton,
            saveProgramButton, deleteProgramButton, editUserButton;

    private Button[] programButtons = new Button[12];

    @FXML
    private TitledPane addCreditTitledPane, createCreditTitledPane, createProgramTitledPane, createPersonTitledPane,
            viewUsersTitledPane, requestsTitledPane, editUserTitledPane, deleteUserTitledPane;
    @FXML
    private Accordion managementAccordion, managementAccordionUsers;
    @FXML
    private VBox personVBox, programVBox;
    @FXML
    private AnchorPane programInfoAnchorPane, programSearchAnchorPane;
    @FXML
    private ComboBox<Genre> genreComboBox;
    @FXML
    private TableView<Integer> findPersonTableView;
    @FXML
    private TableColumn<Integer, String> personCol, occupationCol, roleCol, programCol, contactInfoCol;
    @FXML
    private TextField searchPersonField, programTitleField, durationField, releaseDateField, personNameField, personBirthdayField, personEmailField, creditActorTextField;
    @FXML
    private TextArea programDescriptionArea, deleteUserReasonTXT;

    private Image userImage;
    private Image closeButtonImage;
    private Button button;
    private Circle circle = new Circle(75);

    public Label userRoleField, succesProgramField, deleteUserConfirmationLabel;
    public Label userNameField;
    @FXML
    private Label creditActorLabel;

    private User activeUser = null;
    static ConsumerSystem cs = null;

    private static final String NON_CLICKED_TITLED_PANE = "-fx-background-color: #FFFF; -fx-color: #FFFF; -fx-border-color: #FFFF";
    private static final String CLICKED_TITLED_PANE = "-fx-background-color: #d21e1e; -fx-color: #d21e1e; -fx-border-color: #d21e1e;";
    private static final String NOT_CLICKED_BUTTON = "-fx-background-color: #d21e1e; -fx-text-fill: #FFFF";
    private static final String CLICKED_BUTTON = "-fx-background-color: #FFFF; -fx-background-color: white !important; -fx-text-fill: #AFAFAF;";

    private int pageNumber = 1;
    private final int NUMBEROFPROGRAMSTODISPLAY = 12;
    private int numberOfAdditionalPages;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        succesProgramField.setVisible(false);

        activeUser = LoginController.activeUser;
        cs = LoginController.cs;
        numberOfAdditionalPages = cs.getAllPrograms().size() / NUMBEROFPROGRAMSTODISPLAY;

        setUpProgramButtons();
        //Plots values into comboBox
        updateComboBox();

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

        creditActorLabel.setVisible(false);
        creditActorTextField.setVisible(false);

        setUserPermission();
    }

    @FXML
    private void findPersonInformation(ActionEvent event) {
        findPersonTableView.getItems().clear();
        try{
            //System.out.println(cs.searchPerson(searchPersonField.getText()));
            //findPersonTableView.setEditable(false);
            //Collection<String> list = cs.searchPerson(searchPersonField.getText());
            ArrayList<String> list = cs.searchPerson(searchPersonField.getText());
            final ObservableList<String> details = FXCollections.observableArrayList(list);

            // Smid værdierne ind i hver deres arraylist, så cellerne kan hente værdierne.
            ArrayList<String> personList = new ArrayList<>();
            ArrayList<String> occupationList = new ArrayList<>();
            ArrayList<String> roleList = new ArrayList<>();
            ArrayList<String> programList = new ArrayList<>();
            ArrayList<String> contactList = new ArrayList<>();

            for(String element: list) {
                String[] elementValues = element.split(";");
                personList.add(elementValues[0]);
                occupationList.add(elementValues[1]);
                roleList.add(elementValues[2]);
                programList.add(elementValues[3]);
                contactList.add(elementValues[4]);
            }

            //Indsæt værdierne fra listen i de respektive celler
            personCol.setCellValueFactory(cellData -> {
                Integer rowIndex = cellData.getValue();
                return new ReadOnlyStringWrapper(personList.get(rowIndex));
            });
            occupationCol.setCellValueFactory(cellData -> {
                Integer rowIndex = cellData.getValue();
                return new ReadOnlyStringWrapper(occupationList.get(rowIndex));
            });
            roleCol.setCellValueFactory(cellData -> {
                Integer rowIndex = cellData.getValue();
                return new ReadOnlyStringWrapper(roleList.get(rowIndex));
            });
            programCol.setCellValueFactory(cellData -> {
                Integer rowIndex = cellData.getValue();
                return new ReadOnlyStringWrapper(programList.get(rowIndex));
            });
            contactInfoCol.setCellValueFactory(cellData -> {
                Integer rowIndex = cellData.getValue();
                return new ReadOnlyStringWrapper(contactList.get(rowIndex));
            });

            // Indsæt cellerne i tableview

            for(int i = 0; i < personList.size(); i++) {
                findPersonTableView.getItems().add(i);
            }

            //cs.searchPerson(searchPersonField.getText());
            //System.out.println(cs.searchPerson(searchPersonField.getText()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void createEditProgram(ActionEvent event) throws NumberFormatException {
        if(event.getSource() == saveProgramButton) {
            //converting releasedate from String to Date
            //String tempDate = releaseDateField.getText();
            //Date releaseDate = new SimpleDateFormat("dd/MM/yyyy").parse(tempDate);

            // converting duration from String to LocalTime
            LocalTime durationTime = LocalTime.parse(durationField.getText());
            // converting GenreData from object to arraylist
            //ArrayList<Genre> tempGenres = new ArrayList<>();
            //tempGenres.add(genreComboBox.getSelectionModel().getSelectedItem());
            Genre tempGenre = genreComboBox.getSelectionModel().getSelectedItem();
            String tempGenres = tempGenre.toString();

            succesProgramField.setVisible(true);
            succesProgramField.setText("Program findes allerede..");
            succesProgramField.setStyle("-fx-text-fill: RED");

            if(!cs.doesProgramExist(programTitleField.getText())){
                cs.createEditProgram(programTitleField.getText(), releaseDateField.getText(), durationTime, tempGenres, programDescriptionArea.getText(), 1);
                succesProgramField.setText("Program blev oprettet!");
                succesProgramField.setStyle("-fx-text-fill: GREEN");
                updateComboBox();
                System.out.println(durationTime);
            }
            //cs.createEditProgram(programTitleField.getText(), releaseDateField.getText(), showedOnField.getText(), durationTime, tempGenres, programDescriptionArea.getText(), 1);
            //succesProgramField.setVisible(true);
            //updateComboBox();
        }

    }

    @FXML
    private void createEditPerson(ActionEvent event) {
        LocalDate today = LocalDate.now();
        String birthdateString = personBirthdayField.getText();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate birthdate = LocalDate.parse(birthdateString, dtf);
        Period p = Period.between(birthdate,today);
        int age = p.getYears();
        cs.createEditPerson(age, personEmailField.getText(), personNameField.getText());
        updateComboBox();
    }

    private void setUpCreateCredit()
    {
        //creditPersonCB.setItems(FXCollections.observableArrayList(cs.getAllPersons()));
    }

    private void setUpProgramButtons()
    {
        programButtons[0] = programButton1;
        programButtons[1] = programButton2;
        programButtons[2] = programButton3;
        programButtons[3] = programButton4;
        programButtons[4] = programButton5;
        programButtons[5] = programButton6;
        programButtons[6] = programButton7;
        programButtons[7] = programButton8;
        programButtons[8] = programButton9;
        programButtons[9] = programButton10;
        programButtons[10] = programButton11;
        programButtons[11] = programButton12;
        for (int i = 0; i < 12; i++)
        {
            programButtons[i].setVisible(false);
        }
    }

    private void setUserPermission()
    {
        if(activeUser.getRole().equals("User"))
        {
            manageCreditsButton.setVisible(false);
            manageUsersButton.setVisible(false);
        }
        else if(activeUser.getRole().equals("Producer"))
        {
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
            setProgramsAllPrograms();
            manageCreditsButton.setStyle(NOT_CLICKED_BUTTON);
            manageUsersButton.setStyle(NOT_CLICKED_BUTTON);
            findPersonButton.setStyle(NOT_CLICKED_BUTTON);
            findProgramButton.setStyle(CLICKED_BUTTON);
        }
        //Something wrong - bliver kaldt af findprogrambutton event.

        if(event.getSource() == toSearchButton) {
            programSearchAnchorPane.toFront();
        }
    }

    @FXML
    private void toFrontSearchProgramHandler(ActionEvent event) {

        if(searchProgramField.getText().equals(""))
        {
            for (int i = 0; i < programButtons.length; i++)
            {
                if(event.getSource() == programButtons[i]) {
                    Program tempProgram = getChosenProgram(i);

                    searchedProgramTitle.setText(tempProgram.getName());
                    searchedProgramDuration.setText(tempProgram.getDuration().toString());
                    searchedProgramDescriptionTXT.setText(tempProgram.getDescription());
                    searchedProgramReleaseDate.setText(tempProgram.getReleaseDate());

                    String genres = "";
                    /*for (GenreInterface element: tempProgram.getGenre())
                    {
                        genres += Genre.valueOf(element.toString()) + "\n";
                    }*/

                    String credits = "";
                    /*for (CreditInterface element: cs.getCredits(tempProgram.getId()))
                    {
                        credits += element.toString() + "\n";
                    }*/
                    searchedProgramGenre.setText(genres);
                    searchedProgramCreditsTXT.setText(credits);

                    searchedProgramCreditsTXT.setDisable(true);
                    searchedProgramDescriptionTXT.setDisable(true);

                    programInfoAnchorPane.toFront();
                    break;
                }
            }
        }
        else
        {
            for (int i = 0; i < programButtons.length; i++) {
                if (event.getSource() == programButtons[i]) {
                    Program tempProgram = getChosenSearchedProgram(i);

                    searchedProgramTitle.setText(tempProgram.getName());
                    searchedProgramDuration.setText(tempProgram.getDuration().toString());
                    searchedProgramDescriptionTXT.setText(tempProgram.getDescription());
                    searchedProgramReleaseDate.setText(tempProgram.getReleaseDate());

                    String genres = "";
                    /*for (GenreInterface element : tempProgram.getGenre()) {
                        genres += Genre.valueOf(element.toString()) + "\n";
                    }*/

                    String credits = "";
                    /*for (CreditInterface element : cs.getCredits(tempProgram.getId())) {
                        credits += element.toString() + "\n";
                    }*/
                    searchedProgramGenre.setText(genres);
                    searchedProgramCreditsTXT.setText(credits);

                    searchedProgramCreditsTXT.setDisable(true);
                    searchedProgramDescriptionTXT.setDisable(true);

                    programInfoAnchorPane.toFront();
                    break;
                }
            }
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
            setUpCreateCredit();
            System.out.println("Debug");
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
        if(event.getSource() == deleteUserTitledPane)
        {
            if(!deleteUserTitledPane.getStyle().equals(CLICKED_TITLED_PANE))
            {
                viewUsersTitledPane.setStyle(NON_CLICKED_TITLED_PANE);
                requestsTitledPane.setStyle(NON_CLICKED_TITLED_PANE);
                editUserTitledPane.setStyle(NON_CLICKED_TITLED_PANE);
                deleteUserTitledPane.setStyle(CLICKED_TITLED_PANE);
                deleteUserConfirmationLabel.setVisible(false);
            }
        }
    }

    private void buttonShadower(Button button) {
        this.button = button;
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

    private Program getChosenProgram(int buttonID)
    {
        return cs.getAllPrograms().get(buttonID + ((pageNumber-1) * NUMBEROFPROGRAMSTODISPLAY));
    }

    private Program getChosenSearchedProgram(int buttonID)
    {
        return cs.getSearchedProgram(searchProgramField.getText()).get(buttonID + ((pageNumber-1) * NUMBEROFPROGRAMSTODISPLAY));
    }

    private void setProgramsAllPrograms()
    {
        ArrayList<Program> programs = cs.getAllPrograms();

        setUpProgramButtons();

        if(pageNumber == numberOfAdditionalPages +1)
        {
            int numberOfActiveButtons = programs.size() - ((pageNumber-1) * NUMBEROFPROGRAMSTODISPLAY);
            pageCounter.setText(pageNumber + " ud af " + (numberOfAdditionalPages+1));
            for (int i = 0; i < numberOfActiveButtons; i++)
            {
                programButtons[i].setVisible(true);
                programButtons[i].setText(programs.get(i+((pageNumber-1) * NUMBEROFPROGRAMSTODISPLAY)).getName());
            }
        }
        else
        {
            pageCounter.setText(pageNumber + " ud af " + (numberOfAdditionalPages+1));
            for (int i = 0; i < NUMBEROFPROGRAMSTODISPLAY; i++)
            {
                programButtons[i].setVisible(true);
                programButtons[i].setText(programs.get(i+((pageNumber-1) * NUMBEROFPROGRAMSTODISPLAY)).getName());
            }
        }
    }


    @FXML
    private void setProgramSearchedProgram()
    {
        ArrayList<Program> programs = cs.getSearchedProgram(searchProgramField.getText());

        setUpProgramButtons();

        int numberOfAdditionalSearchPages = programs.size() / NUMBEROFPROGRAMSTODISPLAY;

        if(pageNumber == numberOfAdditionalSearchPages + 1)
        {
            int numberOfActiveButtons = programs.size() - ((pageNumber-1) * NUMBEROFPROGRAMSTODISPLAY);
            pageCounter.setText(pageNumber + " ud af " + (numberOfAdditionalSearchPages+1));
            for (int i = 0; i < numberOfActiveButtons; i++)
            {
                programButtons[i].setVisible(true);
                programButtons[i].setText(programs.get(i+((pageNumber-1) * NUMBEROFPROGRAMSTODISPLAY)).getName());
            }
        }
        else
        {
            pageCounter.setText(pageNumber + " ud af " + (numberOfAdditionalSearchPages+1));
            for (int i = 0; i < NUMBEROFPROGRAMSTODISPLAY; i++)
            {
                programButtons[i].setVisible(true);
                programButtons[i].setText(programs.get(i+((pageNumber-1) * NUMBEROFPROGRAMSTODISPLAY)).getName());
            }
        }
    }

    public void checkOccupationCB(ActionEvent actionEvent)
    {
        if(!creditOccupationCB.getValue().equals(Occupation.SKUESPILLER))
        {
            creditActorLabel.setVisible(false);
            creditActorTextField.setVisible(false);
        }
        else
        {
            creditActorLabel.setVisible(true);
            creditActorTextField.setVisible(true);
        }
    }

    public void saveCredit(ActionEvent actionEvent)
    {
        /*cs.saveCredit(new Credits(creditOccupationCB.getValue(),creditPersonCB.getValue(), creditActorTextField.getText()));
        updateComboBox();*/
    }

    public void saveCreditToProgram(ActionEvent actionEvent)
    {
        cs.saveCreditToProgram(addCreditProgramCreditCB.getValue(), addCreditProgramProgramCB.getValue().getId());
        updateComboBox();
    }

    @FXML
    private void editUserSave() throws IOException {
        cs.saveUser(editUserUsersCB.getValue(),editUserRoleCB.getValue());
        updateComboBox();
    }

    public void updateComboBox() {
        genreComboBox.setItems(FXCollections.observableArrayList(Genre.values()));
        creditOccupationCB.setItems(FXCollections.observableArrayList(Occupation.values().toString()));
        creditPersonCB.setItems(FXCollections.observableArrayList(cs.getAllPersons()));
        //addCreditProgramCreditCB.setItems(FXCollections.observableArrayList(cs.getAllCredits()));
        addCreditProgramProgramCB.setItems(FXCollections.observableArrayList(cs.getAllPrograms()));
        editUserRoleCB.setItems(FXCollections.observableArrayList("User","Producer","Admin"));
        editUserUsersCB.setItems(FXCollections.observableArrayList(cs.getAllUsersExcept(activeUser)));
        deleteUserCB.setItems(FXCollections.observableArrayList(cs.getAllUsersExcept(activeUser)));
        setProgramsAllPrograms();
    }

    @FXML
    private void deleteUser()
    {
        if(deleteUserCheckBox.isSelected())
        {
            cs.deleteUser(deleteUserCB.getValue(),deleteUserReasonTXT.getText());
            System.out.println("Delete User");
            deleteUserConfirmationLabel.setVisible(true);
            return;
        }
        System.out.println("Didn't delete User");
    }

    public void switchPage(ActionEvent actionEvent)
    {
        if(actionEvent.getSource() == secondaryButton1)
        {
            if(pageNumber > 1)
            {
                pageNumber--;
            }
            setProgramsAllPrograms();
            System.out.println("Page back");
        }
        if(actionEvent.getSource() == secondaryButton11)
        {
            if(pageNumber < numberOfAdditionalPages+1)
            {
                pageNumber++;
            }
            setProgramsAllPrograms();
            System.out.println("Page forward");
        }
    }
}
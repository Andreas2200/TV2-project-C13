package Domain;
import Interfaces.*;
import Persistence.DatabaseSystem;
import Persistence.UserData;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ConsumerSystem
{
    //<editor-fold desc="Attributes">
    private User activeUser = null;
    static DatabaseSystem dbSys = new DatabaseSystem();//</editor-fold>

    //<editor-fold desc="Methods for Person">
    public List<CreditInterface> searchPerson(String searchString) {
        List<PersonInterface> people = dbSys.getPersonsFromName(searchString);
        List<CreditInterface> credits = new ArrayList<>();
        for (PersonInterface p : people) {
            for (CreditInterface c : dbSys.getCreditFromID(p.getId())) {
                credits.add(c);
            }
        }
        return credits;
    } //Searches for a set string in the database and returns all credits that match that string

    public boolean createPerson(LocalDate tempBirthDate, String tempEmail, String tempName) {
        Person tempPerson = new Person(tempBirthDate, tempEmail, tempName);
        if (!dbSys.doesPersonExist(tempPerson.getEmail())) {
            dbSys.SavePerson(tempPerson, getUserID(activeUser));
            return false;
        } else {
            System.out.println("That Person already exists..");
            return true;
        }
    } //Creates a person and saves it in the database

    public void editPerson(LocalDate tempBirthDate, String tempEmail, String tempName) {
        Person tempPerson = new Person(tempBirthDate, tempEmail, tempName);
        dbSys.editPerson(tempPerson);
    } //Updates an existing person and saves changes to the database

    public ArrayList<Person> getAllPersonByCreatorId() {
        ArrayList<Person> returnList = new ArrayList<>();

        for (PersonInterface element : dbSys.getAllPersonsByCreatorId(getUserID(activeUser))) {
            returnList.add(new Person(element.getBirthDate(), element.getEmail(), element.getName()));
        }

        return returnList;
    } //Returns all persons made by a certain user by userId
    //</editor-fold>

    //<editor-fold desc="Methods for User">
    public int getUserID(UserInterface user) {
        return dbSys.getUserID(user.getUsername());
    } //Returns the database ID of a given user

    public void createUser(String tempUsername, String tempPass, String tempSalt, String tempName, String tempEmail, LocalDate tempBirthday) {
        User tempUser = new User(tempUsername, tempPass, tempSalt, tempName, tempEmail, tempBirthday);
        dbSys.saveUser(tempUser);
    } //Creates a new user, generates a unique salt, hashes the password and saves it to the database

    public ArrayList<User> getAllUser() {
        ArrayList<User> returnList = new ArrayList<>();

        try {
            for (UserInterface element : dbSys.getViewUser()) {
                returnList.add(new User(element.getId(), element.getName(), element.getRole(), element.getEmail()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnList;
    } //Returns all users from the database

    public List<UserInterface> viewUser() {
        List<UserInterface> users = new ArrayList<>();

        for (UserInterface u : dbSys.getViewUser()) {
            users.add(u);
        }
        return users;
    } //Returns all users from the database so it can be put into a tableview

    public void updateUserRole(int id, int tempRole_id) {
        dbSys.updateUserRole(id, tempRole_id);
    } //Updates a users role

    public void deleteUser(int user, String reason, int deletedBy) {
        dbSys.deleteUser(user, reason, deletedBy);
    } //Soft deletes a user by updating their "Active" boolean to false
    //</editor-fold>

    //<editor-fold desc="Methods for Credit">
    public boolean saveCredit(Person person, Program program, String occupation, String characterName) {
        return dbSys.saveCredit(new Credits(person, program, occupation, characterName, getUserID(activeUser)));
    } //Makes a new credit and saves it to the database

    public void deleteCredit(Person person, Program program, String occupation, String characterName) {
        dbSys.deleteCredit(new Credits(person, program, occupation, characterName, getUserID(activeUser)));
    } //Takes an existing credit and deletes it from the database

    public boolean doesCreditExist(Person person, Program program, String occupation, String characterName) {
        return dbSys.doesCreditExist(new Credits(person, program, occupation, characterName, getUserID(activeUser)));
    } //Checks if the credit exists

    public String getCreditsFromProgramTitle(String name) {
        return dbSys.getCreditsFromProgramId(dbSys.getProgramId(name));
    } //Returns all credits bound to a program
    //</editor-fold>

    //<editor-fold desc="Methods for Program">
    public ArrayList<Program> getAllPrograms() {
        ArrayList<Program> returnList = new ArrayList<>();

        for (ProgramInterface element : dbSys.getAllPrograms()) {
            returnList.add(mapProgramInterfaceProgram(element));
        }
        return returnList;
    } //Returns all programs from the database

    public ArrayList<Person> getAllPersons() {
        ArrayList<Person> returnList = new ArrayList<>();

        for (PersonInterface element : dbSys.getAllPersons()) {
            returnList.add(mapPersonInterfaceProgram(element));
        }
        return returnList;
    } //Returns all programs from the database

    public ArrayList<Program> getAllProgramsByCreatorId() {
        ArrayList<Program> returnList = new ArrayList<>();

        for (ProgramInterface element : dbSys.getAllProgramsByCreatorId(getUserID(activeUser))) {
            returnList.add(mapProgramInterfaceProgram(element));
        }

        return returnList;
    } //Returns all programs from the database created by a logged in user

    public ArrayList<Program> getSearchedProgram(String title) {
        ArrayList<Program> returnList = new ArrayList<>();

        for (ProgramInterface element : dbSys.getProgramBySearch(title)) {
            returnList.add(mapProgramInterfaceProgram(element));
        }
        return returnList;
    } //Returns all programs that includes the search string

    public boolean doesProgramExist(String name) {
        return dbSys.doesProgramExist(name);
    } //Checks if a program exists in the database

    public void updateProgram(String tempName, String tempDate, LocalTime tempDuration, String tempGenre, String tempDesc, int createrID) {
        dbSys.updateProgram(new Program(tempName, tempDate, tempDuration, tempGenre, tempDesc, createrID));
    } //Takes an existing program and updates the information on said program

    public void deleteProgram(String name) {
        dbSys.deleteProgram(name);
    } //Deletes a given program from the database

    public void createEditProgram(String tempName, String tempDate, LocalTime tempDuration, String tempGenre, String tempDesc, int createrID) {
        dbSys.saveProgram(new Program(tempName, tempDate, tempDuration, tempGenre, tempDesc, createrID));
    } //Creates program and saves it into the database

    private Program mapProgramInterfaceProgram(ProgramInterface element) {
        return new Program(element.getName(), element.getReleaseDate(), element.getDuration(), element.getGenre(), element.getDescription(), element.getCreatorID());
    } //Method for quickly mapping a ProgramInterface to Program

    private Person mapPersonInterfaceProgram(PersonInterface element) {
        return new Person(element.getBirthDate(), element.getEmail(), element.getName());
    } //Method for quickly mapping a ProgramInterface to Program
    //</editor-fold>

    //<editor-fold desc="Utilities">
    public User logIn(String tempUsername, String tempPass) {
        UserData userData = (UserData) dbSys.getUser(tempUsername, tempPass);
        try {
            activeUser = new User(userData.getUsername(), userData.getPassword(), userData.getSalt(), userData.getName(), userData.getEmail(), userData.getBirthday(), userData.getRole());
            return activeUser;
        } catch (NullPointerException e) {
            return null;
        }
    } //Method used for logging in, passes the information typed in by the user in to the database where it checks for correct information

    public ArrayList<String> getAllGenres() {
        return dbSys.getAllGenres();
    } //Returns all genres in a String ArrayList

    public ArrayList<String> getAllOccupations() {
        return dbSys.getAllOccupations();
    } //Returns all occupations in a String ArrayList

    public String hashPassword(String password, String salt) {
        return dbSys.hashPassword(password, salt);
    } //Passes the information into the database controller where it gets hashed and return
    //</editor-fold>
 }
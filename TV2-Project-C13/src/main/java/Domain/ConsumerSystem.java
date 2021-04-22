package Domain;

import Interfaces.UserInterface;
import Persistence.DatabaseSystem;
import Persistence.GenreData;
import Persistence.ProgramData;
import Persistence.UserData;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

public class ConsumerSystem
{
    private User activeUser = null;

    static DatabaseSystem dbSys = new DatabaseSystem();

    private void saveCredit()
    {

    }

    private void saveLinkedCredit()
    {

    }
    public void createEditPerson(int tempAge, String tempEmail, String tempName) {
        Person tempPerson = new Person(tempAge, tempEmail, tempName);
        try {
            dbSys.SavePerson(tempPerson);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void createUser(String tempName, String tempUsername, String tempPass, int tempBirthday, String tempEmail)
    {
        User tempUser = new User(tempName, tempUsername, tempPass, tempBirthday, tempEmail);
        try {
            dbSys.SaveUser(tempUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User logIn(String tempUsername,String tempPass)
    {
         UserData userData = (UserData) dbSys.getUser(tempUsername,tempPass);
         try {
             return new User(userData.getName(), userData.getUsername(), userData.getPassword(), userData.getAge(), "Sutenkuk", userData.getRole());
         } catch(NullPointerException e) {
             return null;
         }
    }

    private void changeUserRole()
    {

    }

    private void addCredit()
    {

    }

    private void linkCreditProgram()
    {

    }

    public void createEditProgram(String tempName, Date tempDate, String showedOn, LocalTime tempDuration, ArrayList<Genre> tempGenre, String tempDesc, int createrID) {
        Program tempProgram = new Program(tempName, tempDate, showedOn, tempDuration, tempGenre, tempDesc, createrID);
        dbSys.SaveProgram(tempProgram);
    }
}

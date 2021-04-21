package Domain;

import Interfaces.UserInterface;
import Persistence.DatabaseSystem;
import Persistence.UserData;

import java.io.IOException;
import java.time.LocalDate;

public class ConsumerSystem
{
    private User activeUser = null;

    static DatabaseSystem dbSys = new DatabaseSystem();

    private void saveUser(UserInterface user)
    {

    }

    private void saveProgram()
    {

    }

    private void saveCredit()
    {

    }

    private void saveLinkedCredit()
    {

    }

    public void createUser(String tempName, String tempUsername, String tempPass, int tempBirthday, String tempEmail)
    {
        UserData tempUser = new UserData(tempName, tempUsername, tempPass, tempBirthday, tempEmail);
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

    private void addProgram()
    {

    }

    private void addCredit()
    {

    }

    private void linkCreditProgram()
    {

    }
}

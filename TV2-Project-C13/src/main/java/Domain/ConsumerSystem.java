package Domain;

import Interfaces.UserInterface;
import Persistence.DatabaseSystem;
import Persistence.UserData;

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

    public void createUser()
    {

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

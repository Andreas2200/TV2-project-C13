package Domain;

import Interfaces.*;
import Persistence.CreditData;
import Persistence.DatabaseSystem;
import Persistence.UserData;


import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class ConsumerSystem
{
    public static void main(String[] args) {
        ConsumerSystem cs = new ConsumerSystem();
        cs.searchPerson("Freja");
    }
    private User activeUser = null;

    static DatabaseSystem dbSys = new DatabaseSystem();

    public List<CreditInterface> searchPerson(String searchString) {
        List<PersonInterface> people = dbSys.getPersonsFromName(searchString);
        List<CreditInterface> credits = new ArrayList<>();
        for(PersonInterface p: people) {
            for(CreditInterface c: dbSys.getCreditFromID(p.getId())) {
                credits.add(c);
            }
        }
        return credits;
    }

    public boolean saveCredit(Person person, Program program, String occupation, String characterName)
    {
        return dbSys.saveCredit(new Credits(person,program,occupation,characterName, getUserID(activeUser)));
    }

    public boolean doesCreditExist(Person person, Program program, String occupation, String characterName)
    {
        return dbSys.doesCreditExist(new Credits(person,program,occupation,characterName, getUserID(activeUser)));
    }

    public boolean deleteCredit(Person person, Program program, String occupation, String characterName)
    {
        return dbSys.deleteCredit(new Credits(person,program,occupation,characterName, getUserID(activeUser)));
    }

    public void createEditPerson(int tempAge, String tempEmail, String tempName) {
        Person tempPerson = new Person(tempAge, tempEmail, tempName);
        try {
            if(!dbSys.doesPersonExist(tempPerson.getEmail())){
                dbSys.SavePerson(tempPerson);
            } else{
                System.out.println("That Person already exists..");
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void createUser(String tempUsername, String tempPass, String tempSalt, String tempName, String tempEmail, LocalDate tempBirthday)
    {
        User tempUser = new User(tempUsername,tempPass,tempSalt,tempName,tempEmail,tempBirthday);
        dbSys.saveUser(tempUser);
    }

    public boolean createUser(User user)
    {
        return dbSys.saveUser(user);
    }

    public User logIn(String tempUsername,String tempPass)
    {
        UserData userData = (UserData) dbSys.getUser(tempUsername,tempPass);
        try
        {
            activeUser = new User(userData.getUsername(),userData.getPassword(),userData.getSalt(),userData.getName(),userData.getEmail(),userData.getBirthday(),userData.getRole());
            return activeUser;
        } catch(NullPointerException e) {
            return null;
        }
    }

    public void saveCredit(Credits credit)
    {
        dbSys.saveCredit(credit);
    }

    /*public ArrayList<Credits> getAllCredits()
    {
        ArrayList<Credits> returnList = new ArrayList<>();

        try
        {
            for (CreditInterface element: dbSys.getAllCredits())
            {
                returnList.add(new Credits(element.getOccupation(),new Person(element.getPerson().getAge(),element.getPerson().getId(),element.getPerson().getEmail(),element.getPerson().getName()), element.getCharacterName()));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return returnList;
    }*/

    public String getCreditsFromProgramTitle(String name)
    {
        return dbSys.getCreditsFromProgramId(dbSys.getProgramId(name));
    }

    public ArrayList<Program> getAllPrograms()
    {
        ArrayList<Program> returnList = new ArrayList<>();

        for (ProgramInterface element: dbSys.getAllPrograms())
        {
            returnList.add(mapProgramInterfaceProgram(element));
        }
        return returnList;
    }

    public ArrayList<Program> getAllProgramsByCreatorId()
    {
        ArrayList<Program> returnList = new ArrayList<>();

        for (ProgramInterface element:dbSys.getAllProgramsByCreatorId(getUserID(activeUser)))
        {
            returnList.add(mapProgramInterfaceProgram(element));
        }

        return returnList;
    }

    public ArrayList<String> getAllOccupations()
    {
        return dbSys.getAllOccupations();
    }

    public ArrayList<Person> getAllPersonByCreatorId()
    {
        ArrayList<Person> returnList = new ArrayList<>();

        for (PersonInterface element : dbSys.getAllPersonsByCreatorId(getUserID(activeUser)))
        {
            returnList.add(new Person(element.getAge(), element.getEmail(), element.getName()));
        }

        return returnList;
    }

    public ArrayList<User> getAllUsers()
    {
        ArrayList<User> returnList = new ArrayList<>();

        try {
            for (UserInterface element: dbSys.getAllUsers())
            {
                returnList.add(mapUserInterfaceUser(element));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnList;
    }

    /*public ArrayList<CreditInterface> getCredits(int programID)
    {
        return dbSys.getCredits(programID);
    }*/

    public ArrayList<Program> getSearchedProgram(String title) {
        ArrayList<Program> returnList = new ArrayList<>();

        for (ProgramInterface element: dbSys.getProgramBySearch(title) )
        {
            returnList.add(mapProgramInterfaceProgram(element));
        }
        return returnList;
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
    public boolean doesProgramExist(String name) {
        if(!dbSys.doesProgramExist(name)){
            return false;
        } else {
            return true;
        }
    }

    public void createEditProgram(String tempName, String tempDate, LocalTime tempDuration, String tempGenre, String tempDesc, int createrID) {
        dbSys.saveProgram(new Program(tempName, tempDate, tempDuration, tempGenre, tempDesc, createrID));
    }

    public void saveCreditToProgram(Credits credit, int programID)
    {
        if(dbSys.saveCredits(credit, programID))
        {
            System.out.println("Hold en pause, du har gjort det godt");
        }
        else
        {
            System.out.println("Hold stadigvæk en pause og kom tilbage til det");
        }
    }

    public void saveUser(User user, String role) throws IOException {
        user.setRole(role);
        //dbSys.updateUserPerms(user);
    }

    public void deleteUser(User user, String reason)
    {
        //dbSys.deleteUser(new UserData(user.getName(), user.getUsername(), user.getPassword(), user.getAge(), user.getEmail(), user.getRole()),reason);
    }

    private User mapUserInterfaceUser(UserInterface user)
    {
        return new User(user.getUsername(),user.getPassword(),user.getSalt(),user.getName(),user.getEmail(),user.getBirthday(),user.getRole());
    }

    private Program mapProgramInterfaceProgram(ProgramInterface element)
    {
        return new Program(element.getName(), element.getReleaseDate(), element.getDuration(), element.getGenre(), element.getDescription(), element.getCreatorID());
    }

    public ArrayList<User> getAllUsersExcept(User user)
    {
        ArrayList<User> returnArray = new ArrayList<>();

        /*for (UserInterface element: dbSys.getAllUsersExceptXUser(user))
        {
            returnArray.add(mapUserInterfaceUser(element));
        }*/
        return returnArray;
    }

    public ArrayList<String> getAllGenres()
    {
        return dbSys.getAllGenres();
    }

    public int getUserID(UserInterface user)
    {
        return dbSys.getUserID(user.getUsername());
    }

    public void updateProgram(String tempName, String tempDate, LocalTime tempDuration, String tempGenre, String tempDesc, int createrID)
    {
        dbSys.updateProgram(new Program(tempName, tempDate, tempDuration, tempGenre, tempDesc, createrID));
    }

    public void deleteProgram(String name)
    {
        dbSys.deleteProgram(name);
    }

    public String hashPassword(String password, String salt) {
        return dbSys.hashPassword(password,salt);
    }

}



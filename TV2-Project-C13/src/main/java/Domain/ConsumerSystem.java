package Domain;

import Interfaces.*;
import Persistence.DatabaseSystem;
import Persistence.GenreData;
import Persistence.ProgramData;
import Persistence.UserData;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ConsumerSystem
{
    private User activeUser = null;

    static DatabaseSystem dbSys = new DatabaseSystem();

    public ArrayList<String> searchPerson(String searchString) throws Exception {
        ArrayList<String> people = new ArrayList<>();
        String nameString = "";
        String occupationString = "";
        String roleString = "";
        String programString = "";
        PersonInterface p = null;
        try{
            // Lav et arraylist af personinterfaces, for at få personer
            ArrayList<PersonInterface> person = dbSys.SearchPerson(searchString);
            // Søg igennem personinterface, og få alle personers id
            for(int i = 0; i < person.size(); i++) {
                p = person.get(i);
                // Lav et arraylist for at kunne læse strengen og udtrække værdien for programid, samt occupation og role
                ArrayList<String> getCreditString = dbSys.getAllCreditsFromCreditFile(p.getId());
                for(String element: getCreditString) {
                    String[] values = element.split(";");
                    occupationString = values[1];
                    roleString = values[3];
                    //Lav igen et arraylist for at kunne læse strengen og udtrække værdien for programnavn
                    ArrayList<String> getProgramString = dbSys.getProgramFromID(values[2]);
                    for(String e: getProgramString) {
                        String[] programValues = e.split(";");
                        programString = programValues[1];
                        nameString = p.getName() + ";" + occupationString + ";" + roleString + ";" + programString + ";" + p.getEmail() + "\n";
                        people.add(nameString);
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return people;
    }

    private void saveCredit()
    {

    }

    private void saveLinkedCredit()
    {

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
             return new User(userData.getName(), userData.getUsername(), userData.getPassword(), userData.getAge(), userData.getEmail(), userData.getRole());
         } catch(NullPointerException e) {
             return null;
         }
    }

    public void saveCredit(Credits credit)
    {
        dbSys.saveCredit(credit);
    }

    public ArrayList<Credits> getAllCredits()
    {
        ArrayList<Credits> returnList = new ArrayList<>();

        try
        {
            for (CreditInterface element: dbSys.getAllCredits())
            {
                returnList.add(new Credits(Occupation.valueOf(element.getOccupation().toString()),new Person(element.getPerson().getAge(),element.getPerson().getId(),element.getPerson().getEmail(),element.getPerson().getName())));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return returnList;
    }

    public ArrayList<Program> getAllPrograms()
    {
        ArrayList<Program> returnList = new ArrayList<>();

        try
        {
            for (ProgramInterface element: dbSys.getProgram())
            {
                //Calendar calendar = Calendar.getInstance();
                //String[] calendarValues = element.getReleaseDate().split(",");
                //calendar.set(Integer.parseInt(calendarValues[0]), Integer.parseInt(calendarValues[1]), Integer.parseInt(calendarValues[2]));
                //Date date = new Date();
                //date = Calendar.getInstance().getTime();

                //String[] durationTime = element.getDuration().split(",");
                //LocalTime time = LocalTime.of(Integer.parseInt(durationTime[0]), Integer.parseInt(durationTime[1]));
                ArrayList<Genre> genres = new ArrayList<>();
                for (GenreInterface genreInterfaces: element.getGenre())
                {
                    genres.add(Genre.valueOf(genreInterfaces.toString()));
                }
                returnList.add(new Program(element.getId(), element.getName(), element.getReleaseDate(), element.getShowedOn(), element.getDuration(), genres, element.getDescription(), element.getCreatorID()));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return returnList;
    }


    public ArrayList<Person> getAllPersons()
    {
        ArrayList<Person> returnList = new ArrayList<>();

        try {
            for (PersonInterface element: dbSys.getAllPersons())
            {
                returnList.add(new Person(element.getAge(),element.getId(), element.getEmail(), element.getName()));
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    public void createEditProgram(String tempName, String tempDate, String showedOn, LocalTime tempDuration, ArrayList<Genre> tempGenre, String tempDesc, int createrID) {
        Program tempProgram = new Program(tempName, tempDate, showedOn, tempDuration, tempGenre, tempDesc, createrID);
        if(!dbSys.doesProgramExist(tempProgram.getName())) {
            dbSys.SaveProgram(tempProgram);
            System.out.println(tempProgram.getDuration());
        } else {
            System.out.println("Program already exists idiot..");
        }

    }

    public void saveCreditToProgram(Credits credit, int programID)
    {
        if(dbSys.saveCredits(credit,programID))
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
        dbSys.updateUserPerms(user);
    }

    public void deleteUser(User user, String reason)
    {
        dbSys.deleteUser(new UserData(user.getName(), user.getUsername(), user.getPassword(), user.getAge(), user.getEmail(), user.getRole()),reason);
    }

    private User mapUserInterfaceUser(UserInterface user)
    {
        return new User(user.getName(),user.getUsername(), user.getPassword(), user.getAge(), user.getEmail(), user.getRole());
    }

    public ArrayList<User> getAllUsersExcept(User user)
    {
        ArrayList<User> returnArray = new ArrayList<>();

        for (UserInterface element: dbSys.getAllUsersExceptXUser(user))
        {
            returnArray.add(mapUserInterfaceUser(element));
        }
        return returnArray;
    }
}

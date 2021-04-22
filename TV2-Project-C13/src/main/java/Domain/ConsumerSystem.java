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
                Calendar calendar = Calendar.getInstance();
                String[] calendarValues = element.getReleaseDate().split(",");
                calendar.set(Integer.parseInt(calendarValues[0]), Integer.parseInt(calendarValues[1]), Integer.parseInt(calendarValues[2]));
                Date date = new Date();
                date = Calendar.getInstance().getTime();


                String[] durationTime = element.getDuration().split(",");
                LocalTime time = LocalTime.of(Integer.parseInt(durationTime[0]), Integer.parseInt(durationTime[1]));
                ArrayList<Genre> genres = new ArrayList<>();
                for (GenreInterface genreInterfaces: element.getGenre())
                {
                    genres.add(Genre.valueOf(genreInterfaces.toString()));
                }
                returnList.add(new Program(element.getId(), element.getName(), date, element.getShowedOn(), time, genres, element.getDescription(), element.getCreatorID()));
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
}

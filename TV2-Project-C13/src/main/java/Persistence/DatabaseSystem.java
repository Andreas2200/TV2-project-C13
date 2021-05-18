package Persistence;


import Domain.Person;
import Domain.User;
import Interfaces.*;
import Domain.Occupation;
import javafx.util.converter.LocalDateStringConverter;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.Date;
import java.util.logging.SimpleFormatter;

//bruges kun til at teste database componenter og skal i ingen omstændigheder bruges i det endelige produkt
class Main {
    static DatabaseSystem dbSys = new DatabaseSystem();
    public static void main(String[] args) throws Exception
    {
        dbSys = dbSys.getInstance();
        //dbSys.getUser("morten420","Pa22Wo7d123");
        //System.out.println(dbSys.getProgramFromID(1));
        System.out.println(dbSys.getPersonsFromName("B"));
        //System.out.println(dbSys.getCreditFromID(1));

        /*for (CreditInterface element: dbSys.getCreditFromID(1))
        {
            String printString = "Program Name: " + element.getProgram().getName() + "\n" +
                    "Person Name: " + element.getPerson().getName() + "\n" +
                    "Occupation: " + element.getOccupation() + "\n" +
                    "Character Name: " + element.getCharacterName();
            System.out.println(printString);
        }*/
    }
}

public class DatabaseSystem
{
    private static DatabaseSystem instance;
    private String url = "217.61.218.112";
    private int port = 5432;
    private String databaseName = "TV2ProjectC13";
    private String username = "TV2Project";
    private String password = "1234";
    private Connection connection = null;

    public DatabaseSystem(){initializePostgresqlDatabase();}

    public static synchronized Persistence.DatabaseSystem getInstance(){
        if (instance == null){
            instance = new Persistence.DatabaseSystem();
        }
        return instance;
    }

    private void initializePostgresqlDatabase() {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            connection = DriverManager.getConnection("jdbc:postgresql://" + url + ":" + port + "/" + databaseName, username, password);
            System.out.println("Connection made");
        } catch (SQLException | IllegalArgumentException ex) {
            ex.printStackTrace(System.err);
        } finally {
            if (connection == null) System.exit(-1);
        }
    }

    //<editor-fold desc="Methods with SQL Implementation">

    public ArrayList<String> getAllOccupations()
    {
        ArrayList<String> returnList = new ArrayList<>();
        try
        {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM occupation");
            ResultSet sqlOccupationReturnValue = stmt.executeQuery();
            while (sqlOccupationReturnValue.next())
            {
                returnList.add(sqlOccupationReturnValue.getString(2));
            }
            return returnList;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> getAllGenres()
    {
        ArrayList<String> returnList = new ArrayList<>();
        try
        {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM genres");
            ResultSet sqlGenreReturnValue = stmt.executeQuery();
            while (sqlGenreReturnValue.next())
            {
                returnList.add(sqlGenreReturnValue.getString(2));
            }
            return returnList;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    public UserInterface getUser(String username, String password){
        try
        {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            stmt.setString(1,username);
            ResultSet sqlReturnValues = stmt.executeQuery();
            if(!sqlReturnValues.next())
            {
                return null;
            }
            if(hashPassword(password,sqlReturnValues.getString(4)).equals(sqlReturnValues.getString(3)))
            {
                stmt = connection.prepareStatement("SELECT * FROM roles WHERE id= ?");
                stmt.setInt(1,sqlReturnValues.getInt(6));
                ResultSet sqlRoleValues = stmt.executeQuery();
                if(!sqlRoleValues.next())
                {
                    return null;
                }
                return new UserData(sqlReturnValues.getString(2),sqlReturnValues.getString(3),sqlReturnValues.getString(4),sqlReturnValues.getString(5),sqlReturnValues.getString(7),java.time.LocalDate.parse(sqlReturnValues.getDate(8).toString()),sqlRoleValues.getString(2));
            }
            return null;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ProgramInterface getProgramFromID(int programID){
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM programs WHERE id = ?");
            stmt.setInt(1, programID);
            ResultSet sqlReturnValues = stmt.executeQuery();
            if (!sqlReturnValues.next()) {
                return null;
            }

           if (programID== sqlReturnValues.getInt(1)) {
                stmt = connection.prepareStatement("SELECT * FROM genres WHERE id= ?");
                stmt.setInt(1, sqlReturnValues.getInt(3));
                ResultSet sqlGenreValues = stmt.executeQuery();
                if (!sqlGenreValues.next()) {
                    return null;
                }
                return new ProgramData(sqlReturnValues.getString(2), sqlReturnValues.getString(4), LocalTime.parse(sqlReturnValues.getString(5)), sqlGenreValues.getString(2), sqlReturnValues.getString(6), sqlReturnValues.getInt(7));
           }
           return null;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ArrayList<ProgramInterface> getAllProgramsByCreatorId(int id)
    {
        try
        {
            ArrayList<ProgramInterface> returnList = new ArrayList<>();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM programs WHERE created = ?");
            stmt.setInt(1,id);
            ResultSet sqlReturnValues = stmt.executeQuery();
            while (sqlReturnValues.next())
            {
                stmt = connection.prepareStatement("SELECT * FROM genres WHERE id = ?");
                stmt.setInt(1,sqlReturnValues.getInt(3));
                ResultSet sqlGenreReturnValue = stmt.executeQuery();
                if(!sqlGenreReturnValue.next())
                {
                    return null;
                }
                returnList.add(new ProgramData(sqlReturnValues.getString(2), sqlReturnValues.getString(4), LocalTime.parse(sqlReturnValues.getString(5)), sqlGenreReturnValue.getString(2), sqlReturnValues.getString(6), sqlReturnValues.getInt(7)));
            }
            return returnList;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    public int getUserID(String username)
    {
        try
        {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            stmt.setString(1,username);
            ResultSet sqlReturnValue = stmt.executeQuery();
            if(!sqlReturnValue.next())
            {
                return -1;
            }
            return sqlReturnValue.getInt(1);
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return -1;
        }
    }

    public UserInterface getUser(String username){
        try
        {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            stmt.setString(1,username);
            ResultSet sqlReturnValues = stmt.executeQuery();
            if(!sqlReturnValues.next())
            {
                return null;
            }
            stmt = connection.prepareStatement("SELECT * FROM roles WHERE id= ?");
            stmt.setInt(1,sqlReturnValues.getInt(6));
            ResultSet sqlRoleValues = stmt.executeQuery();
            if(!sqlRoleValues.next())
            {
                return null;
            }
            return new UserData(sqlReturnValues.getString(2),sqlReturnValues.getString(3),sqlReturnValues.getString(4),sqlReturnValues.getString(5),sqlReturnValues.getString(7),java.time.LocalDate.parse(sqlReturnValues.getDate(8).toString()),sqlRoleValues.getString(2));
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean updateProgram(ProgramInterface program)
    {
        try
        {
            int genreId;
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM genres WHERE genre = ?");
            stmt.setString(1, program.getGenre());
            ResultSet sqlResultValue = stmt.executeQuery();
            if(!sqlResultValue.next())
            {
                return false;
            }
            genreId = sqlResultValue.getInt(1);
            stmt = connection.prepareStatement("UPDATE programs SET genre_id = ?,release_date = ?,length = ?, description = ? WHERE title = ?");
            stmt.setInt(1, genreId);
            java.util.Date date = new SimpleDateFormat("dd-MM-yyyy").parse(program.getReleaseDate());
            java.sql.Date sql = new java.sql.Date(date.getTime());
            stmt.setDate(2,sql);
            stmt.setString(3,program.getDuration().toString());
            stmt.setString(4,program.getDescription());
            stmt.setString(5,program.getName());
            stmt.execute();
            return true;
        }
        catch (SQLException | ParseException ex)
        {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean saveUser(UserInterface user){
        if (getUser(user.getUsername()) == null)
        {
            try
            {
                int roleId;
                PreparedStatement stmt = connection.prepareStatement("SELECT * FROM roles WHERE role = ?");
                stmt.setString(1,user.getRole());
                ResultSet sqlResultValue = stmt.executeQuery();
                if(!sqlResultValue.next())
                {
                    return false;
                }
                roleId = sqlResultValue.getInt(1);
                stmt = connection.prepareStatement("INSERT INTO users (username, password, salt, name, role_id, email, age, active) VALUES (?,?,?,?,?,?,?,?)");
                stmt.setString(1,user.getUsername());
                stmt.setString(2,user.getPassword());
                stmt.setString(3,user.getSalt());
                stmt.setString(4,user.getName());
                stmt.setInt(5,roleId);
                stmt.setString(6,user.getEmail());
                stmt.setDate(7,java.sql.Date.valueOf(user.getBirthday()));
                stmt.setBoolean(8,true);
                stmt.execute();
                return true;
            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
                return false;
            }
        }
        else {
            return false;
        }
    }

    public boolean saveProgram(ProgramInterface program)
    {
        if(getProgram(program.getName()) == null)
        {
            try
            {
                int genreId;
                PreparedStatement stmt = connection.prepareStatement("SELECT * FROM genres WHERE genre = ?");
                stmt.setString(1, program.getGenre());
                ResultSet sqlResultValue = stmt.executeQuery();
                if(!sqlResultValue.next())
                {
                    return false;
                }
                genreId = sqlResultValue.getInt(1);
                stmt = connection.prepareStatement("INSERT INTO programs (title,genre_id,release_date,length,description,created) VALUES (?,?,?,?,?,?)");
                stmt.setString(1,program.getName());
                stmt.setInt(2,genreId);
                java.util.Date date = new SimpleDateFormat("dd-MM-yyyy").parse(program.getReleaseDate());
                java.sql.Date sql = new java.sql.Date(date.getTime());
                stmt.setDate(3,sql);
                stmt.setString(4,program.getDuration().toString());
                stmt.setString(5,program.getDescription());
                stmt.setInt(6,program.getCreatorID());
                stmt.execute();
                return true;
            }
            catch (SQLException | ParseException ex)
            {
                ex.printStackTrace();
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    public boolean deleteCredit(CreditInterface credit)
    {
        try
        {
            PreparedStatement stmt;
            if(getOccupationId(credit.getOccupation()) == 36)
            {
                stmt = connection.prepareStatement("DELETE FROM movie_role WHERE credits_id = ?");
                stmt.setInt(1,getCreditId(credit));
                stmt.execute();
            }
            stmt = connection.prepareStatement("DELETE FROM credits WHERE id = ?");
            stmt.setInt(1,getCreditId(credit));
            stmt.execute();
            return true;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean deleteProgram(String programName)
    {
        try
        {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM programs WHERE title = ?");
            stmt.setString(1,programName);
            stmt.execute();
            return true;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean saveCredit(CreditInterface credit)
    {
        try
        {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO credits (program_id,person_id,occupation,created) VALUES (?,?,?,?)");
            stmt.setInt(1,getProgramId(credit.getProgram().getName()));
            stmt.setInt(2,getPersonId(credit.getPerson().getEmail()));
            stmt.setInt(3,getOccupationId(credit.getOccupation()));
            stmt.setInt(4,credit.getCreatorId());
            stmt.execute();
            if(getOccupationId(credit.getOccupation()) == 36)
            {
                stmt = connection.prepareStatement("SELECT * FROM credits WHERE program_id = ? AND person_id = ? AND occupation = ? AND created = ?");
                stmt.setInt(1,getProgramId(credit.getProgram().getName()));
                stmt.setInt(2,getPersonId(credit.getPerson().getEmail()));
                stmt.setInt(3,getOccupationId(credit.getOccupation()));
                stmt.setInt(4,credit.getCreatorId());
                ResultSet sqlReturnValue = stmt.executeQuery();
                if(!sqlReturnValue.next())
                {
                    return false;
                }
                stmt = connection.prepareStatement("INSERT INTO movie_role (credits_id, role_in_movie) VALUES (?,?)");
                stmt.setInt(1,sqlReturnValue.getInt(1));
                stmt.setString(2,credit.getCharacterName());
                stmt.execute();
                return true;
            }
            return true;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean doesCreditExist(CreditInterface credit)
    {
        try
        {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM credits WHERE program_id = ? AND person_id = ? AND occupation = ? AND created = ?");
            stmt.setInt(1,getProgramId(credit.getProgram().getName()));
            stmt.setInt(2,getPersonId(credit.getPerson().getEmail()));
            stmt.setInt(3,getOccupationId(credit.getOccupation()));
            stmt.setInt(4,credit.getCreatorId());
            ResultSet sqlReturnValue = stmt.executeQuery();
            return sqlReturnValue.next();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return false;
        }
    }

    public int getCreditId(CreditInterface credit)
    {
        try
        {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM credits WHERE program_id = ? AND person_id = ? AND occupation = ? AND created = ?");
            stmt.setInt(1,getProgramId(credit.getProgram().getName()));
            stmt.setInt(2,getPersonId(credit.getPerson().getEmail()));
            stmt.setInt(3,getOccupationId(credit.getOccupation()));
            stmt.setInt(4,credit.getCreatorId());
            ResultSet sqlReturnValue = stmt.executeQuery();
            if(!sqlReturnValue.next())
            {
                return -1;
            }
            return sqlReturnValue.getInt(1);
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return -1;
        }
    }

    public boolean doesProgramExist (String name)
    {
        if(getProgram(name) == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public int getOccupationId(String occupation)
    {
        try
        {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM occupation WHERE occupation = ?");
            stmt.setString(1,occupation);
            ResultSet sqlReturnValue = stmt.executeQuery();
            if(!sqlReturnValue.next())
            {
                return -1;
            }
            return sqlReturnValue.getInt(1);
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return -1;
        }
    }

    public int getPersonId(String personMail)
    {
        try
        {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM persons WHERE email = ?");
            stmt.setString(1,personMail);
            ResultSet sqlReturnValue = stmt.executeQuery();
            if(!sqlReturnValue.next())
            {
                return -1;
            }
            return sqlReturnValue.getInt(1);
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return -1;
        }
    }

    public ArrayList<ProgramInterface> getProgramBySearch(String name)
    {
        try
        {
            ArrayList<ProgramInterface> returnList = new ArrayList<>();

            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM programs WHERE position(? in title) > 0");
            stmt.setString(1, name);
            ResultSet sqlProgramReturnValue = stmt.executeQuery();
            while(sqlProgramReturnValue.next())
            {
                stmt = connection.prepareStatement("SELECT * FROM genres WHERE id = ?");
                stmt.setInt(1,sqlProgramReturnValue.getInt(3));
                ResultSet sqlGenreReturnValue = stmt.executeQuery();
                if(!sqlGenreReturnValue.next())
                {
                    return null;
                }
                returnList.add(new ProgramData(sqlProgramReturnValue.getString(2),sqlProgramReturnValue.getString(4),LocalTime.parse(sqlProgramReturnValue.getString(5)),sqlGenreReturnValue.getString(2),sqlProgramReturnValue.getString(6),sqlProgramReturnValue.getInt(7)));
            }
            return returnList;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    public ArrayList<ProgramInterface> getAllPrograms()
    {
        try
        {
            ArrayList<ProgramInterface> returnList = new ArrayList<>();

            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM programs");
            ResultSet sqlProgramReturnValue = stmt.executeQuery();
            while(sqlProgramReturnValue.next())
            {
                stmt = connection.prepareStatement("SELECT * FROM genres WHERE id = ?");
                stmt.setInt(1,sqlProgramReturnValue.getInt(3));
                ResultSet sqlGenreReturnValue = stmt.executeQuery();
                if(!sqlGenreReturnValue.next())
                {
                    return null;
                }
                returnList.add(new ProgramData(sqlProgramReturnValue.getString(2),sqlProgramReturnValue.getString(4),LocalTime.parse(sqlProgramReturnValue.getString(5)),sqlGenreReturnValue.getString(2),sqlProgramReturnValue.getString(6),sqlProgramReturnValue.getInt(7)));
            }
            return returnList;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    public ProgramInterface getProgram(String programTitle)
    {
        try
        {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM programs WHERE title = ?");
            stmt.setString(1,programTitle);
            ResultSet sqlProgramReturnValue = stmt.executeQuery();
            if(!sqlProgramReturnValue.next())
            {
                return null;
            }
            //Gets genre from the program
            stmt = connection.prepareStatement("SELECT * FROM genres WHERE id = ?");
            stmt.setInt(1,sqlProgramReturnValue.getInt(3));
            ResultSet sqlGenreReturnValue = stmt.executeQuery();
            if(!sqlGenreReturnValue.next())
            {
                return null;
            }
            return new ProgramData(sqlProgramReturnValue.getString(2),sqlProgramReturnValue.getString(4),LocalTime.parse(sqlProgramReturnValue.getString(5)),sqlGenreReturnValue.getString(2),sqlProgramReturnValue.getString(6),sqlProgramReturnValue.getInt(7));
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    public List<PersonInterface> getPersonsFromName(String searchParam) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM persons WHERE name LIKE CONCAT('%',?,'%')");
            //String search = searchParam + "%";
            stmt.setString(1, searchParam);
            ResultSet sqlReturnValues = stmt.executeQuery();
            List<PersonInterface> returnValue = new ArrayList<>();
            while(sqlReturnValues.next()) {
                LocalDate birthdate = java.time.LocalDate.parse(sqlReturnValues.getDate(3).toString());
                returnValue.add(new PersonData(birthdate, sqlReturnValues.getInt(1), sqlReturnValues.getString(4), sqlReturnValues.getString(2)));
            }
            return returnValue;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public int getProgramId(String programName)
    {
        try
        {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM programs WHERE title = ?");
            stmt.setString(1,programName);
            ResultSet sqlReturnValue = stmt.executeQuery();
            if(!sqlReturnValue.next())
            {
                return -1;
            }
            return sqlReturnValue.getInt(1);
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return -1;
        }
    }

    public String getCreditsFromProgramId(int id)
    {
        String returnString = "";

        try
        {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM credits WHERE program_id = ?");
            stmt.setInt(1,id);
            ResultSet sqlCreditsReturnValues = stmt.executeQuery();
            while (sqlCreditsReturnValues.next())
            {
                String stringToAdd = "";
                stmt = connection.prepareStatement("SELECT * FROM persons WHERE id = ?");
                stmt.setInt(1,sqlCreditsReturnValues.getInt(3));
                ResultSet sqlPersonReturnValue = stmt.executeQuery();
                if(!sqlPersonReturnValue.next())
                {
                    return null;
                }
                stringToAdd += sqlPersonReturnValue.getString(2) + ", ";
                stmt = connection.prepareStatement("SELECT * FROM occupation WHERE id = ?");
                stmt.setInt(1,sqlCreditsReturnValues.getInt(4));
                ResultSet sqlOccupationReturnValue = stmt.executeQuery();
                if(!sqlOccupationReturnValue.next())
                {
                    return null;
                }
                stringToAdd += sqlOccupationReturnValue.getString(2);
                if(sqlOccupationReturnValue.getInt(1) == 36)
                {
                    stmt = connection.prepareStatement("SELECT * FROM movie_role WHERE credits_id = ?");
                    stmt.setInt(1,sqlCreditsReturnValues.getInt(1));
                    ResultSet sqlMovieRoleReturnValue = stmt.executeQuery();
                    if(!sqlMovieRoleReturnValue.next())
                    {
                        return null;
                    }
                    stringToAdd += ", " + sqlMovieRoleReturnValue.getString(3);
                }
                stringToAdd += "\n";
                returnString += stringToAdd;
            }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return null;
        }

        return returnString;
    }

    public List<CreditInterface> getCreditFromID(int personID) {
        try
        {
            List<CreditInterface> returnValue = new ArrayList<>();

            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM credits WHERE person_id = ?");
            stmt.setInt(1,personID);
            ResultSet sqlCreditReturnValue = stmt.executeQuery();
            while(sqlCreditReturnValue.next())
            {
                //System.out.println("ID: " + sqlCreditReturnValue.getInt(1));
                //Gets program
                stmt = connection.prepareStatement("SELECT * FROM programs WHERE id = ?");
                stmt.setInt(1,sqlCreditReturnValue.getInt(2));
                ResultSet sqlProgramReturnValue = stmt.executeQuery();
                if(!sqlProgramReturnValue.next())
                {
                    return null;
                }
                //Gets genre from the program
                stmt = connection.prepareStatement("SELECT * FROM genres WHERE id = ?");
                stmt.setInt(1,sqlProgramReturnValue.getInt(3));
                ResultSet sqlGenreReturnValue = stmt.executeQuery();
                if(!sqlGenreReturnValue.next())
                {
                    return null;
                }
                ProgramData tempProgram = new ProgramData(sqlProgramReturnValue.getString(2),sqlProgramReturnValue.getString(4),LocalTime.parse(sqlProgramReturnValue.getString(5)),sqlGenreReturnValue.getString(2),sqlProgramReturnValue.getString(6),sqlProgramReturnValue.getInt(7));

                //Gets person
                stmt = connection.prepareStatement("SELECT * FROM persons WHERE id = ?");
                stmt.setInt(1,sqlCreditReturnValue.getInt(3));
                ResultSet sqlPersonReturnValue = stmt.executeQuery();
                if(!sqlPersonReturnValue.next())
                {
                    return null;
                }
                LocalDate birthdate = java.time.LocalDate.parse(sqlPersonReturnValue.getDate(3).toString());
                PersonData tempPerson = new PersonData(birthdate, sqlPersonReturnValue.getInt(1),sqlPersonReturnValue.getString(4),sqlPersonReturnValue.getString(2));

                //Gets occupation
                stmt = connection.prepareStatement("SELECT * FROM occupation where id = ?");
                stmt.setInt(1,sqlCreditReturnValue.getInt(4));
                ResultSet sqlOccupationReturnValue = stmt.executeQuery();
                if(!sqlOccupationReturnValue.next())
                {
                    return null;
                }
                String tempOccupation = sqlOccupationReturnValue.getString(2);

                int tempCreatedID = sqlCreditReturnValue.getInt(5);

                if(sqlOccupationReturnValue.getInt(1) == 36)
                {
                    String tempCharacterName = getMovieRole(sqlCreditReturnValue.getInt(1));
                    returnValue.add(new CreditData(tempPerson,tempProgram,tempOccupation,tempCharacterName,tempCreatedID));
                }
                else
                {
                    returnValue.add(new CreditData(tempPerson,tempProgram,tempOccupation,tempCreatedID));
                }
            }

            return returnValue;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String getMovieRole(int creditID) {
        try {
            String role;
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM movie_role WHERE credits_id = ?");
            stmt.setInt(1, creditID);
            ResultSet sqlReturnValues = stmt.executeQuery();
            if (!sqlReturnValues.next()) {
                return null;
            }
            role = sqlReturnValues.getString(3);
            return role;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }


    //</editor-fold desc="Methods with SQL Implementation">

    //<editor-fold desc="Methods missing SQL Implementation">


    public ArrayList<ProgramInterface> getProgram() {
        //readlines and put em in a list
        //ArrayList<ProgramInterface> searchItems = new ArrayList<>();    hvad gør den her linje???
        ArrayList<String> dataValues = null;
        try {
            dataValues = readDataValues("programs.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<ProgramInterface> programs = new ArrayList<>();

        /*for (int i = 0; i < dataValues.size(); i += 7) {
            //Year of release
            Calendar calendar = Calendar.getInstance();
            String[] calendarValues = dataValues.get(3 + i).split(",");
            calendar.set(Integer.parseInt(calendarValues[0]), Integer.parseInt(calendarValues[1]), Integer.parseInt(calendarValues[2]));
            Date date = new Date();
            date = Calendar.getInstance().getTime();
            //Watch length
            // Simon, hvorfor skulle den split på komma og ikke kolon?
            String[] splittetTime = (dataValues.get(4 + i).split(":"));
            LocalTime time = LocalTime.of(Integer.parseInt(splittetTime[0]), Integer.parseInt(splittetTime[1]), Integer.parseInt(splittetTime[2]));

            //Genre
            ArrayList<GenreData> genres = new ArrayList<>();
            String[] splittetGenres = dataValues.get(2+i).split(",");
            for (int j = 0; j < splittetGenres.length; j++){
                genres.add(GenreData.valueOf(splittetGenres[j]));
            }

            ProgramData program = new ProgramData(Integer.parseInt(dataValues.get(0 + i)), dataValues.get(1 + i), dataValues.get(3+i), null, time, dataValues.get(2+i), dataValues.get(5 + i), Integer.parseInt(dataValues.get(6 + i)));

            //credits
            Scanner reader = null;
            try {
                reader = new Scanner(new File("credit.txt"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            while (reader.hasNextLine()){
                int programID = Integer.parseInt(reader.nextLine().split(";")[2]);
                if (Integer.parseInt(dataValues.get(0 + i)) == programID) {
                    for (CreditInterface credit : (getCredits(programID))){
                        program.addCredit(credit);
                    }
                }
            }
            programs.add(program);
        }*/
        return programs;
    }

    //denne metode vil ikke kunne virke hvis den ikke kan få fat på creditteringerne for et program
    /*public ArrayList<ProgramInterface> getProgram(PersonInterface person) throws Exception {
        throw new Exception("Mangler en getCreditsmetode i Program før denne metode kan blive implementeret -2 Ris");

        ArrayList<ProgramInterface> programs = getProgram();
        ArrayList<CreditInterface> credits = getCredits();
        ArrayList<ProgramInterface> returnPrograms = new ArrayList<>();
        for (ProgramInterface program : programs){
            for (CreditInterface credit : credits){
                if (credit.getPerson().equals(person)) if (program.);
            }
        }

        return returnPrograms;

    }*/

    /*public ProgramInterface getProgram(GenreInterface genre) throws Exception {
        ArrayList<ProgramInterface> programs = getProgram();
        for (ProgramInterface program : programs) {
            for (GenreInterface genre_ : program.getGenre()) {
                if (genre_.equals(genre)) return program;
            }
        }
        return null;
    }*/

    /*public ArrayList<ProgramInterface> getProgram (String title){
        ArrayList<ProgramInterface> programs = getProgram();
        //Sort em
        Collections.sort(programs, Comparator.comparing(ProgramInterface::getName));

        ArrayList<ProgramInterface> sortedPrograms = new ArrayList<>();

        for (ProgramInterface program : programs) {
            String splitValues = SplitByChar(program.getName(), title.length());
            if (splitValues.equals(title)) sortedPrograms.add(program);
        }
        return sortedPrograms;
    }*/

    public boolean SaveProgram (ProgramInterface program){
        try {
            int ID = -1;

            //getID
            Scanner reader = new Scanner(new File("programs.txt"));
            while (reader.hasNextLine()) {
                String read = reader.nextLine();
                String[] readValues = read.split(";");
                ID = Integer.parseInt(readValues[0]);
            }
            ID += 1;
            reader.close();

            FileWriter writer = new FileWriter(new File("programs.txt"), true);

            /*String genres = "";
            for (GenreInterface genre : program.getGenre()) {
                genres += genre + ",";
            }*/
            String genres = program.getGenre();

            writer.write(ID + ";" + program.getName() + ";" + genres + ";" + program.getReleaseDate() + ";" + program.getDuration() + ";" + program.getDescription() + ";" + program.getCreatorID() + "\n");
            writer.close();
            return true;
        } catch (IOException e) {
            System.out.println(e);
            return false;
        }
    }

    /*public boolean SaveProgram (ProgramInterface program,int ID){
        try {
            FileWriter writer = new FileWriter(new File("programs.txt"), true);
            String genres = "";
            for (GenreInterface genre : program.getGenre()) {
                if (program.getGenre().get(program.getGenre().size()).equals(genre)) {
                    genres += genre;
                    break;
                }
                genres += genre + ",";
            }
            writer.write(ID + ";" + program.getName() + ";" + genres + ";" + program.getReleaseDate() + ";" + program.getDuration() + ";" + program.getDescription() + ";" + program.getCreatorID() + "\n");
            writer.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }*/

    //denne her metode er for at få fat på alle credits uden at skulle have nogen kritiriere
    /*public ArrayList<CreditInterface> getCredits () throws Exception {
        ArrayList<CreditInterface> credits = new ArrayList<>();

        Scanner reader = new Scanner(new File("credits.txt"));
        //stjal lige noget kode, det ville sku være hurtigere
        while (reader.hasNextLine()) {
            String read = reader.nextLine();
            String[] readSplit = read.split(";");
            CreditData credit;
            if (readSplit.length == 3) {
                credit = new CreditData(readSplit[1], (PersonData) getPerson(Integer.parseInt(readSplit[0])));
            } else {
                credit = new CreditData(readSplit[1], (PersonData) getPerson(Integer.parseInt(readSplit[0])), readSplit[2]);
            }
            credits.add(credit);
        }
        return credits;
    }*/

    /*public ArrayList<CreditInterface> getCredits ( int programID){
        ArrayList<CreditInterface> credits = new ArrayList<>();

        try {
            Scanner reader = new Scanner(new File("credit.txt"));

            while (reader.hasNextLine()) {
                String read = reader.nextLine();
                String[] readSplit = read.split(";");
                //hop til næste itteration hvis credittet ikke har rigtig ID

                if (readSplit.length == 3) {
                    if (Integer.parseInt(readSplit[2]) != programID) continue;
                } else {
                    if (Integer.parseInt(readSplit[2]) != programID) continue;
                }

                CreditData credit;
                //if (readSplit.length == 3) {
                //credit = new CreditData(OccupationData.valueOf(readSplit[1]), (PersonData) getPerson(Integer.parseInt(readSplit[0])));
                //}
                //else {
                credit = new CreditData(readSplit[1], (PersonData) getPerson(Integer.parseInt(readSplit[0])), readSplit[3]);
                //}
                credits.add(credit);
            }
            return credits;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }*/

    /*public ArrayList<CreditInterface> getCredits (String programTitle) throws Exception {
        //rettet i det efter jeg rettede i saveCredits
        ArrayList<CreditInterface> credits = new ArrayList<>();
        //get programID
        int programID = getProgram(programTitle).get(0).getId();
        System.out.println(programID);

        try {
            Scanner reader = new Scanner(new File("credits.txt"));

    while (reader.hasNextLine()){
        String read = reader.nextLine();
        String[] readSplit = read.split(";");
        //hop til næste itteration hvis credittet ikke har rigtig ID

        if (readSplit.length == 3){
            if (Integer.parseInt(readSplit[2]) != programID) continue;
        }
        else {
            if (Integer.parseInt(readSplit[3]) != programID) continue;
        }

        CreditData credit;
        if (readSplit.length == 3) {
            credit = new CreditData(readSplit[1], (PersonData) getPerson(Integer.parseInt(readSplit[0])));
        }
        else {
            credit = new CreditData(readSplit[1], (PersonData) getPerson(Integer.parseInt(readSplit[0])), readSplit[2]);
        }
        credits.add(credit);
    }
            return credits;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }*/

    public boolean saveCredits (CreditInterface credit,int programID){
        try {
            //ved ikke hvorfor den skulle lave en ny fil hvergang
            //har tilføjet programID sådan at vi altid kan finde tilbage til det program det var lavet til
            //gjort sådan at den fortæller om det lykkedes for den at gemme credits
            FileWriter writer = new FileWriter(new File("credit.txt"), true);
            if (!credit.getCharacterName().equals("N/A")) {
                writer.write(credit.getPerson().getId() + ";" + credit.getOccupation().toString() + ";" + programID + ";" + credit.getCharacterName() + "\n");
                writer.close();
            } else {
                writer.write(credit.getPerson().getId() + ";" + credit.getOccupation().toString() + ";" + programID + ";" + "N/A" + "\n");
                writer.close();
            }
        } catch (IOException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    /*public ArrayList<CreditInterface> getAllCredits () throws Exception {
        ArrayList<CreditInterface> credits = new ArrayList<>();
        ArrayList<String> readValues = new ArrayList<>();
        try (Scanner reader = new Scanner(new File("credits.txt"))) {
            while (reader.hasNextLine()) {
                readValues.add(reader.nextLine());
            }
            for (String element : readValues) {
                String[] valuesToUse = element.split(";");
                credits.add(new CreditData(valuesToUse[4], new PersonData(Integer.parseInt(valuesToUse[0]), Integer.parseInt(valuesToUse[1]), valuesToUse[2], valuesToUse[3]), valuesToUse[5]));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return credits;
    }*/

    //Lavet af Sigster
    public ArrayList<String> getAllCreditsFromCreditFile ( int personID){
        ArrayList<String> readValues = new ArrayList<>();
        //List<ArrayList<String>> credits = new ArrayList<>();
        ArrayList<String> credit = new ArrayList<>();
        try (Scanner reader = new Scanner(new File("credit.txt"))) {
            while (reader.hasNextLine()) {
                readValues.add(reader.nextLine());
            }
            for (String element : readValues) {
                String[] valuesToUse = element.split(";");
                if (String.valueOf(personID).equals(valuesToUse[0])) {
                    credit.add(valuesToUse[0] + ";" + valuesToUse[1] + ";" + valuesToUse[2] + ";" + valuesToUse[3]);
                    //credits.add(credit);
                    //Evt. lav ovenstående om til fori loop og brug samme index (i) til at sætte ind på credits ?
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return credit;
    }

    //Lavet af Sigster
    /*public ArrayList<String> getProgramFromID(String programID) {
        ArrayList<String> readValues = new ArrayList<>();
        ArrayList<String> program = new ArrayList<>();
        try(Scanner reader = new Scanner(new File("programs.txt"))) {
            while (reader.hasNextLine()) {
                readValues.add(reader.nextLine());
            }
            for(String element: readValues) {
                String[] valuesToUse = element.split(";");
                if(String.valueOf(programID).equals(valuesToUse[0])) {
                    program.add(valuesToUse[0] + ";" + valuesToUse[1] + ";" + valuesToUse[2]
                            + ";" + valuesToUse[3] + ";" + valuesToUse[4] + ";" + valuesToUse[5] + ";" + valuesToUse[6]);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return program;
    }*/

    /*public UserInterface getUser(String username, String password) {
        try (Scanner reader = new Scanner(new File("usernames.txt")))
        {
            //har haft nogle problemer med at få pathen til filen til at fungere
            while (reader.hasNext()){
                String[] userInfo = reader.nextLine().split(";");
                if (userInfo[0].equals(username)){
                    if (userInfo[1].equals(password)){
                        return new UserData(userInfo[2], userInfo[0], userInfo[1], Integer.parseInt(userInfo[5]), userInfo[4], userInfo[3]) {
                        };
                    }
                }
            }
        }
        catch (FileNotFoundException e){
            System.out.println(e);
        }
        return null;
    }*/



    /*public UserInterface getUser(String username) {
        try (Scanner reader = new Scanner(new File("usernames.txt")))
        {
            while (reader.hasNext()){
                String[] userInfo = reader.nextLine().split(";");
                if (userInfo[0].equals(username)){
                    return new UserData(userInfo[2], userInfo[0], userInfo[1], Integer.parseInt(userInfo[5]), userInfo[4], userInfo[3]);
                }
            }
        }
        catch (FileNotFoundException e){
            System.out.println(e);
        }
        return null;
    }*/

    //returns if the username already existed
    //true == user didn't exist and adding the new user was a succes
    //false == user already exist
    /*public boolean SaveUser(UserInterface user) throws IOException {
        if (getUser(user.getUsername()) == null){
            FileWriter writer = new FileWriter("usernames.txt", true);
            writer.write( user.getUsername() + ";" + user.getPassword() + ";" + user.getName() + ";" + user.getRole() + ";" + user.getEmail() + ";" + user.getAge() + "\n" );
            writer.close();
            return true;
        }
        else {
            return false;
        }
    }*/

    public ArrayList<UserInterface> getAllUsers ()
    {
        ArrayList<UserInterface> returnList = new ArrayList<>();
        ArrayList<String> readValues = new ArrayList<>();
        try (Scanner reader = new Scanner(new File("usernames.txt"))) {
            while (reader.hasNextLine()) {
                readValues.add(reader.nextLine());
            }
            for (String element : readValues) {
                String[] valuesToUse = element.split(";");
                //returnList.add(new UserData(valuesToUse[2],valuesToUse[0], valuesToUse[1],Integer.parseInt(valuesToUse[5]), valuesToUse[4], valuesToUse[3]));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return returnList;
    }

    //FIX ME, få den til at returnere en Person og ændre navnet
    public String Search ( int id) throws Exception {
        try {
            ArrayList<String> readLines = new ArrayList<>();
            Scanner reader = new Scanner(new File("persons.txt"));
            while (reader.hasNextLine()) {
                readLines.add(reader.nextLine());
            }
        } catch (Exception e) {
            throw new Exception();
        }
        return "";
    }

    public void SavePerson (PersonInterface person) throws IOException {
        Scanner reader = new Scanner(new File("persons.txt"));
        if (person.getId() == -1) {
            int ID = 0;
            while (reader.hasNextLine()) {
                ID = Integer.parseInt(reader.nextLine().split(";")[0]) + 1;
            }
            reader.close();
            FileWriter writer = new FileWriter("persons.txt", true);
            writer.write("\n" + ID + ";" + person.getName() + ";" + person.getAge() + ";" + person.getEmail());
            writer.close();
        } else {
            FileWriter writer = new FileWriter("persons.txt", true);
            writer.write("\n" + person.getId() + ";" + person.getName() + ";" + person.getAge() + ";" + person.getEmail());
            writer.close();
        }
    }

    public PersonInterface getPersonFromID (int id){
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM persons WHERE id = ?");
            stmt.setInt(1, id);
            ResultSet sqlReturnValues = stmt.executeQuery();
            if (!sqlReturnValues.next()) {
                return null;
            }
            LocalDate birthdate = java.time.LocalDate.parse(sqlReturnValues.getDate(3).toString());
            return new PersonData(birthdate, sqlReturnValues.getInt(1), sqlReturnValues.getString(4), sqlReturnValues.getString(2));

        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean doesPersonExist (String email){
        try {
            Scanner reader = new Scanner(new File("persons.txt"));
            while (reader.hasNextLine()) {
                String read = reader.nextLine();
                String[] readSplit = read.split(";");

                if (readSplit[3].equals(email)) {
                    System.out.println(readSplit);
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /*public boolean doesProgramExist (String name){
        try {
            Scanner reader = new Scanner(new File("programs.txt"));
            while (reader.hasNextLine()) {
                String read = reader.nextLine();
                String[] readSplit = read.split(";");
                if (readSplit[1].equals(name)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }*/

    //SearchParam uses letters to narrow the search result down, type is the searched var in object, file is the file the data is in
    /*public ArrayList<PersonInterface> SearchPerson (String searchParam) throws Exception {
        //readlines and put em in a list
        String splitValue = "";
        searchParam = searchParam.toLowerCase();
        ArrayList<PersonInterface> searchItems = new ArrayList<>();
        ArrayList<PersonInterface> dataValues = getAllPersons();

        //throw out all of the lines which is not searched for
        for (int i = 0; i < dataValues.size(); i++) {
            if (dataValues.get(i).getName().length() >= searchParam.length()) {
                splitValue = SplitByChar(dataValues.get(i).getName(), searchParam.length());
                splitValue = splitValue.toLowerCase();

                if (splitValue.equals(searchParam)) {
                    searchItems.add(dataValues.get(i));
                    //System.out.println(dataValues.get(i).getName());
                }
            }
        }
        //sort em
        Collections.sort(searchItems, Comparator.comparing(PersonInterface::getName));
        return searchItems;
    }

    //An unsorted and quicker way to get all persons, mostly used by Datasystem itself but can be used outside
    public ArrayList<PersonInterface> getAllPersons () throws Exception {
        ArrayList<PersonInterface> persons = new ArrayList<>();
        ArrayList<String> personValues = readDataValues("persons.txt");
        for (int i = 0; i < personValues.size(); i = i + 4) {
            persons.add(new PersonData(Integer.parseInt(personValues.get(i + 2)), Integer.parseInt(personValues.get(i + 0)), personValues.get(i + 3), personValues.get(i + 1)));
        }
        return persons;
    }*/

    public ArrayList<PersonInterface> getAllPersonsByCreatorId(int id)
    {
        ArrayList<PersonInterface> returnList = new ArrayList<>();

        try
        {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM persons WHERE created = ?");
            stmt.setInt(1,id);
            ResultSet sqlReturnValues = stmt.executeQuery();
            while(sqlReturnValues.next())
            {
                LocalDate birthdate = java.time.LocalDate.parse(sqlReturnValues.getDate(3).toString());
                returnList.add(new PersonData(birthdate,sqlReturnValues.getString(4),sqlReturnValues.getString(2)));
            }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return null;
        }

        return returnList;
    }


    private String SplitByChar (String text,int splitBy){
        return text.substring(0, splitBy);
    }

    private ArrayList<String> readDataValues (String path) throws FileNotFoundException {
        ArrayList<String> readLines = new ArrayList<>();
        ArrayList<String> returnLines = new ArrayList<>();
        try {
            Scanner reader = new Scanner(new File(path));
            while (reader.hasNextLine()) {
                readLines.add(reader.nextLine());
            }
            for (String readLine : readLines) {
                String[] lines = readLine.split(";");
                for (String line : lines) {
                    returnLines.add(line);
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return returnLines;
    }

    private void updateUsers (ArrayList < UserInterface > users)
    {
        try (FileWriter writer = new FileWriter(new File("usernames.txt"))) {
            for (UserInterface user : users) {
                writer.write(user.getUsername() + ";" + user.getPassword() + ";" + user.getName() + ";" + user.getRole() + ";" + user.getEmail() + ";" + user.getAge() + "\n");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void updateUserPerms(UserInterface user)
    {
        ArrayList<UserInterface> tempUserArray = getAllUsers();
        ArrayList<UserInterface> updateUserArray = new ArrayList<>();

        for (UserInterface element: tempUserArray)
        {
            if(element.getUsername().equals(user.getUsername()))
            {
                updateUserArray.add(user);
            }
            else
            {
                updateUserArray.add(element);
            }
        }

        updateUsers(updateUserArray);
    }

    public void deleteUser(UserData user, String reason)
    {
        ArrayList<UserInterface> tempUserArray = getAllUsers();
        ArrayList<UserInterface> updateUserArray = new ArrayList<>();

        try(FileWriter writer = new FileWriter(new File("deletedUsers.txt"),true))
        {
            writer.write("User: " + user.getUsername() + ";Deleted Reason: " + reason + ";\n");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        for (UserInterface element: tempUserArray)
        {
            if(!element.getUsername().equals(user.getUsername()))
            {
                updateUserArray.add(element);
            }
        }
        updateUsers(updateUserArray);
    }

    public ArrayList<UserInterface> getAllUsersExceptXUser(UserInterface user)
    {
        ArrayList<UserInterface> tempUserArray = getAllUsers();
        ArrayList<UserInterface> returnArray = new ArrayList<>();

        for (UserInterface element: tempUserArray)
        {
            if(!element.getUsername().equals(user.getUsername()))
            {
                returnArray.add(element);
            }
        }
        return returnArray;
    }

    //</editor-fold desc="Methods missing SQL Implementation">

    public String hashPassword(String password, String salt) {
        String returnHash = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest((password + salt).getBytes(StandardCharsets.UTF_8));
            BigInteger bigInteger = new BigInteger(1, bytes);
            returnHash = bigInteger.toString(16);
            while (returnHash.length() < 32) {
                returnHash = "0" + returnHash;
            }
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return returnHash;
    }
}

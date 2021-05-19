package Persistence;

import Interfaces.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class DatabaseSystem
{
    private static DatabaseSystem instance;
    private String url = "217.61.218.112";
    private int port = 5432;
    private String databaseName = "TV2ProjectC13";
    private String username = "TV2Project";
    private String password = "1234";
    private Connection connection = null;

    public DatabaseSystem() {
        initializePostgresqlDatabase();
    }

    public static synchronized Persistence.DatabaseSystem getInstance() {
        if (instance == null) {
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
    public ArrayList<String> getAllOccupations() {
        ArrayList<String> returnList = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM occupation");
            ResultSet sqlOccupationReturnValue = stmt.executeQuery();
            while (sqlOccupationReturnValue.next()) {
                returnList.add(sqlOccupationReturnValue.getString(2));
            }
            return returnList;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> getAllGenres() {
        ArrayList<String> returnList = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM genres");
            ResultSet sqlGenreReturnValue = stmt.executeQuery();
            while (sqlGenreReturnValue.next()) {
                returnList.add(sqlGenreReturnValue.getString(2));
            }
            return returnList;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<UserInterface> getViewUser() {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT us.id, us.name, ro.role, us.email FROM users us JOIN roles ro ON us.role_id = ro.id ORDER BY name;");
            ResultSet rs = stmt.executeQuery();
            List<UserInterface> returnValue = new ArrayList<>();
            while (rs.next()) {
                returnValue.add(new UserData(rs.getInt("id"), rs.getString("name"), rs.getString("role"), rs.getString("email")));
            }
            return returnValue;
        } catch (SQLException ex) {
            return null;
        }
    }

    public UserInterface getUser(String username, String password) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            stmt.setString(1, username);
            ResultSet sqlReturnValues = stmt.executeQuery();
            if (!sqlReturnValues.next()) {
                return null;
            }
            if (hashPassword(password, sqlReturnValues.getString(4)).equals(sqlReturnValues.getString(3))) {
                stmt = connection.prepareStatement("SELECT * FROM roles WHERE id= ?");
                stmt.setInt(1, sqlReturnValues.getInt(6));
                ResultSet sqlRoleValues = stmt.executeQuery();
                if (!sqlRoleValues.next()) {
                    return null;
                }
                return new UserData(sqlReturnValues.getString(2), sqlReturnValues.getString(3), sqlReturnValues.getString(4), sqlReturnValues.getString(5), sqlReturnValues.getString(7), java.time.LocalDate.parse(sqlReturnValues.getDate(8).toString()), sqlRoleValues.getString(2));
            }
            return null;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ProgramInterface getProgramFromID(int programID) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM programs WHERE id = ?");
            stmt.setInt(1, programID);
            ResultSet sqlReturnValues = stmt.executeQuery();
            if (!sqlReturnValues.next()) {
                return null;
            }

            if (programID == sqlReturnValues.getInt(1)) {
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

    public ArrayList<ProgramInterface> getAllProgramsByCreatorId(int id) {
        try {
            ArrayList<ProgramInterface> returnList = new ArrayList<>();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM programs WHERE created = ?");
            stmt.setInt(1, id);
            ResultSet sqlReturnValues = stmt.executeQuery();
            while (sqlReturnValues.next()) {
                stmt = connection.prepareStatement("SELECT * FROM genres WHERE id = ?");
                stmt.setInt(1, sqlReturnValues.getInt(3));
                ResultSet sqlGenreReturnValue = stmt.executeQuery();
                if (!sqlGenreReturnValue.next()) {
                    return null;
                }
                returnList.add(new ProgramData(sqlReturnValues.getString(2), sqlReturnValues.getString(4), LocalTime.parse(sqlReturnValues.getString(5)), sqlGenreReturnValue.getString(2), sqlReturnValues.getString(6), sqlReturnValues.getInt(7)));
            }
            return returnList;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public int getUserID(String username) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            stmt.setString(1, username);
            ResultSet sqlReturnValue = stmt.executeQuery();
            if (!sqlReturnValue.next()) {
                return -1;
            }
            return sqlReturnValue.getInt(1);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public UserInterface getUser(String username) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            stmt.setString(1, username);
            ResultSet sqlReturnValues = stmt.executeQuery();
            if (!sqlReturnValues.next()) {
                return null;
            }
            stmt = connection.prepareStatement("SELECT * FROM roles WHERE id= ?");
            stmt.setInt(1, sqlReturnValues.getInt(6));
            ResultSet sqlRoleValues = stmt.executeQuery();
            if (!sqlRoleValues.next()) {
                return null;
            }
            return new UserData(sqlReturnValues.getString(2), sqlReturnValues.getString(3), sqlReturnValues.getString(4), sqlReturnValues.getString(5), sqlReturnValues.getString(7), java.time.LocalDate.parse(sqlReturnValues.getDate(8).toString()), sqlRoleValues.getString(2));
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean updateProgram(ProgramInterface program) {
        try {
            int genreId;
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM genres WHERE genre = ?");
            stmt.setString(1, program.getGenre());
            ResultSet sqlResultValue = stmt.executeQuery();
            if (!sqlResultValue.next()) {
                return false;
            }
            genreId = sqlResultValue.getInt(1);
            stmt = connection.prepareStatement("UPDATE programs SET genre_id = ?,release_date = ?,length = ?, description = ? WHERE title = ?");
            stmt.setInt(1, genreId);
            java.util.Date date = new SimpleDateFormat("dd-MM-yyyy").parse(program.getReleaseDate());
            java.sql.Date sql = new java.sql.Date(date.getTime());
            stmt.setDate(2, sql);
            stmt.setString(3, program.getDuration().toString());
            stmt.setString(4, program.getDescription());
            stmt.setString(5, program.getName());
            stmt.execute();
            return true;
        } catch (SQLException | ParseException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean updateUserRole(int id, int rolleid) {
        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE users SET role_id = ? WHERE id = ?");
            stmt.setInt(1, rolleid);
            stmt.setInt(2, id);
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean saveUser(UserInterface user) {
        if (getUser(user.getUsername()) == null) {
            try {
                int roleId;
                PreparedStatement stmt = connection.prepareStatement("SELECT * FROM roles WHERE role = ?");
                stmt.setString(1, user.getRole());
                ResultSet sqlResultValue = stmt.executeQuery();
                if (!sqlResultValue.next()) {
                    return false;
                }
                roleId = sqlResultValue.getInt(1);
                stmt = connection.prepareStatement("INSERT INTO users (username, password, salt, name, role_id, email, age, active) VALUES (?,?,?,?,?,?,?,?)");
                stmt.setString(1, user.getUsername());
                stmt.setString(2, user.getPassword());
                stmt.setString(3, user.getSalt());
                stmt.setString(4, user.getName());
                stmt.setInt(5, roleId);
                stmt.setString(6, user.getEmail());
                stmt.setDate(7, java.sql.Date.valueOf(user.getBirthday()));
                stmt.setBoolean(8, true);
                stmt.execute();
                return true;
            } catch (SQLException ex) {
                ex.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean saveProgram(ProgramInterface program) {
        if (getProgram(program.getName()) == null) {
            try {
                int genreId;
                PreparedStatement stmt = connection.prepareStatement("SELECT * FROM genres WHERE genre = ?");
                stmt.setString(1, program.getGenre());
                ResultSet sqlResultValue = stmt.executeQuery();
                if (!sqlResultValue.next()) {
                    return false;
                }
                genreId = sqlResultValue.getInt(1);
                stmt = connection.prepareStatement("INSERT INTO programs (title,genre_id,release_date,length,description,created) VALUES (?,?,?,?,?,?)");
                stmt.setString(1, program.getName());
                stmt.setInt(2, genreId);
                java.util.Date date = new SimpleDateFormat("dd-MM-yyyy").parse(program.getReleaseDate());
                java.sql.Date sql = new java.sql.Date(date.getTime());
                stmt.setDate(3, sql);
                stmt.setString(4, program.getDuration().toString());
                stmt.setString(5, program.getDescription());
                stmt.setInt(6, program.getCreatorID());
                stmt.execute();
                return true;
            } catch (SQLException | ParseException ex) {
                ex.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean deleteCredit(CreditInterface credit) {
        try {
            PreparedStatement stmt;
            if (getOccupationId(credit.getOccupation()) == 36) {
                stmt = connection.prepareStatement("DELETE FROM movie_role WHERE credits_id = ?");
                stmt.setInt(1, getCreditId(credit));
                stmt.execute();
            }
            stmt = connection.prepareStatement("DELETE FROM credits WHERE id = ?");
            stmt.setInt(1, getCreditId(credit));
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean deleteProgram(String programName) {
        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM programs WHERE title = ?");
            stmt.setString(1, programName);
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean saveCredit(CreditInterface credit) {
        try {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO credits (program_id,person_id,occupation,created) VALUES (?,?,?,?)");
            stmt.setInt(1, getProgramId(credit.getProgram().getName()));
            stmt.setInt(2, getPersonId(credit.getPerson().getEmail()));
            stmt.setInt(3, getOccupationId(credit.getOccupation()));
            stmt.setInt(4, credit.getCreatorId());
            stmt.execute();
            if (getOccupationId(credit.getOccupation()) == 36) {
                stmt = connection.prepareStatement("SELECT * FROM credits WHERE program_id = ? AND person_id = ? AND occupation = ? AND created = ?");
                stmt.setInt(1, getProgramId(credit.getProgram().getName()));
                stmt.setInt(2, getPersonId(credit.getPerson().getEmail()));
                stmt.setInt(3, getOccupationId(credit.getOccupation()));
                stmt.setInt(4, credit.getCreatorId());
                ResultSet sqlReturnValue = stmt.executeQuery();
                if (!sqlReturnValue.next()) {
                    return false;
                }
                stmt = connection.prepareStatement("INSERT INTO movie_role (credits_id, role_in_movie) VALUES (?,?)");
                stmt.setInt(1, sqlReturnValue.getInt(1));
                stmt.setString(2, credit.getCharacterName());
                stmt.execute();
                return true;
            }
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean doesCreditExist(CreditInterface credit) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM credits WHERE program_id = ? AND person_id = ? AND occupation = ? AND created = ?");
            stmt.setInt(1, getProgramId(credit.getProgram().getName()));
            stmt.setInt(2, getPersonId(credit.getPerson().getEmail()));
            stmt.setInt(3, getOccupationId(credit.getOccupation()));
            stmt.setInt(4, credit.getCreatorId());
            ResultSet sqlReturnValue = stmt.executeQuery();
            return sqlReturnValue.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public int getCreditId(CreditInterface credit) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM credits WHERE program_id = ? AND person_id = ? AND occupation = ? AND created = ?");
            stmt.setInt(1, getProgramId(credit.getProgram().getName()));
            stmt.setInt(2, getPersonId(credit.getPerson().getEmail()));
            stmt.setInt(3, getOccupationId(credit.getOccupation()));
            stmt.setInt(4, credit.getCreatorId());
            ResultSet sqlReturnValue = stmt.executeQuery();
            if (!sqlReturnValue.next()) {
                return -1;
            }
            return sqlReturnValue.getInt(1);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public boolean doesProgramExist(String name) {
        if (getProgram(name) == null) {
            return false;
        } else {
            return true;
        }
    }

    public int getOccupationId(String occupation) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM occupation WHERE occupation = ?");
            stmt.setString(1, occupation);
            ResultSet sqlReturnValue = stmt.executeQuery();
            if (!sqlReturnValue.next()) {
                return -1;
            }
            return sqlReturnValue.getInt(1);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public int getPersonId(String personMail) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM persons WHERE email = ?");
            stmt.setString(1, personMail);
            ResultSet sqlReturnValue = stmt.executeQuery();
            if (!sqlReturnValue.next()) {
                return -1;
            }
            return sqlReturnValue.getInt(1);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public ArrayList<ProgramInterface> getProgramBySearch(String name) {
        try {
            ArrayList<ProgramInterface> returnList = new ArrayList<>();

            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM programs WHERE position(? in title) > 0");
            stmt.setString(1, name);
            ResultSet sqlProgramReturnValue = stmt.executeQuery();
            while (sqlProgramReturnValue.next()) {
                stmt = connection.prepareStatement("SELECT * FROM genres WHERE id = ?");
                stmt.setInt(1, sqlProgramReturnValue.getInt(3));
                ResultSet sqlGenreReturnValue = stmt.executeQuery();
                if (!sqlGenreReturnValue.next()) {
                    return null;
                }
                returnList.add(new ProgramData(sqlProgramReturnValue.getString(2), sqlProgramReturnValue.getString(4), LocalTime.parse(sqlProgramReturnValue.getString(5)), sqlGenreReturnValue.getString(2), sqlProgramReturnValue.getString(6), sqlProgramReturnValue.getInt(7)));
            }
            return returnList;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ArrayList<ProgramInterface> getAllPrograms() {
        try {
            ArrayList<ProgramInterface> returnList = new ArrayList<>();

            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM programs");
            ResultSet sqlProgramReturnValue = stmt.executeQuery();
            while (sqlProgramReturnValue.next()) {
                stmt = connection.prepareStatement("SELECT * FROM genres WHERE id = ?");
                stmt.setInt(1, sqlProgramReturnValue.getInt(3));
                ResultSet sqlGenreReturnValue = stmt.executeQuery();
                if (!sqlGenreReturnValue.next()) {
                    return null;
                }
                returnList.add(new ProgramData(sqlProgramReturnValue.getString(2), sqlProgramReturnValue.getString(4), LocalTime.parse(sqlProgramReturnValue.getString(5)), sqlGenreReturnValue.getString(2), sqlProgramReturnValue.getString(6), sqlProgramReturnValue.getInt(7)));
            }
            return returnList;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ProgramInterface getProgram(String programTitle) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM programs WHERE title = ?");
            stmt.setString(1, programTitle);
            ResultSet sqlProgramReturnValue = stmt.executeQuery();
            if (!sqlProgramReturnValue.next()) {
                return null;
            }
            //Gets genre from the program
            stmt = connection.prepareStatement("SELECT * FROM genres WHERE id = ?");
            stmt.setInt(1, sqlProgramReturnValue.getInt(3));
            ResultSet sqlGenreReturnValue = stmt.executeQuery();
            if (!sqlGenreReturnValue.next()) {
                return null;
            }
            return new ProgramData(sqlProgramReturnValue.getString(2), sqlProgramReturnValue.getString(4), LocalTime.parse(sqlProgramReturnValue.getString(5)), sqlGenreReturnValue.getString(2), sqlProgramReturnValue.getString(6), sqlProgramReturnValue.getInt(7));
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<PersonInterface> getPersonsFromName(String searchParam) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM persons WHERE name LIKE CONCAT('%',?,'%')");
            stmt.setString(1, searchParam);
            ResultSet sqlReturnValues = stmt.executeQuery();
            List<PersonInterface> returnValue = new ArrayList<>();
            while (sqlReturnValues.next()) {
                LocalDate birthdate = java.time.LocalDate.parse(sqlReturnValues.getDate(3).toString());
                returnValue.add(new PersonData(birthdate, sqlReturnValues.getInt(1), sqlReturnValues.getString(4), sqlReturnValues.getString(2)));
            }
            return returnValue;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public int getProgramId(String programName) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM programs WHERE title = ?");
            stmt.setString(1, programName);
            ResultSet sqlReturnValue = stmt.executeQuery();
            if (!sqlReturnValue.next()) {
                return -1;
            }
            return sqlReturnValue.getInt(1);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public String getCreditsFromProgramId(int id) {
        String returnString = "";

        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM credits WHERE program_id = ?");
            stmt.setInt(1, id);
            ResultSet sqlCreditsReturnValues = stmt.executeQuery();
            while (sqlCreditsReturnValues.next()) {
                String stringToAdd = "";
                stmt = connection.prepareStatement("SELECT * FROM persons WHERE id = ?");
                stmt.setInt(1, sqlCreditsReturnValues.getInt(3));
                ResultSet sqlPersonReturnValue = stmt.executeQuery();
                if (!sqlPersonReturnValue.next()) {
                    return null;
                }
                stringToAdd += sqlPersonReturnValue.getString(2) + ", ";
                stmt = connection.prepareStatement("SELECT * FROM occupation WHERE id = ?");
                stmt.setInt(1, sqlCreditsReturnValues.getInt(4));
                ResultSet sqlOccupationReturnValue = stmt.executeQuery();
                if (!sqlOccupationReturnValue.next()) {
                    return null;
                }
                stringToAdd += sqlOccupationReturnValue.getString(2);
                if (sqlOccupationReturnValue.getInt(1) == 36) {
                    stmt = connection.prepareStatement("SELECT * FROM movie_role WHERE credits_id = ?");
                    stmt.setInt(1, sqlCreditsReturnValues.getInt(1));
                    ResultSet sqlMovieRoleReturnValue = stmt.executeQuery();
                    if (!sqlMovieRoleReturnValue.next()) {
                        return null;
                    }
                    stringToAdd += ", " + sqlMovieRoleReturnValue.getString(3);
                }
                stringToAdd += "\n";
                returnString += stringToAdd;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

        return returnString;
    }

    public List<CreditInterface> getCreditFromID(int personID) {
        try {
            List<CreditInterface> returnValue = new ArrayList<>();

            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM credits WHERE person_id = ?");
            stmt.setInt(1, personID);
            ResultSet sqlCreditReturnValue = stmt.executeQuery();
            while (sqlCreditReturnValue.next()) {
                //Gets program
                stmt = connection.prepareStatement("SELECT * FROM programs WHERE id = ?");
                stmt.setInt(1, sqlCreditReturnValue.getInt(2));
                ResultSet sqlProgramReturnValue = stmt.executeQuery();
                if (!sqlProgramReturnValue.next()) {
                    return null;
                }
                //Gets genre from the program
                stmt = connection.prepareStatement("SELECT * FROM genres WHERE id = ?");
                stmt.setInt(1, sqlProgramReturnValue.getInt(3));
                ResultSet sqlGenreReturnValue = stmt.executeQuery();
                if (!sqlGenreReturnValue.next()) {
                    return null;
                }
                ProgramData tempProgram = new ProgramData(sqlProgramReturnValue.getString(2), sqlProgramReturnValue.getString(4), LocalTime.parse(sqlProgramReturnValue.getString(5)), sqlGenreReturnValue.getString(2), sqlProgramReturnValue.getString(6), sqlProgramReturnValue.getInt(7));

                //Gets person
                stmt = connection.prepareStatement("SELECT * FROM persons WHERE id = ?");
                stmt.setInt(1, sqlCreditReturnValue.getInt(3));
                ResultSet sqlPersonReturnValue = stmt.executeQuery();
                if (!sqlPersonReturnValue.next()) {
                    return null;
                }
                LocalDate birthdate = java.time.LocalDate.parse(sqlPersonReturnValue.getDate(3).toString());
                PersonData tempPerson = new PersonData(birthdate, sqlPersonReturnValue.getInt(1), sqlPersonReturnValue.getString(4), sqlPersonReturnValue.getString(2));

                //Gets occupation
                stmt = connection.prepareStatement("SELECT * FROM occupation where id = ?");
                stmt.setInt(1, sqlCreditReturnValue.getInt(4));
                ResultSet sqlOccupationReturnValue = stmt.executeQuery();
                if (!sqlOccupationReturnValue.next()) {
                    return null;
                }
                String tempOccupation = sqlOccupationReturnValue.getString(2);

                int tempCreatedID = sqlCreditReturnValue.getInt(5);

                if (sqlOccupationReturnValue.getInt(1) == 36) {
                    String tempCharacterName = getMovieRole(sqlCreditReturnValue.getInt(1));
                    returnValue.add(new CreditData(tempPerson, tempProgram, tempOccupation, tempCharacterName, tempCreatedID));
                } else {
                    returnValue.add(new CreditData(tempPerson, tempProgram, tempOccupation, tempCreatedID));
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

    public void SavePerson(PersonInterface person, int userId) {
        try {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO persons (name, age, email, created) VALUES (?,?,?,?)");
            stmt.setString(1, person.getName());
            stmt.setDate(2, java.sql.Date.valueOf(person.getBirthDate()));
            stmt.setString(3, person.getEmail());
            stmt.setInt(4, userId);
            stmt.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public PersonInterface getPersonFromID(int id) {
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

    public boolean doesPersonExist(String email) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM persons WHERE email = ?");
            stmt.setString(1, email);
            ResultSet sqlReturnValues = stmt.executeQuery();
            if (sqlReturnValues.next()) {
                return true;
            }
            return false;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public void editPerson(PersonInterface person) {
        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE persons SET name = ?, age = ? WHERE email = ?");
            stmt.setString(1, person.getName());
            stmt.setDate(2, java.sql.Date.valueOf(person.getBirthDate()));
            stmt.setString(3, person.getEmail());
            stmt.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public ArrayList<PersonInterface> getAllPersonsByCreatorId(int id) {
        ArrayList<PersonInterface> returnList = new ArrayList<>();

        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM persons WHERE created = ?");
            stmt.setInt(1, id);
            ResultSet sqlReturnValues = stmt.executeQuery();
            while (sqlReturnValues.next()) {
                LocalDate birthdate = java.time.LocalDate.parse(sqlReturnValues.getDate(3).toString());
                returnList.add(new PersonData(birthdate, sqlReturnValues.getString(4), sqlReturnValues.getString(2)));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

        return returnList;
    }

    public void deleteUser(int userId, String reason, int deletedById) {
        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE users SET active = ? WHERE id = ?");
            stmt.setBoolean(1, false);
            stmt.setInt(2, userId);
            stmt.execute();
            stmt = connection.prepareStatement("INSERT INTO deleted_users (user_id,deleted_by,reason,date) VALUES (?,?,?,?)");
            stmt.setInt(1, userId);
            stmt.setInt(2, deletedById);
            stmt.setString(3, reason);
            java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
            stmt.setDate(4, date);
            stmt.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return;
        }
    }
    //</editor-fold>

    //<editor-fold desc="Utilities">
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
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return returnHash;
    }
    //</editor-fold>
}

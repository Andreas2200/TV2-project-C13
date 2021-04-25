package Persistence;


import Domain.User;
import Interfaces.*;
import Domain.Occupation;
import java.io.*;
import java.time.LocalTime;
import java.util.*;

//bruges kun til at teste database componenter og skal i ingen omstændigheder bruges i det endelige produkt
class Main {
    static DatabaseSystem dbSys = new DatabaseSystem();
    public static void main(String[] args) throws Exception {
        dbSys = dbSys.getInstance();
        //User user = null;
        //System.out.println(user = (User) dbSys.getUser("Admin", "password"));
        //System.out.println(String.valueOf(dbSys.SearchPerson("tom")));
        //System.out.println(String.valueOf(dbSys.getAllPersons()));
        System.out.println(dbSys.doesPersonExist("Sigurdskelmose@gmail.com"));
    }
}

public class DatabaseSystem {

    private static DatabaseSystem instance;

    public static synchronized DatabaseSystem getInstance(){
        if (instance == null){
            instance = new DatabaseSystem();
        }
        return instance;
    }

    public ArrayList<ProgramInterface> getProgram() throws Exception {
        //readlines and put em in a list
        //ArrayList<ProgramInterface> searchItems = new ArrayList<>();    hvad gør den her linje???
        ArrayList<String> dataValues = readDataValues("programs.txt");
        ArrayList<ProgramInterface> programs = new ArrayList<>();

        for (int i = 0; i < dataValues.size(); i += 7){
            //Year of release
            /*Calendar calendar = Calendar.getInstance();
            String[] calendarValues = dataValues.get(3 + i).split(",");
            calendar.set(Integer.parseInt(calendarValues[0]), Integer.parseInt(calendarValues[1]), Integer.parseInt(calendarValues[2]));*/
            Date date = new Date();
            date = Calendar.getInstance().getTime();
            //Watch length
        // Simon, hvorfor skulle den split på komma og ikke kolon?
            String[] splittetTime = (dataValues.get(4 + i).split(":"));
            LocalTime time = LocalTime.of(Integer.parseInt(splittetTime[0]), Integer.parseInt(splittetTime[1]), Integer.parseInt(splittetTime[2]));

            //Genre
            ArrayList<GenreData> genres = new ArrayList<>();
            String[] splittetGenres = dataValues.get(2).split(",");
            for (int j = 0; j < splittetGenres.length; j++){
                genres.add(GenreData.valueOf(splittetGenres[j]));
            }

            ProgramData program = new ProgramData(Integer.parseInt(dataValues.get(0 + i)), dataValues.get(1 + i), dataValues.get(3+i), null, time, genres, dataValues.get(5 + i), Integer.parseInt(dataValues.get(6 + i)));

            //credits
            Scanner reader = new Scanner(new File("credits.txt"));
            while (reader.hasNextLine()){
                int programID = Integer.parseInt(reader.nextLine().split(";")[0]);
                if (Integer.parseInt(dataValues.get(0 + i)) == programID) {
                    for (CreditInterface credit : (getCredits(programID))){
                        program.addCredit(credit);
                    }
                }
            }
            programs.add(program);
        }
        return programs;
    }

    //denne metode vil ikke kunne virke hvis den ikke kan få fat på creditteringerne for et program
    public ArrayList<ProgramInterface> getProgram(PersonInterface person) throws Exception {
        throw new Exception("Mangler en getCreditsmetode i Program før denne metode kan blive implementeret -2 Ris");
        /*
        ArrayList<ProgramInterface> programs = getProgram();
        ArrayList<CreditInterface> credits = getCredits();
        ArrayList<ProgramInterface> returnPrograms = new ArrayList<>();
        for (ProgramInterface program : programs){
            for (CreditInterface credit : credits){
                if (credit.getPerson().equals(person)) if (program.);
            }
        }

        return returnPrograms;
*/
    }

    public ProgramInterface getProgram(GenreInterface genre) throws Exception {
        ArrayList<ProgramInterface> programs = getProgram();
        for (ProgramInterface program : programs) {
            for (GenreInterface genre_ : program.getGenre()) {
                if (genre_.equals(genre)) return program;
            }
        }
        return null;
    }

    public ArrayList<ProgramInterface> getProgram(String title) throws Exception {
        ArrayList<ProgramInterface> programs = getProgram();
        //Sort em
        Collections.sort(programs, Comparator.comparing(ProgramInterface::getName));

        ArrayList<ProgramInterface> sortedPrograms = new ArrayList<>();

        for (ProgramInterface program : programs) {
            String splitValues = SplitByChar(program.getName(), title.length());
            if (splitValues.equals(title)) sortedPrograms.add(program);
        }
        return sortedPrograms;
    }

    public boolean SaveProgram(ProgramInterface program) {
        try {
            int ID = -1;

            //getID
            Scanner reader = new Scanner(new File("programs.txt"));
            while (reader.hasNextLine()){
                String read = reader.nextLine();
                String[] readValues = read.split(";");
                ID = Integer.parseInt(readValues[0]);
            }
            ID += 1;
            reader.close();

            FileWriter writer = new FileWriter(new File("programs.txt"), true);

            String genres = "";
            for (GenreInterface genre : program.getGenre()){
                genres += genre + ",";
            }

            writer.write(ID + ";" + program.getName() + ";" + genres + ";" + program.getReleaseDate() + ";" + program.getDuration() + ";" + program.getDescription() + ";" + program.getCreatorID() + "\n");
            writer.close();
            return true;
        }
        catch (IOException e){
            System.out.println(e);
            return false;
        }
    }

    public boolean SaveProgram(ProgramInterface program, int ID) {
        try {
            FileWriter writer = new FileWriter(new File("programs.txt"), true);
            String genres = "";
            for (GenreInterface genre : program.getGenre()){
                if (program.getGenre().get(program.getGenre().size()).equals(genre)){
                    genres += genre;
                    break;
                }
                genres += genre + ",";
            }
            writer.write(ID + ";" + program.getName() + ";" + genres + ";" + program.getReleaseDate() + ";" + program.getDuration() + ";" + program.getDescription() + ";" + program.getCreatorID() + "\n");
            writer.close();
            return true;
        }
        catch (IOException e){
            return false;
        }
    }

    //denne her metode er for at få fat på alle credits uden at skulle have nogen kritiriere
    public ArrayList<CreditInterface> getCredits() throws Exception {
        ArrayList<CreditInterface>  credits = new ArrayList<>();

        Scanner reader = new Scanner(new File("credits.txt"));
        //stjal lige noget kode, det ville sku være hurtigere
        while (reader.hasNextLine()){
            String read = reader.nextLine();
            String[] readSplit = read.split(";");
            CreditData credit;
            if (readSplit.length == 3) {
                credit = new CreditData(OccupationData.valueOf(readSplit[1]), (PersonData) getPerson(Integer.parseInt(readSplit[0])));
            }
            else {
                credit = new CreditData(OccupationData.valueOf(readSplit[1]), (PersonData) getPerson(Integer.parseInt(readSplit[0])), readSplit[2]);
            }
            credits.add(credit);
        }
        return credits;
    }

    public ArrayList<CreditInterface> getCredits(int programID) throws Exception {
        ArrayList<CreditInterface> credits = new ArrayList<>();

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
                    credit = new CreditData(OccupationData.valueOf(readSplit[1]), (PersonData) getPerson(Integer.parseInt(readSplit[0])));
                }
                else {
                    credit = new CreditData(OccupationData.valueOf(readSplit[1]), (PersonData) getPerson(Integer.parseInt(readSplit[0])), readSplit[2]);
                }
                credits.add(credit);
            }
            return credits;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<CreditInterface> getCredits(String programTitle) throws Exception {
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
                    credit = new CreditData(OccupationData.valueOf(readSplit[1]), (PersonData) getPerson(Integer.parseInt(readSplit[0])));
                }
                else {
                    credit = new CreditData(OccupationData.valueOf(readSplit[1]), (PersonData) getPerson(Integer.parseInt(readSplit[0])), readSplit[2]);
                }
                credits.add(credit);
            }
            return credits;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean saveCredits(CreditInterface credit, int programID){
        try {
            //ved ikke hvorfor den skulle lave en ny fil hvergang
            //har tilføjet programID sådan at vi altid kan finde tilbage til det program det var lavet til
            //gjort sådan at den fortæller om det lykkedes for den at gemme credits
            FileWriter writer = new FileWriter(new File("credit.txt"), true);
            if (credit.getCharacterName() != null) {
                writer.write(credit.getPerson().getId() + ";" + credit.getOccupation().toString() + ";" + programID + ";" + credit.getOccupation() + "\n");
                writer.close();
            }
            else {
                writer.write(credit.getPerson().getId() + ";" + credit.getOccupation().toString() + ";" + programID + "\n");
                writer.close();
            }
        }
        catch (IOException e){
            System.out.println(e);
            return false;
        }
        return true;
    }

    public ArrayList<CreditInterface> getAllCredits() throws Exception {
        ArrayList<CreditInterface> credits = new ArrayList<>();
        ArrayList<String> readValues = new ArrayList<>();
        try(Scanner reader = new Scanner(new File("credits.txt")))
        {
            while (reader.hasNextLine())
            {
                readValues.add(reader.nextLine());
            }
            for (String element: readValues)
            {
                String[] valuesToUse = element.split(";");
                credits.add(new CreditData(OccupationData.valueOf(valuesToUse[4]), new PersonData(Integer.parseInt(valuesToUse[0]),Integer.parseInt(valuesToUse[1]),valuesToUse[2],valuesToUse[3])));
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        return credits;
    }

    public boolean saveCredit(CreditInterface credit)
    {
        try(FileWriter writer = new FileWriter(new File("Credits.txt"),true))
        {
            if(credit.getPerson().getName() != null)
            {
                writer.write(credit.getPerson().getAge() + ";" + credit.getPerson().getId() + ";" + credit.getPerson().getEmail() + ";" + credit.getPerson().getName() + ";" + credit.getOccupation() + "\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    //Lavet af Sigster
    public ArrayList<String>  getAllCreditsFromCreditFile(int personID) {
        ArrayList<String> readValues = new ArrayList<>();
        //List<ArrayList<String>> credits = new ArrayList<>();
        ArrayList<String> credit = new ArrayList<>();
        try(Scanner reader = new Scanner(new File("credit.txt"))) {
            while (reader.hasNextLine()) {
                readValues.add(reader.nextLine());
            }
            for(String element: readValues) {
                String[] valuesToUse = element.split(";");
                if(String.valueOf(personID).equals(valuesToUse[0])) {
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
    public ArrayList<String> getProgramFromID(String programID) {
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
    }

    public UserInterface getUser(String username, String password) {
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
    }

    public UserInterface getUser(String username) {
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
    }

    //returns if the username already existed
    //true == user didn't exist and adding the new user was a succes
    //false == user already exist
    public boolean SaveUser(UserInterface user) throws IOException {
        if (getUser(user.getUsername()) == null){
            FileWriter writer = new FileWriter("usernames.txt", true);
            writer.write( user.getUsername() + ";" + user.getPassword() + ";" + user.getName() + ";" + user.getRole() + ";" + user.getEmail() + ";" + user.getAge() + "\n" );
            writer.close();
            return true;
        }
        else {
            return false;
        }
    }

    public ArrayList<UserInterface> getAllUsers()
    {
        ArrayList<UserInterface> returnList = new ArrayList<>();
        ArrayList<String> readValues = new ArrayList<>();
        try(Scanner reader = new Scanner(new File("usernames.txt")))
        {
            while (reader.hasNextLine())
            {
                readValues.add(reader.nextLine());
            }
            for (String element: readValues)
            {
                String[] valuesToUse = element.split(";");
                returnList.add(new UserData(valuesToUse[2],valuesToUse[0], valuesToUse[1],Integer.parseInt(valuesToUse[5]), valuesToUse[4], valuesToUse[3]));
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        return returnList;
    }

    //FIX ME, få den til at returnere en Person og ændre navnet
    public String Search(int id) throws Exception {
        try {
            ArrayList<String> readLines = new ArrayList<>();
            Scanner reader = new Scanner(new File("persons.txt"));
            while (reader.hasNextLine()){
                readLines.add(reader.nextLine());
            }
        }
        catch (Exception e){
            throw new Exception();
        }
        return "";
    }

    public void SavePerson(PersonInterface person) throws IOException {
        Scanner reader = new Scanner(new File("persons.txt"));
        if (person.getId() == -1){
            int ID = 0;
            while (reader.hasNextLine()){
                ID = Integer.parseInt(reader.nextLine().split(";")[0]) + 1;
            }
            reader.close();
            FileWriter writer = new FileWriter("persons.txt", true);
            writer.write("\n" + ID + ";" + person.getName() + ";" + person.getAge() + ";" + person.getEmail());
            writer.close();
        }
        else {
            FileWriter writer = new FileWriter("persons.txt", true);
            writer.write("\n" + person.getId() + ";" + person.getName() + ";" + person.getAge() + ";" + person.getEmail());
            writer.close();
        }
    }

    public PersonInterface getPerson(int id) {
        PersonData person = null;
        try {
            Scanner reader = new Scanner(new File("persons.txt"));
            while (reader.hasNextLine()) {
                String read = reader.nextLine();
                String[] readSplit = read.split(";");
                if (Integer.parseInt(readSplit[0]) == id) {
                    person = new PersonData(Integer.parseInt(readSplit[2]), id, readSplit[3], readSplit[1]);
                }
            }
            return person;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean doesPersonExist(String email) {
        try {
            Scanner reader = new Scanner(new File("persons.txt"));
            while(reader.hasNextLine()) {
                String read = reader.nextLine();
                String [] readSplit = read.split(";");

                if(readSplit[3].equals(email)) {
                    System.out.println(readSplit);
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean doesProgramExist(String name) {
        try {
            Scanner reader = new Scanner(new File("programs.txt"));
            while(reader.hasNextLine()) {
                String read = reader.nextLine();
                String[] readSplit = read.split(";");
                if(readSplit[1].equals(name)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    //SearchParam uses letters to narrow the search result down, type is the searched var in object, file is the file the data is in
    public ArrayList<PersonInterface> SearchPerson(String searchParam) throws Exception {
        //readlines and put em in a list
        String splitValue = "";
        searchParam = searchParam.toLowerCase();
        ArrayList<PersonInterface> searchItems = new ArrayList<>();
        ArrayList<PersonInterface> dataValues = getAllPersons();

        //throw out all of the lines which is not searched for
        for (int i = 0; i < dataValues.size(); i++){
            if(dataValues.get(i).getName().length() == searchParam.length()) {
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
    public ArrayList<PersonInterface> getAllPersons() throws Exception {
        ArrayList<PersonInterface> persons = new ArrayList<>();
        ArrayList<String> personValues = readDataValues("persons.txt");
        for (int i = 0; i < personValues.size(); i = i + 4){
            persons.add(new PersonData(Integer.parseInt(personValues.get(i + 2)), Integer.parseInt(personValues.get(i + 0)), personValues.get(i + 3), personValues.get(i + 1)));
        }
        return persons;
    }


    private String SplitByChar(String text, int splitBy){
        return text.substring(0, splitBy);
    }

    private ArrayList<String> readDataValues(String path) throws Exception {
        ArrayList<String> readLines = new ArrayList<>();
        ArrayList<String> returnLines = new ArrayList<>();
        try {
            Scanner reader = new Scanner(new File(path));
            while (reader.hasNextLine()){
                readLines.add(reader.nextLine());
            }
            for (String readLine : readLines){
                String[] lines = readLine.split(";");
                for (String line : lines){
                    returnLines.add(line);
                }
            }
            reader.close();
        }
        catch (Exception e){
            throw new FileNotFoundException();
        }
        return returnLines;
    }

    private void updateUsers(ArrayList<UserInterface> users)
    {
        try (FileWriter writer = new FileWriter(new File("usernames.txt")))
        {
            for (UserInterface user: users)
            {
                writer.write( user.getUsername() + ";" + user.getPassword() + ";" + user.getName() + ";" + user.getRole() + ";" + user.getEmail() + ";" + user.getAge() + "\n" );
            }
        }
        catch (IOException ex)
        {
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
}

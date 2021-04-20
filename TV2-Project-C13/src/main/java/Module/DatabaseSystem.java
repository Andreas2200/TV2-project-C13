package Module;

import CLI.Credit;
import CLI.Program;
import CLI.User;
import Interfaces.CreditInterface;
import Interfaces.PersonInterface;
import Interfaces.ProgramInterface;
import Interfaces.UserInterface;
import org.example.Credits;
import org.example.Genre;
import org.example.Occupation;
import org.example.Person;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

//bruges kun til at teste database componenter og skal i ingen omstændigheder bruges i det endelige produkt
class Main {
    static DatabaseSystem dbSys = new DatabaseSystem();
    public static void main(String[] args) throws Exception {
        dbSys = dbSys.getInstance();
        /*
        Person person = new Person(20, 1, "BoB@bobbymail.bob", "Bob Bobbyson");
        Credits credit = new Credits(Occupation.CASTING, person);
        dbSys.saveCredits(credit, "Superman 2");
        ArrayList<CreditInterface> arrayList = dbSys.getCredits("Superman 2");
        System.out.println(arrayList.get(5).getOccupation());
         */
        //ArrayList<Program> programs = dbSys.getProgram();
        //System.out.println(dbSys.SaveProgram(new Program("SUPERMAN 2 Return of the Jedi", new Date(), null, LocalTime.now(), new ArrayList<Genre>(Collections.singleton(Genre.ACTION)), "Bedre end den første", 0 )));
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
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, Integer.parseInt(dataValues.get(4 + i)));
            Date date = new Date();
            date = Calendar.getInstance().getTime();
            //Watch length
            String[] splittetTime = (dataValues.get(5 + i).split(","));
            ArrayList<Integer> splittetTimeInt = new ArrayList<Integer>();
            for (int j = 0; j < splittetTime.length; j ++){
                splittetTimeInt.add(Integer.parseInt(splittetTime[j]));
            }
            LocalTime time = LocalTime.of(splittetTimeInt.get(0), splittetTimeInt.get(1), splittetTimeInt.get(2), splittetTimeInt.get(3));

            //Genre
            ArrayList<Genre> genres = new ArrayList<>();
            String[] splittetGenres = dataValues.get(2).split(",");
            for (int j = 0; j < splittetGenres.length; j++){
                genres.add(Genre.valueOf(splittetGenres[j]));
            }
            ProgramData program = new ProgramData(dataValues.get(1 + i), date, null, time, genres, dataValues.get(6 + i), Integer.parseInt(dataValues.get(0 + i)));
            programs.add(program);
        }
        return programs;
    }

    public Program getProgram(Person person){
        return null;
    }

    public ProgramInterface getProgram(Genre genre) throws Exception {
        ArrayList<ProgramInterface> programs = getProgram();
        for (ProgramInterface program : programs) {
            for (Genre genre_ : program.getGenre()) {
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
            for (Genre genre : program.getGenre()){
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
            for (Genre genre : program.getGenre()){
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


    public ArrayList<CreditInterface> getCredits(String programTitle){
        ArrayList<CreditInterface> credits = new ArrayList<>();
        try {
            Scanner reader = new Scanner(new File(programTitle + "-credits.txt"));
            while (reader.hasNextLine()){
                String read = reader.nextLine();
                String[] readSplit = read.split(";");
                CreditData credit;
                if (readSplit.length == 2) {
                    credit = new CreditData(Occupation.valueOf(readSplit[1]), (PersonData) getPerson(Integer.parseInt(readSplit[0])));
                }
                else {
                    credit = new CreditData(Occupation.valueOf(readSplit[1]), (PersonData) getPerson(Integer.parseInt(readSplit[0])), readSplit[2]);
                }
                credits.add(credit);
            }
            return credits;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveCredits(CreditInterface credit, String programTitle){
        try {
            FileWriter writer = new FileWriter(new File(programTitle + "-credits.txt"), true);
            if (credit.getCharacterName() != null) {
                writer.write(credit.getPerson().getId() + ";" + credit.getOccupation().toString() + ";" + credit.getOccupation() + "\n");
                writer.close();
            }
            else {
                writer.write(credit.getPerson().getId() + ";" + credit.getOccupation().toString() + "\n");
                writer.close();
            }
        }
        catch (IOException e){
            System.out.println(e);
        }
    }

    public UserInterface getUser(String username, String password) {
        try (Scanner reader = new Scanner(new File("usernames.txt")))
        {
            //har haft nogle problemer med at få pathen til filen til at fungere
            while (reader.hasNext()){
                String[] userInfo = reader.nextLine().split(";");
                if (userInfo[0].equals(username)){
                    if (userInfo[1].equals(password)){
                        return new UserData(userInfo[2], userInfo[0], userInfo[1], Integer.parseInt(userInfo[4]),userInfo[3]);
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
                    return new UserData(userInfo[2], userInfo[0], userInfo[1], Integer.parseInt(userInfo[4]),userInfo[3]);
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
            writer.write(user.getUsername() + ";" + user.getPassword() + ";" + user.getName() + ";" + user.getRole() + ";" + user.getAge() + "\n");
            writer.close();
            return true;
        }
        else {
            return false;
        }
    }

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

    //SearchParam uses letters to narrow the search result down, type is the searched var in object, file is the file the data is in
    public ArrayList<PersonInterface> SearchPerson(String searchParam) throws Exception {
        //readlines and put em in a list
        String splitValue = "";
        searchParam = searchParam.toLowerCase();
        ArrayList<PersonInterface> searchItems = new ArrayList<>();
        ArrayList<PersonInterface> dataValues = getAllPersons();

        //throw out all of the lines which is not searched for
        for (int i = 0; i < dataValues.size(); i++){
            splitValue = SplitByChar(dataValues.get(i).getName(), searchParam.length());
            splitValue = splitValue.toLowerCase();
            if (splitValue.equals(searchParam)){
                searchItems.add(dataValues.get(i));
                System.out.println(dataValues.get(i).getName());
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
        }
        catch (Exception e){
            throw new FileNotFoundException();
        }
        return returnLines;
    }
}

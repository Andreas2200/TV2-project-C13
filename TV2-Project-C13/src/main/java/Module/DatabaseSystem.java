package Module;

import CLI.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

class Person{
    int age;
    int ID;
    String Email;
    String Name;
    Person(int age, int ID, String Email, String Name){
        this.age = age; this.ID = ID; this.Email = Email; this.Name = Name;
    }
    public String getName(){
        return Name;
    }
}

class Main {
    static DatabaseSystem dbSys = new DatabaseSystem();
    public static void main(String[] args) throws Exception {
        ArrayList<Person> searched = dbSys.SearchPerson("k");
        for (Person person : searched){
            System.out.println("Person:\n" + person.Name + "\nAge:\n" + person.age + "\nEmail:\n" + person.Email);
        }
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

    public User getUser(String username, String password) {
        try {
            //har haft nogle problemer med at få pathen til filen til at fungere
            Scanner reader = new Scanner(new File("../usernames.txt"));
            while (reader.hasNext()){
                String[] userInfo = reader.nextLine().split(",");
                if (userInfo[0].equals(username)){
                    if (userInfo[1].equals(password)){
                        return new User(userInfo[2], userInfo[0], userInfo[1], Integer.parseInt(userInfo[4]));
                    }
                }
            }
        }
        catch (FileNotFoundException e){
            System.out.println(e);
        }
        return null;
    }

    public String Search(int id) throws Exception {
        try {
            ArrayList<String> readLines = new ArrayList<>();
            Scanner reader = new Scanner(new File("this.txt"));
            while (reader.hasNextLine()){
                readLines.add(reader.nextLine());
            }

        }
        catch (Exception e){
            throw new Exception();
        }
        return "";
    }

    //SearchParam uses letters to narrow the search result down, type is the searched var in object, file is the file the data is in
    public ArrayList<Person> SearchPerson(String searchParam) throws Exception {
        //readlines and put em in a list
        String splitValue = "";
        searchParam = searchParam.toLowerCase();
        ArrayList<Person> searchItems = new ArrayList<>();
        ArrayList<Person> dataValues = getAllPersons();

        //throw out all of the lines which is not searched for
        for (int i = 0; i < dataValues.size(); i++){
            splitValue = SplitByChar(dataValues.get(i).Name, searchParam.length());
            splitValue = splitValue.toLowerCase();
            if (splitValue.equals(searchParam)){
                searchItems.add(dataValues.get(i));
                System.out.println(dataValues.get(i).Name);
            }
        }
        //sort em
        Collections.sort(searchItems, Comparator.comparing(Person::getName));
        return searchItems;
    }

    //An unsorted and quicker way to get all persons, mostly used by Datasystem itself but can be used outside
    public ArrayList<Person> getAllPersons() throws Exception {
        ArrayList<Person> persons = new ArrayList<>();
        ArrayList<String> personValues = readDataValues("D:/Memes/Sorting/Sorting/out/production/Sorting/sample/this.txt");
        for (int i = 0; i < personValues.size(); i = i + 4){
            persons.add(new Person(Integer.parseInt(personValues.get(i + 2)), Integer.parseInt(personValues.get(i + 0)), personValues.get(i + 3), personValues.get(i + 1)));
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
                String[] lines = readLine.split(",");
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

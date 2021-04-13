package Persistence;

import Domain.User;
import Domain.Person;

import java.io.*;
import java.util.*;

//bruges kun til at teste database componenter og skal i ingen omstændigheder bruges i det endelige produkt
class Main {
    static DatabaseSystem dbSys = new DatabaseSystem();
    public static void main(String[] args) throws Exception {
        ArrayList<Person> searched = dbSys.searchPerson("k");
        for (Person person : searched){
            System.out.println("Person:\n" + person.getName() + "\nAge:\n" + person.getAge() + "\nEmail:\n" + person.getEmail());
        }
        //String name, String username, String password, int age, String role
        dbSys.saveUser(new User("Tom Holland", "TommyBOI", "ThisIsAmerica69", 24, "Spiderman"));
        dbSys.savePerson(new Person(48, "Tom@Holland.US", "Tom Holland"));
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
        try (Scanner reader = new Scanner(new File("usernames.txt")))
        {
            //har haft nogle problemer med at få pathen til filen til at fungere
            while (reader.hasNext()){
                String[] userInfo = reader.nextLine().split(";");
                if (userInfo[0].equals(username)){
                    if (userInfo[1].equals(password)){
                        return new User(userInfo[2], userInfo[0], userInfo[1], Integer.parseInt(userInfo[4]),userInfo[5],userInfo[3]);
                    }
                }
            }
        }
        catch (FileNotFoundException e){
            System.out.println(e);
        }
        return null;
    }

    public User getUser(String username) {
        try (Scanner reader = new Scanner(new File("usernames.txt")))
        {
            while (reader.hasNext()){
                String[] userInfo = reader.nextLine().split(";");
                if (userInfo[0].equals(username)){
                    return new User(userInfo[2], userInfo[0], userInfo[1], Integer.parseInt(userInfo[4]),userInfo[3]);
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
    public boolean saveUser(User user) throws IOException {
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

    public String search(int id) throws Exception {
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

    public void savePerson(Person person) throws IOException {
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

    //SearchParam uses letters to narrow the search result down, type is the searched var in object, file is the file the data is in
    public ArrayList<Person> searchPerson(String searchParam) throws Exception {
        //readlines and put em in a list
        String splitValue = "";
        searchParam = searchParam.toLowerCase();
        ArrayList<Person> searchItems = new ArrayList<>();
        ArrayList<Person> dataValues = getAllPersons();

        //throw out all of the lines which is not searched for
        for (int i = 0; i < dataValues.size(); i++){
            splitValue = splitByChar(dataValues.get(i).getName(), searchParam.length());
            splitValue = splitValue.toLowerCase();
            if (splitValue.equals(searchParam)){
                searchItems.add(dataValues.get(i));
                System.out.println(dataValues.get(i).getName());
            }
        }
        //sort em
        Collections.sort(searchItems, Comparator.comparing(Person::getName));
        return searchItems;
    }

    //An unsorted and quicker way to get all persons, mostly used by Datasystem itself but can be used outside
    public ArrayList<Person> getAllPersons() throws Exception {
        ArrayList<Person> persons = new ArrayList<>();
        ArrayList<String> personValues = readDataValues("persons.txt");
        for (int i = 0; i < personValues.size(); i = i + 4){
            persons.add(new Person(Integer.parseInt(personValues.get(i + 2)), Integer.parseInt(personValues.get(i + 0)), personValues.get(i + 3), personValues.get(i + 1)));
        }
        return persons;
    }


    private String splitByChar(String text, int splitBy){
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

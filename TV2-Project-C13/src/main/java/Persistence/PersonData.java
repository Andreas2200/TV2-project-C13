package Persistence;

import Interfaces.PersonInterface;

public class PersonData implements PersonInterface {
    private int age;
    private int id;
    private String email;
    private String name;

    public PersonData (int age, int id, String email, String name) {
        this.age = age;
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public PersonData (int age, String email, String name) {
        this.age = age;
        this.id = -1;
        this.email = email;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}

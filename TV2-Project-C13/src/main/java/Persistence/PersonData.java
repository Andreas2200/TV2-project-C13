package Persistence;

import Interfaces.PersonInterface;

import java.time.LocalDate;
import java.time.Period;

public class PersonData implements PersonInterface {
    private LocalDate birthDate;
    private int id;
    private String email;
    private String name;

    public PersonData(LocalDate birthDate, int id, String email, String name) {
        this.birthDate = birthDate;
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public PersonData(LocalDate birthDate, String email, String name) {
        this.birthDate = birthDate;
        this.id = -1;
        this.email = email;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getAge() {
        LocalDate today = LocalDate.now();
        Period p = Period.between(birthDate, today);
        return p.getYears();
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    @Override
    public String toString() {
        return "\n" + "Name: " + getName() + " age: " + getAge() + " email: " + getEmail();
    }

}

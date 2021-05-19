package Persistence;

import Interfaces.UserInterface;

import java.time.LocalDate;
import java.time.Period;

public class UserData implements UserInterface {
    private String username;
    private String password;
    private String salt;
    private int id;
    private String name;
    private String role;
    private String email;
    private LocalDate birthday;

    public UserData(String username, String password, String salt, String name, String email, LocalDate birthday) {
        this.username = username.toLowerCase();
        this.password = password;
        this.salt = salt;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
        this.role = "User";
    }

    public UserData(String username, String password, String salt, String name, String email, LocalDate birthday, String role) {
        this(username, password, salt, name, email, birthday);
        this.role = role;
    }

    public UserData(int id, String name, String role, String email) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        LocalDate today = LocalDate.now();
        Period p = Period.between(birthday, today);
        return p.getYears();
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getRole() {
        return role;
    }

    public String getSalt() {
        return salt;
    }

    public void setPassword(String newPas) {
        this.password = newPas;
    }

    public void changeName(String newName) {
        this.name = newName;
    }

    public void setRole(String newRole) {
        this.role = newRole;
    }

    public boolean checkInfo(String name, String username, int age) {
        return this.getName().equals(name) && this.getUsername().equals(username) && this.getAge() == age;
    }

    @Override
    public String toString() {
        return "Bruger ID: " + getId() + ", " + "Brugernavn: " + getUsername() + ", " + "Navn: " + getName() + ", " + "E-mail: " + getEmail() + ", " + "Rolle: " + getRole();
    }
}

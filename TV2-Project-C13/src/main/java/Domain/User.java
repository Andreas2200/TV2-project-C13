package Domain;

import Interfaces.UserInterface;

import java.time.LocalDate;
import java.time.Period;

public class User implements UserInterface {

    private String username;
    private String password;
    private String salt;
    private String name;
    private String role;
    private String email;
    private LocalDate birthday;


    public User(String username, String password, String salt,String name, String email, LocalDate birthday) {
        this.username = username.toLowerCase();
        this.password = password;
        this.salt = salt;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
        this.role = "User";
    }

    public User(String username, String password, String salt, String name, String email, LocalDate birthday, String role)
    {
        this(username,password,salt,name,email, birthday);
        this.role = role;
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

    public int getAge() {
        LocalDate today = LocalDate.now();
        Period p = Period.between(birthday,today);
        return p.getYears();
    }

    public LocalDate getBirthday()
    {
        return birthday;
    }

    public String getEmail(){return email;}

    public String getRole() {
        return role;
    }

    public String getSalt(){return salt;}

    public void setPassword(String newPas) {
        this.password = newPas;
    }

    public void changeName(String newName){this.name = newName;}

    public void setRole(String newRole){this.role = newRole;}

    public boolean checkInfo(String name, String username, int age)
    {
        return this.getName().equals(name) && this.getUsername().equals(username) && this.getAge() == age;
    }

    @Override
    public String toString() {
        return "" + getName() + ";" + getUsername() + ";" + getPassword() + ";" + getAge() + ";" + getRole() + ";" + getEmail() + ";";
    }
}

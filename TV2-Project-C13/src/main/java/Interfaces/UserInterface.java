package Interfaces;

import java.time.LocalDate;

public interface UserInterface {
    public String getName();

    public String getUsername();

    public String getPassword();

    public int getAge();

    public LocalDate getBirthday();

    public String getRole();

    public String getEmail();

    public String getSalt();

    public int getId();

    public void setPassword(String newPas);

    public void changeName(String newName);

    public void setRole(String newRole);

    public boolean checkInfo(String name, String username, int age);
}

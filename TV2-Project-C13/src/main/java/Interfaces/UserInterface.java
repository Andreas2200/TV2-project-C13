package Interfaces;

public interface UserInterface {
    public String getName();

    public String getUsername();

    public String getPassword();

    public int getAge();

    public String getRole();

    public void setPassword(String newPas);

    public void changeName(String newName);

    public void setRole(String newRole);

    public boolean checkInfo(String name, String username, int age);
}

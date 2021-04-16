package Domain;

public class User {
    private String name;
    private String username;
    private String password;
    private String email;
    private String role;
    private int age;

    public User(String name, String username, String password, int age, String email) {
        this.name = name;
        this.username = username.toLowerCase();
        this.password = password;
        this.age = age;
        this.email = email;
        this.role = "User";
    }

    public User(String name, String username, String password, int age, String email, String role)
    {
        this(name,username,password,age,email);
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
        return age;
    }

    public String getEmail(){return email;}

    public String getRole() {
        return role;
    }

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
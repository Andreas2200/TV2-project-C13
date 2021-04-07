package CLI;

public class Credit
{
    private String name;
    private String role;

    public Credit(String name, String role)
    {
        this.name = name;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "" + getName() + ";" + getRole() +";";
    }
}

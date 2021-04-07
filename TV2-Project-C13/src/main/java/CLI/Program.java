package CLI;

import java.util.ArrayList;

public class Program
{
    private String name;
    private String releaseDate;
    private String showedOn;
    private int id;
    private static int programID;

    private ArrayList<Credit> credits;

    public Program(String name, String releaseDate, String showedOn)
    {
        this.name = name;
        this.releaseDate = releaseDate;
        this.showedOn = showedOn;
        credits = new ArrayList<>();
        id = programID++;
    }

    public void addCredit(Credit credit)
    {
        credits.add(credit);
    }

    public String toSaveFile()
    {
        return "" + getName() + ";" + getReleaseDate() + ";" + getShowedOn() + ";";
    }

    @Override
    public String toString()
    {
        return "\nName: " + getName() + "\n" +
                "Showed on: " + getShowedOn() + "\n" +
                "Release date: " + getReleaseDate() + "\n" +
                "People in credits: " + getCreditSize() + "\n" +
                "ID: " + id + "\n";
    }

    public String getName() {
        return name;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getShowedOn()
    {
        return showedOn;
    }

    public int getId()
    {
        return id;
    }

    public int getCreditSize()
    {
        return credits.size();
    }
}

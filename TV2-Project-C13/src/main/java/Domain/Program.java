package Domain;

import Interfaces.CreditInterface;
import Interfaces.GenreInterface;
import Interfaces.ProgramInterface;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

public class Program implements ProgramInterface
{
    private String name;
    private String releaseDate;
    private int id;
    private static int programID;
    private LocalTime duration;
    private String genre;
    private String description;
    private int creatorID;

    private ArrayList<CreditInterface> credits;

    public Program(String name, String releaseDate, LocalTime duration, String genre, String description, int creatorID)
    {
        this.name = name;
        this.releaseDate = releaseDate;
        credits = new ArrayList<>();
        id = programID++;
        this.duration = duration;
        this.genre = genre;
        this.description = description;
        this.creatorID = creatorID;
    }

    public Program(int id, String name, String releaseDate, LocalTime duration, String genre, String description, int creatorID)
    {
        this.name = name;
        this.releaseDate = releaseDate;
        credits = new ArrayList<>();
        this.duration = duration;
        this.genre = genre;
        this.description = description;
        this.creatorID = creatorID;
        this.id = id;
    }

    public void addCredit(CreditInterface credit)
    {
        credits.add(credit);
    }

    public String toSaveFile()
    {
        return "" + getName() + ";" + getReleaseDate() + ";";
    }

    /*@Override
    public String toString()
    {
        return "\nName: " + getName() + "\n" +
                "Showed on: " + getShowedOn() + "\n" +
                "Release date: " + getReleaseDate() + "\n" +
                "People in credits: " + getCreditSize() + "\n" +
                "ID: " + id + "\n";
    }*/

    @Override
    public String toString()
    {
        return "Name: " + getName()  +
                "Release date: " + getReleaseDate() +
                "People in credits: " + getCreditSize() +
                "ID: " + id;
    }

    public String getName() {
        return name;
    }

    public String getReleaseDate() {
        return releaseDate.toString();
    }

    public int getId()
    {
        return id;
    }

    public int getCreditSize()
    {
        return credits.size();
    }

    //FIX ME not implemented
    public String getGenre() {
        return genre;
    }

    public LocalTime getDuration() {
        //return duration.toString();
        return duration;
    }

    public String getDescription(){
        return description;
    }

    public int getCreatorID() {
        return creatorID;
    }
}

package Module;

import CLI.Credit;
import Interfaces.CreditInterface;
import Interfaces.ProgramInterface;
import org.example.Genre;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

public class ProgramData implements ProgramInterface {
    private String name;
    private Date releaseDate;
    private String showedOn;
    private int id;
    private static int programID;
    private LocalTime duration;
    private ArrayList<Genre> genre;
    private String description;
    private int creatorID;

    private ArrayList<CreditInterface> credits;

    public ProgramData(String name, Date releaseDate, String showedOn, LocalTime duration, ArrayList<Genre> genre, String description, int creatorID)
    {
        this.name = name;
        this.releaseDate = releaseDate;
        this.showedOn = showedOn;
        credits = new ArrayList<>();
        id = programID++;
        this.duration = duration;
        this.genre = genre;
        this.description = description;
        this.creatorID = creatorID;
    }

    public ProgramData(int id, String name, Date releaseDate, String showedOn, LocalTime duration, ArrayList<Genre> genre, String description, int creatorID)
    {
        this.name = name;
        this.releaseDate = releaseDate;
        this.showedOn = showedOn;
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
        return releaseDate.getYear() + "," + releaseDate.getMonth() + "," + releaseDate.getDay();
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

    public ArrayList<Genre> getGenre() {
        return genre;
    }

    public String getDuration() {
        //return duration.toString();
        return "1,30,2";
    }

    public String getDescription(){
        return description;
    }

    public int getCreatorID() {
        return creatorID;
    }
}

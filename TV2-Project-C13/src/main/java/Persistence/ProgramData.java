package Persistence;

import Interfaces.CreditInterface;
import Interfaces.GenreInterface;
import Interfaces.ProgramInterface;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

public class ProgramData implements ProgramInterface {
    private String name;
    private String releaseDate;
    private LocalTime duration;
    private String genre;
    private String description;
    private int creatorID;

    private ArrayList<CreditInterface> credits;

    public ProgramData(String name, String releaseDate, LocalTime duration, String genre, String description, int creatorID)
    {
        this.name = name;
        this.releaseDate = releaseDate;
        credits = new ArrayList<>();
        this.duration = duration;
        this.genre = genre;
        this.description = description;
        this.creatorID = creatorID;
    }

    public void addCredit(CreditInterface credit)
    {
        credits.add(credit);
    }

    public String toSaveFile()
    {
        return "" + getName() + ";" + getReleaseDate() + ";";
    }

    @Override
    public String toString()
    {
        return "\nName: " + getName() + "\n" +
                "Release date: " + getReleaseDate() + "\n" +
                "People in credits: " + getCreditSize() + "\n";
    }

    public String getName() {
        return name;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public int getCreditSize()
    {
        return credits.size();
    }

    //FIX ME! not implemented
    public String getGenre()
    {
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

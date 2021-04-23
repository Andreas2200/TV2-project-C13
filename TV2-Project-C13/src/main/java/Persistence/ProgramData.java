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
    private String showedOn;
    private int id;
    private static int programID;
    private LocalTime duration;
    private ArrayList<GenreData> genre;
    private String description;
    private int creatorID;

    private ArrayList<CreditInterface> credits;

    public ProgramData(String name, String releaseDate, String showedOn, LocalTime duration, ArrayList<GenreData> genre, String description, int creatorID)
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

    public ProgramData(int id, String name, String releaseDate, String showedOn, LocalTime duration, ArrayList<GenreData> genre, String description, int creatorID)
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

    //FIX ME! not implemented
    public ArrayList<GenreInterface> getGenre()
    {
        ArrayList<GenreInterface> genreReturnList = new ArrayList<>(genre);

        return genreReturnList;
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

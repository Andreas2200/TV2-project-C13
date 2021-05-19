package Domain;

import Interfaces.ProgramInterface;
import java.time.LocalTime;

public class Program implements ProgramInterface {
    private String name;
    private String releaseDate;
    private LocalTime duration;
    private String genre;
    private String description;
    private int creatorID;

    public Program(String name, String releaseDate, LocalTime duration, String genre, String description, int creatorID) {
        this.name = name;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.genre = genre;
        this.description = description;
        this.creatorID = creatorID;
    }


    public String toSaveFile() {
        return "" + getName() + ";" + getReleaseDate() + ";";
    }

    @Override
    public String toString() {
        return "Name: " + getName() +
                " Release date: " + getReleaseDate();
    }

    public String getName() {
        return name;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getGenre() {
        return genre;
    }

    public LocalTime getDuration() {
        return duration;
    }

    public String getDescription() {
        return description;
    }

    public int getCreatorID() {
        return creatorID;
    }
}

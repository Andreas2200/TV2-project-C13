package Interfaces;

import CLI.Credit;
import org.example.Genre;

import java.util.ArrayList;

public interface ProgramInterface {
    public void addCredit(CreditInterface credit);

    public String toSaveFile();

    public String getName();

    public String getReleaseDate();

    public String getShowedOn();

    public int getId();

    public int getCreditSize();

    public ArrayList<Genre> getGenre();

    public String getDuration();

    public String getDescription();

    public int getCreatorID();
}

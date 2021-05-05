package Interfaces;

import java.time.LocalTime;
import java.util.ArrayList;

public interface ProgramInterface {
    public void addCredit(CreditInterface credit);

    public String toSaveFile();

    public String getName();

    public String getReleaseDate();

    public int getId();

    public int getCreditSize();

    public String getGenre();

    public LocalTime getDuration();

    public String getDescription();

    public int getCreatorID();
}

package Interfaces;

import java.time.LocalTime;

public interface ProgramInterface {

    public String toSaveFile();

    public String getName();

    public String getReleaseDate();

    public String getGenre();

    public LocalTime getDuration();

    public String getDescription();

    public int getCreatorID();
}

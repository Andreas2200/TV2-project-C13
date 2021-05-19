package Interfaces;

import java.time.LocalDate;

public interface PersonInterface {
    public int getId();

    public int getAge();

    public String getEmail();

    public String getName();

    public LocalDate getBirthDate();

    @Override
    String toString();
}

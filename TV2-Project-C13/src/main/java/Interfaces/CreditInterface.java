package Interfaces;

import Persistence.OccupationData;

public interface CreditInterface {
    public String getOccupation();

    public PersonInterface getPerson();

    public ProgramInterface getProgram();

    public String getCharacterName();

    public int getCreatorId();
}

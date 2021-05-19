package Domain;

import Interfaces.CreditInterface;
import Persistence.PersonData;
import Persistence.ProgramData;

public class Credits implements CreditInterface {
    private String occupation;
    private Person person;
    private String characterName;
    private Program program;
    int createrID;

    public Credits(Person person, Program program, String occupation, String characterName, int createrID) {
        this.person = person;
        this.program = program;
        this.occupation = occupation;
        this.createrID = createrID;

        if (characterName.equals("")) {
            this.characterName = "";
        } else {
            this.characterName = characterName;
        }
    }

    public String getOccupation() {
        return occupation;
    }

    public Person getPerson() {
        return person;
    }

    public String getCharacterName() {
        return characterName;
    }

    public Program getProgram() {
        return program;
    }

    public int getCreatorId() {
        return createrID;
    }

    @Override
    public String toString() {
        return "Name: " + person.getName() + " Occupation: " + occupation + " Character name: " + characterName;
    }
}
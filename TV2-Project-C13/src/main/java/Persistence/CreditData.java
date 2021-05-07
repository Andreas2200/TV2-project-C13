package Persistence;

import Interfaces.CreditInterface;


public class CreditData implements CreditInterface {
    private String occupation;
    private PersonData person;
    private String characterName;
    private ProgramData program;
    int createrID;

    /*public CreditData(String occupation, PersonData person) {
        this.occupation = occupation;
        this.person = person;
        this.characterName = "N/A";
    }*/

    /*public CreditData(String occupation, PersonData person, String characterName) {
        this(occupation, person);

        if(characterName.equals(""))
        {
            this.characterName = "N/A";
            System.out.println("Credit Debug");
        }
        else
        {
            this.characterName = characterName;
        }
    }*/

    public CreditData(PersonData person, ProgramData program, String occupation, int createrID)
    {
        this.person = person;
        this.program = program;
        this.occupation = occupation;
        this.createrID = createrID;
        this.characterName = "N/A";
    }

    public CreditData(PersonData person, ProgramData program, String occupation, String characterName, int createrID) {
        this.person = person;
        this.program = program;
        this.occupation = occupation;
        this.createrID = createrID;
        this.characterName = characterName;

    }

    public String getOccupation() {
        return occupation;
    }

    public PersonData getPerson() {
        return person;
    }

    public String getCharacterName() {
        return characterName;
    }

    public ProgramData getProgram() {
        return program;
    }

    @Override
    public String toString() {
        return "Name: " + person.getName() + " Occupation: " + occupation + " Character name: " + characterName;
    }
}

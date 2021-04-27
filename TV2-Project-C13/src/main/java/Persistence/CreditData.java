package Persistence;

import Interfaces.CreditInterface;


public class CreditData implements CreditInterface {
    private OccupationData occupation;
    private PersonData person;
    private String characterName;

    public CreditData(OccupationData occupation, PersonData person) {
        this.occupation = occupation;
        this.person = person;
        this.characterName = "N/A";
    }

    public CreditData(OccupationData occupation, PersonData person, String characterName) {
        this(occupation, person);
        this.characterName = characterName;
    }
    public OccupationData getOccupation() {
        return occupation;
    }

    public PersonData getPerson() {
        return person;
    }

    public String getCharacterName() {
        return characterName;
    }

    @Override
    public String toString() {
        return "Name: " + person.getName() + " Occupation: " + occupation + " Character name: " + characterName;
    }
}

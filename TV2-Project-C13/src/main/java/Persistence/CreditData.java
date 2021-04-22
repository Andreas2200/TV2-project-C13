package Persistence;

import Interfaces.CreditInterface;


public class CreditData implements CreditInterface {
    private OccupationData occupation;
    private PersonData person;
    private String characterName;

    public CreditData(OccupationData occupation, PersonData person) {
        this.occupation = occupation;
        this.person = person;
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

}

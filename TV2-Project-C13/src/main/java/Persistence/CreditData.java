package Persistence;

import Interfaces.CreditInterface;
import Domain.Occupation;

public class CreditData implements CreditInterface {
    private Occupation occupation;
    private PersonData person;
    private String characterName;

    public CreditData(Occupation occupation, PersonData person) {
        this.occupation = occupation;
        this.person = person;
    }

    public CreditData(Occupation occupation, PersonData person, String characterName) {
        this(occupation, person);
        this.characterName = characterName;
    }
    public Occupation getOccupation() {
        return occupation;
    }

    public PersonData getPerson() {
        return person;
    }

    public String getCharacterName() {
        return characterName;
    }

}

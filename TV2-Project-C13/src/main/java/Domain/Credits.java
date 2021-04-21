package Domain;

import Interfaces.CreditInterface;

public class Credits implements CreditInterface {
    private Occupation occupation;
    private Person person;
    private String characterName;

    public Credits(Occupation occupation, Person person) {
        this.occupation = occupation;
        this.person = person;
    }

    public Credits(Occupation occupation, Person person, String characterName) {
        this(occupation, person);
        this.characterName = characterName;
    }

    public Occupation getOccupation() {
        return occupation;
    }

    public Person getPerson() {
        return person;
    }

    public String getCharacterName() {
        return characterName;
    }
}
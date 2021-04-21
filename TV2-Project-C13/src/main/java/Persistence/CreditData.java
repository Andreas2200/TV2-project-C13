package Module;

import Interfaces.CreditInterface;
import org.example.Occupation;
import org.example.Person;

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

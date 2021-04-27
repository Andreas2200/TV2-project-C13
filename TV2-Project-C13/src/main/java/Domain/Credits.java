package Domain;

import Interfaces.CreditInterface;

public class Credits implements CreditInterface {
    private Occupation occupation;
    private Person person;
    private String characterName;

    public Credits(Occupation occupation, Person person) {
        this.occupation = occupation;
        this.person = person;
        this.characterName = "N/A";
    }

    public Credits(Occupation occupation, Person person, String characterName) {
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

    @Override
    public String toString() {
        return "Name: " + person.getName() + " Occupation: " + occupation + " Character name: " + characterName;
    }
}
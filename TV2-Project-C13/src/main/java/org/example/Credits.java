package org.example;

public class Credits {
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
}

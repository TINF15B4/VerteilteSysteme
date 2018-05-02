package de.tinf15b4.quizduell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class NameGenerator {
    private final String[] names =
            {"Ben", "Jonas", "Leon", "Paul", "Finn", "Noah", "Elias", "Luis", "Felix", "Lukas",
            "Henry", "Maximilian", "Luca", "Oskar", "Emil", "Anton", "Max", "Theo", "Jakob", "Matteo",
            "Liam", "Moritz", "Julian", "Leo", "David", "Alexander", "Milan", "Philipp", "Niklas", "Karl",
            "Tim", "Samuel", "Mats", "Tom", "Leonard", "Erik", "Jonathan", "Hannes", "Rafael", "Linus",
            "Jan", "Jonah", "Fabian", "Vincent", "Lennard", "Mika", "Adrian", "Till", "Simon", "Jannik",
            "Emma", "Hannah", "Mia", "Sophia", "Emilia", "Lina", "Anna", "Marie", "Mila", "Lea",
            "Leni", "Clara", "Lena", "Luisa", "Luisa", "Leonie", "Amelie", "Emily", "Johanna", "Ella", "Nele",
            "Sophie", "Charlotte", "Ida", "Lilly", "Laura", "Maja", "Mathilda", "Lara", "Frieda", "Lia",
            "Greta", "Lotta", "Sarah", "Melina", "Paula", "Julia", "Marlene", "Pia", "Alina", "Nora",
            "Elisa", "Victoria", "Mira", "Lisa", "Isabella", "Anni", "Juna", "Isabell", "Zoe", "Marah"};

    private HashMap<String, ArrayList<Character>> markovMap;

    private static final int CHAINLENGTH = 2;
    private static final int MINLENGTH = 5;
    private static final int MAXLENGTH = 15;


    public String getName() {
        return this.markovName(MINLENGTH, MAXLENGTH, CHAINLENGTH);
    }

    public String getNameWithNumbers(){
        return String.format("%s%05d", this.getName(), new Random().nextInt(100000));
    }

    public NameGenerator() {
        this.generateMap(CHAINLENGTH);
    }

    private Character getSuffix(String prefix) {
        if(this.markovMap.containsKey(prefix)) {
            ArrayList<Character> choices = this.markovMap.get(prefix);
            Random r = new Random();
            return choices.get(r.nextInt(choices.size()));
        }else{
            return '\n';
        }
    }

    private String markovName(int minLength, int maxLength, int chainLength) {
        String prefix = String.format("%" + chainLength + "s", "");
        Character suffix;
        StringBuilder name = new StringBuilder();

        int loops = 0;

        while (name.length() < maxLength) {
            suffix = getSuffix(prefix);
            if (suffix.equals('\n')) {
                if(name.length() >= minLength || loops > 100) {
                    break;
                } else{
                    loops++;
                }
            } else {
                name.append(suffix);
                prefix = prefix.substring(1) + suffix;
            }
        }
        return name.toString();
    }

    private void generateMap(int chainLength) {
        this.markovMap = new HashMap<String, ArrayList<Character>>();
        for (String name : names) {
            String strippedName = name.trim();
            String lengthenedName = String.format("%" + chainLength + "s", "") + strippedName;

            for (int n = 0; n < strippedName.length(); n++) {
                String prefix = lengthenedName.substring(n, n + chainLength);
                Character suffix = lengthenedName.charAt(n+chainLength);
                addKey(prefix, suffix);
            }

            // Add newline as end of name
            this.addKey(lengthenedName.substring(strippedName.length(), strippedName.length() + chainLength), '\n');
        }
    }

    private void addKey(String prefix, Character suffix) {
        if (this.markovMap.containsKey(prefix)) {
            this.markovMap.get(prefix).add(suffix);
        } else {
            ArrayList<Character> suffixList = new ArrayList<>();
            suffixList.add(suffix);
            this.markovMap.put(prefix, suffixList);
        }
    }

}

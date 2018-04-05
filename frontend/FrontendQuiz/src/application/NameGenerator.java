package application;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.HashMap;

public class NameGenerator {
    private final String[] names = {/* TODO */};
    private HashMap<String, ArrayList<Character>> markovMap;

    private static final int CHAINLENGTH = 2;
    private static final int MINLENGTH = 5;
    private static final int MAXLENGTH = 15;


    public String getName() {
        // TODO Call Markov Implementation
        throw new NotImplementedException();
    }

    public NameGenerator() {
        // TODO Setup Map for Lookup
    }

    private String markovName(int minLength, int maxLength, int chainLength) {
        // TODO Implement Algorithm
        throw new NotImplementedException();
    }

    private void generateMap(int chainLength) {
        // TODO Implement map generation from names

        for (String name : names) {
            String strippedName = name.trim();
            String lengthenedName = String.format("%" + chainLength + "s", "") + strippedName;

            for (int n = 0; n < strippedName.length(); n++) {
                String prefix = lengthenedName.substring(n, n+chainLength);
                Character suffix = lengthenedName.charAt(n);
                if (this.markovMap.containsKey(prefix)) {
                    this.markovMap.get(prefix).add(suffix);
                } else {
                    ArrayList<Character> suffixList = new ArrayList<>();
                    suffixList.add(suffix);
                    this.markovMap.put(prefix, suffixList);
                }
            }

            // Add newline as end of name
            this.markovMap.get(lengthenedName.substring(strippedName.length(), strippedName.length()+chainLength)).add('\n');
        }

    }

}

package com.pac.scramble;

public class PrefixComparator {
    public PrefixComparator() {
    }

    public int compare(String word, String prefix) {
//            System.err.println("prefix: \"" + prefix + "\"; word: \"" + word + "\"");
        if (word.startsWith(prefix)) {
            return 0;
        }
        else {
            return word.compareTo(prefix);
        }
    }
}
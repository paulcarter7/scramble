package com.pac.scramble;

import java.util.Comparator;

public class PrefixComparator
implements Comparator <String> {

    public int compare(String word, String prefix) {
        if (word.startsWith(prefix)) {
            return 0;
        }
        else {
            return word.compareTo(prefix);
        }
    }
}
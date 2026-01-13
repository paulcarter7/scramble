package com.pac.scramble;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: paulc
 * Date: 4/17/13
 * Time: 1:11 PM
 *
 * Brute force implementation stores words in a list and uses binary search to find them (or not)
 */
public class SimplePrefixDictionary
implements PrefixDictionary{

    private final Comparator<String> prefixComparator;

    private final String wordFile;

    private List<String> dictionary = null;

    public SimplePrefixDictionary(String wordFile) {
        this.wordFile = wordFile;
        this.dictionary = new ArrayList<String>();
        this.prefixComparator = new PrefixComparator();
    }

    @Override
    public void init()
    throws Exception {

        try (BufferedReader file = new BufferedReader(new FileReader(wordFile, StandardCharsets.UTF_8))) {
            String word;
            while ((word = file.readLine()) != null) {
                dictionary.add(word);
            }
        }
        Collections.sort(dictionary);
//        System.err.println(dictionary.size() + " words loaded in dictionary");

    }


    @Override
    public boolean hasPrefix(String prefix) {
        return Collections.binarySearch(dictionary, prefix, prefixComparator) >=0;
    }

    @Override
    public boolean hasWord(String word) {
        return Collections.binarySearch(dictionary, word)>=0;
    }
}

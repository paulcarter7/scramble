package com.pac.scramble;

import com.pac.trie.Trie;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Created with IntelliJ IDEA.
 * User: paulc
 * Date: 4/18/13
 * Time: 4:16 PM
 *
 * Use a Trie data structure to store the dictionary and do lookups
 */
public class TriePrefixDictionary
        implements PrefixDictionary {

    private final String wordFile;
    private Trie trie;

    public TriePrefixDictionary(String filename) {
        this.wordFile = filename;
        trie = new Trie();
    }

    @Override
    public void init()
    throws Exception {

        BufferedReader file = null;
        try {
            file = new BufferedReader(new FileReader(wordFile));
            String word = null;
            while ((word = file.readLine()) != null) {
                trie.insertWord(word);
            }
        }
        finally {
            file.close();
        }
    }

    @Override
    public boolean hasPrefix(String prefix) {
        return trie.hasPrefix(prefix);
    }

    @Override
    public boolean hasWord(String word) {
        return trie.findWord(word);
    }
}

package com.pac.trie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: paucarter
 * Date: 4/3/13
 * Time: 10:01 PM
 */
public class Trie {

    public static int OFFSET = 97; // normalize from 'a' to zero based array index of 0
    private TrieNode root = new TrieNode('0'); // special case, sentinel value

    public void insertWord(String word) {
        char[] letters = word.toCharArray();
        TrieNode current = root;
        for (int i = 0; i < letters.length; i++) {
            if (current.nodes[letters[i] - OFFSET] == null) {
                current.nodes[letters[i] - OFFSET] = new TrieNode((letters[i]));
            }
            current = current.nodes[letters[i] - OFFSET];
        }
        current.isFullWord = true;
        return;
    }

    public boolean findWord(String s) {
        TrieNode current = goToNode(s);
        return null != current && current.isFullWord;

    }

    public boolean hasPrefix(String s) {
        return goToNode(s) != null;
    }

    private TrieNode goToNode(String prefix) {
        TrieNode current = root;

        for (int i = 0; i < prefix.length(); i++) {
            if (current == null) {
                return null;
            }
            current = current.nodes[prefix.charAt(i) - OFFSET];
        }
        return current;

    }

    private List<String> findWordsWithPrefix(String prefix) {
        TrieNode current = goToNode(prefix);
        List<String> words = new ArrayList<String>();
        recurseTrieNodes(prefix, words, current);

        return words;
    }

    private void recurseTrieNodes(String prefix, List<String> words, TrieNode current) {

        if (current.isFullWord) {
            words.add(prefix);
        }
        TrieNode[] nodes = current.nodes;
        for (TrieNode node : nodes) {
            if (node != null) {
                recurseTrieNodes(prefix + node.letter, words, node);
            }
        }
    }

    /**
     * debugging
     */
    public void printTrie() {
        TrieNode[] nodes = root.nodes;
        for (TrieNode node : nodes) {
            System.err.println("node: " + node);
        }

    }

    private static class TrieNode {
        private char letter;
        private TrieNode[] nodes = null;
        private boolean isFullWord = false;

        private TrieNode(char c) {
            this.letter = c;
            nodes = new TrieNode[26];
        }
    }
}

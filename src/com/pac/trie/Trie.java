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

    public static class Test {
        private static int failed = 0;
        private static int count = 0;
        private static List<String> testFailures = new ArrayList<String>();

        public static void main(String[] args) {

            Trie t = new Trie();
            t.insertWord("blah");
            t.insertWord("bell");
            t.insertWord("bells");

            assertTrue(t.findWord("blah"), "should find 'blah'");
            assertTrue(!t.findWord("b"), "should not find 'b'");
            assertTrue(!t.findWord("bl"), "should not find 'bl'");
            assertTrue(!t.findWord("bla"), "should not find 'bla'");
            assertTrue(!t.findWord("blahs"), "should not find 'blahs'");
            assertTrue(!t.findWord("alloy"), "should not find 'alloy'");

            assertTrue(t.findWord("bell"), "should find 'bell'");
            assertTrue(t.findWord("bells"), "should find 'bells'");
            assertTrue(!t.findWord("balls"), "should not find 'balls'");

            List<String> words = t.findWordsWithPrefix("bell");
//            System.err.println("words, prefix(bell): " + words);
            assertTrue(words.size()==2, "size should be 2");
            assertTrue(words.contains("bells"), "prefix(bell): should contains 'bells'");
            assertTrue(words.contains("bell"), "prefix(bell): should contains 'bell'");

            words = t.findWordsWithPrefix("be");
            assertTrue(words.size()==2, "size should be 2");
            assertTrue(words.contains("bells"), "prefix(be): should contains 'bells'");
            assertTrue(words.contains("bell"), "prefix(be): should contains 'bell'");

            words = t.findWordsWithPrefix(("b"));
            assertTrue(words.size()==3, "size should be 3");
            assertTrue(words.contains("bells"), "prefix(b): should contains 'bells'");
            assertTrue(words.contains("bell"), "prefix(b): should contains 'bell'");
            assertTrue(words.contains("blah"), "prefix(b): should contains 'blah'");

            // should return all words
            words = t.findWordsWithPrefix("");
//            System.err.println("words: " + words);
            assertTrue(words.size() == 3, "all: size should be 3");

            summary();
        }

        public static void assertTrue(boolean exp, String s) {
            count++;
            if (!exp) {
                failed++;
                testFailures.add(s);
            }
        }

        public static void summary() {
            if (failed > 0) {
                System.err.println("TEST FAILURES:");
                for (String testFailure : testFailures) {
                    System.err.println("  FAILED: " + testFailure);
                }
            }
            else {
                System.err.println(count + " of " + count + " successful tests");
            }
        }
    }

}

package com.pac.trie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrieTest {

    private Trie trie;

    @BeforeEach
    void setUp() {
        trie = new Trie();
        trie.insertWord("blah");
        trie.insertWord("bell");
        trie.insertWord("bells");
    }

    @Nested
    @DisplayName("Word Finding Tests")
    class WordFindingTests {

        @Test
        @DisplayName("Should find inserted word 'blah'")
        void testFindBlah() {
            assertTrue(trie.findWord("blah"), "should find 'blah'");
        }

        @Test
        @DisplayName("Should find inserted word 'bell'")
        void testFindBell() {
            assertTrue(trie.findWord("bell"), "should find 'bell'");
        }

        @Test
        @DisplayName("Should find inserted word 'bells'")
        void testFindBells() {
            assertTrue(trie.findWord("bells"), "should find 'bells'");
        }

        @Test
        @DisplayName("Should not find partial word 'b'")
        void testNotFindB() {
            assertFalse(trie.findWord("b"), "should not find 'b'");
        }

        @Test
        @DisplayName("Should not find partial word 'bl'")
        void testNotFindBl() {
            assertFalse(trie.findWord("bl"), "should not find 'bl'");
        }

        @Test
        @DisplayName("Should not find partial word 'bla'")
        void testNotFindBla() {
            assertFalse(trie.findWord("bla"), "should not find 'bla'");
        }

        @Test
        @DisplayName("Should not find word with extra letters 'blahs'")
        void testNotFindBlahs() {
            assertFalse(trie.findWord("blahs"), "should not find 'blahs'");
        }

        @Test
        @DisplayName("Should not find non-existent word 'alloy'")
        void testNotFindAlloy() {
            assertFalse(trie.findWord("alloy"), "should not find 'alloy'");
        }

        @Test
        @DisplayName("Should not find non-existent word 'balls'")
        void testNotFindBalls() {
            assertFalse(trie.findWord("balls"), "should not find 'balls'");
        }
    }

    @Nested
    @DisplayName("Find Words With Prefix Tests")
    class FindWordsWithPrefixTests {

        private List<String> findWordsWithPrefix(String prefix) throws Exception {
            Method method = Trie.class.getDeclaredMethod("findWordsWithPrefix", String.class);
            method.setAccessible(true);
            @SuppressWarnings("unchecked")
            List<String> result = (List<String>) method.invoke(trie, prefix);
            return result;
        }

        @Test
        @DisplayName("Prefix 'bell' should return 2 words")
        void testPrefixBell() throws Exception {
            List<String> words = findWordsWithPrefix("bell");

            assertEquals(2, words.size(), "size should be 2");
            assertTrue(words.contains("bells"), "prefix(bell): should contain 'bells'");
            assertTrue(words.contains("bell"), "prefix(bell): should contain 'bell'");
        }

        @Test
        @DisplayName("Prefix 'be' should return 2 words")
        void testPrefixBe() throws Exception {
            List<String> words = findWordsWithPrefix("be");

            assertEquals(2, words.size(), "size should be 2");
            assertTrue(words.contains("bells"), "prefix(be): should contain 'bells'");
            assertTrue(words.contains("bell"), "prefix(be): should contain 'bell'");
        }

        @Test
        @DisplayName("Prefix 'b' should return 3 words")
        void testPrefixB() throws Exception {
            List<String> words = findWordsWithPrefix("b");

            assertEquals(3, words.size(), "size should be 3");
            assertTrue(words.contains("bells"), "prefix(b): should contain 'bells'");
            assertTrue(words.contains("bell"), "prefix(b): should contain 'bell'");
            assertTrue(words.contains("blah"), "prefix(b): should contain 'blah'");
        }

        @Test
        @DisplayName("Empty prefix should return all 3 words")
        void testEmptyPrefix() throws Exception {
            List<String> words = findWordsWithPrefix("");

            assertEquals(3, words.size(), "all: size should be 3");
        }
    }

    @Nested
    @DisplayName("Has Prefix Tests")
    class HasPrefixTests {

        @Test
        @DisplayName("Should have prefix 'b'")
        void testHasPrefixB() {
            assertTrue(trie.hasPrefix("b"));
        }

        @Test
        @DisplayName("Should have prefix 'bl'")
        void testHasPrefixBl() {
            assertTrue(trie.hasPrefix("bl"));
        }

        @Test
        @DisplayName("Should have prefix 'bla'")
        void testHasPrefixBla() {
            assertTrue(trie.hasPrefix("bla"));
        }

        @Test
        @DisplayName("Should have prefix 'bell'")
        void testHasPrefixBell() {
            assertTrue(trie.hasPrefix("bell"));
        }

        @Test
        @DisplayName("Should not have prefix 'x'")
        void testNotHasPrefixX() {
            assertFalse(trie.hasPrefix("x"));
        }

        @Test
        @DisplayName("Should not have prefix 'ba'")
        void testNotHasPrefixBa() {
            assertFalse(trie.hasPrefix("ba"));
        }
    }
}

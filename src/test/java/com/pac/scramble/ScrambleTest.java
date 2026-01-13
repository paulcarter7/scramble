package com.pac.scramble;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ScrambleTest {

    private static final Character[][] SCREAM_3X3 = {
            {'a', 'b', 'c'},
            {'d', 'e', 'f'},
            {'g', 'h', 'i'},
    };

    private static final Character[][] TEST_SCRAMBLE_BOARD = {
            {'l', 'e', 'n', 'o'},
            {'n', 's', 'i', 'm'},
            {'s', 't', 'a', 's'},
            {'e', 'r', 'g', 'v'},
    };

    private static final Character[][] TEST_SCRAMBLE_BOARD_WITH_QS = {
            {'i', 'q'},
            {'t', 'e'}
    };

    @Nested
    @DisplayName("Neighbor Finding Tests")
    class NeighborFindingTests {

        private Scramble scramble;

        @BeforeEach
        void setUp() {
            scramble = new Scramble(SCREAM_3X3, null);
        }

        @Test
        @DisplayName("Corner position (0,0) should have 3 neighbors")
        void testCornerPosition00() {
            Set<ScrambleCharacter> result = Scramble.findUnusedNeighbors(
                    new ScrambleCharacter('a', 0, 0), scramble.sb);

            assertEquals(3, result.size(), "size(0,0)");
            assertTrue(result.contains(new ScrambleCharacter('b', 0, 1)));
            assertTrue(result.contains(new ScrambleCharacter('e', 1, 1)));
            assertTrue(result.contains(new ScrambleCharacter('d', 1, 0)));
        }

        @Test
        @DisplayName("Center position (1,1) should have 8 neighbors")
        void testCenterPosition11() {
            Set<ScrambleCharacter> result = Scramble.findUnusedNeighbors(
                    new ScrambleCharacter('e', 1, 1), scramble.sb);

            assertEquals(8, result.size(), "size(1,1)");
            assertTrue(result.contains(new ScrambleCharacter('a', 0, 0)));
            assertTrue(result.contains(new ScrambleCharacter('b', 0, 1)));
            assertTrue(result.contains(new ScrambleCharacter('c', 0, 2)));
            assertTrue(result.contains(new ScrambleCharacter('d', 1, 0)));
            assertTrue(result.contains(new ScrambleCharacter('f', 1, 2)));
            assertTrue(result.contains(new ScrambleCharacter('g', 2, 0)));
            assertTrue(result.contains(new ScrambleCharacter('h', 2, 1)));
            assertTrue(result.contains(new ScrambleCharacter('i', 2, 2)));
        }

        @Test
        @DisplayName("Corner position (2,2) should have 3 neighbors")
        void testCornerPosition22() {
            Set<ScrambleCharacter> result = Scramble.findUnusedNeighbors(
                    new ScrambleCharacter('i', 2, 2), scramble.sb);

            assertEquals(3, result.size(), "size(2,2)");
            assertTrue(result.contains(new ScrambleCharacter('e', 1, 1)));
            assertTrue(result.contains(new ScrambleCharacter('h', 2, 1)));
            assertTrue(result.contains(new ScrambleCharacter('f', 1, 2)));
        }

        @Test
        @DisplayName("Edge position (1,2) should have 5 neighbors")
        void testEdgePosition12() {
            Set<ScrambleCharacter> result = Scramble.findUnusedNeighbors(
                    new ScrambleCharacter('f', 1, 2), scramble.sb);

            assertEquals(5, result.size(), "size(1,2)");
            assertTrue(result.contains(new ScrambleCharacter('c', 0, 2)));
            assertTrue(result.contains(new ScrambleCharacter('b', 0, 1)));
            assertTrue(result.contains(new ScrambleCharacter('e', 1, 1)));
            assertTrue(result.contains(new ScrambleCharacter('h', 2, 1)));
            assertTrue(result.contains(new ScrambleCharacter('i', 2, 2)));
        }

        @Test
        @DisplayName("Edge position (1,0) should have 5 neighbors")
        void testEdgePosition10() {
            Set<ScrambleCharacter> result = Scramble.findUnusedNeighbors(
                    new ScrambleCharacter('d', 1, 0), scramble.sb);

            assertEquals(5, result.size(), "size(1,0)");
            assertTrue(result.contains(new ScrambleCharacter('a', 0, 0)));
            assertTrue(result.contains(new ScrambleCharacter('b', 0, 1)));
            assertTrue(result.contains(new ScrambleCharacter('e', 1, 1)));
            assertTrue(result.contains(new ScrambleCharacter('h', 2, 1)));
            assertTrue(result.contains(new ScrambleCharacter('g', 2, 0)));
        }
    }

    @Nested
    @DisplayName("Factorial and Permutation Tests")
    class FactorialTests {

        @Test
        @DisplayName("Factorial calculations should be correct")
        void testFactorial() {
            assertEquals(2, Scramble.f(2), "2!");
            assertEquals(6, Scramble.f(3), "3!");
            assertEquals(24, Scramble.f(4), "4!");
            assertEquals(120, Scramble.f(5), "5!");
        }

        @Test
        @DisplayName("Word permutation calculations should be correct")
        void testWordPermutations() {
            assertEquals(2, Scramble.findWordPermutations(2), "2 letter combos");
            assertEquals(60, Scramble.findWordPermutations(4), "4 letter combos");
            assertEquals(986400, Scramble.findWordPermutations(9), "9 letter combos");
        }
    }

    @Nested
    @DisplayName("ScrambleCharacter Tests")
    class ScrambleCharacterTests {

        @Test
        @DisplayName("Equal characters should be equal")
        void testEquals() {
            ScrambleCharacter c = new ScrambleCharacter('a', 1, 1);
            ScrambleCharacter d = new ScrambleCharacter('a', 1, 1);

            assertEquals(c, d, "equals test");
            assertEquals(c, c, "equals self test");
        }

        @Test
        @DisplayName("Different letter should not be equal")
        void testNotEqualsDifferentLetter() {
            ScrambleCharacter c = new ScrambleCharacter('a', 1, 1);
            ScrambleCharacter d = new ScrambleCharacter('b', 1, 1);

            assertNotEquals(c, d, "not equals - different letter");
        }

        @Test
        @DisplayName("Different row should not be equal")
        void testNotEqualsDifferentRow() {
            ScrambleCharacter c = new ScrambleCharacter('a', 1, 1);
            ScrambleCharacter d = new ScrambleCharacter('a', 0, 1);

            assertNotEquals(c, d, "not equals - different row");
        }

        @Test
        @DisplayName("Different column should not be equal")
        void testNotEqualsDifferentColumn() {
            ScrambleCharacter c = new ScrambleCharacter('a', 1, 1);
            ScrambleCharacter d = new ScrambleCharacter('a', 1, 0);

            assertNotEquals(c, d, "not equals - different column");
        }
    }

    @Nested
    @DisplayName("PrefixComparator Tests")
    class PrefixComparatorTests {

        @Test
        @DisplayName("Binary search with prefix comparator should work")
        void testPrefixComparator() {
            Comparator<String> comparator = new PrefixComparator();
            List<String> words = new ArrayList<>();
            words.add("a");
            words.add("abcdefgh");
            words.add("acdezxy");

            assertEquals(1, Collections.binarySearch(words, "abc", comparator), "comparator test");
            assertNotEquals(1, Collections.binarySearch(words, "ad", comparator), "comparator test 2");
            assertNotEquals(1, Collections.binarySearch(words, "z", comparator), "comparator test 3");
        }
    }

    @Nested
    @DisplayName("ValueComparator Tests")
    class ValueComparatorTests {

        @Test
        @DisplayName("ValueComparator should sort by score descending")
        void testValueComparator() {
            Map<String, Integer> scores = new HashMap<>();
            scores.put("that", 20);
            scores.put("this", 10);
            scores.put("theother", 15);
            scores.put("holla", 15);

            assertEquals(4, scores.size(), "unsorted score map size");
            assertEquals(20, scores.get("that"), "simple get from scores");
            assertEquals(15, scores.get("holla"), "simple get2 from scores");

            Scramble.ValueComparator vc = new Scramble.ValueComparator(scores);
            Map<String, Integer> sortedMap = new TreeMap<>(vc);
            sortedMap.put("that", 20);
            sortedMap.put("this", 10);
            sortedMap.put("theother", 15);
            sortedMap.put("holla", 15);

            assertEquals(4, sortedMap.size(), "sorted map size");

            Iterator<Map.Entry<String, Integer>> iter = sortedMap.entrySet().iterator();
            assertEquals(20, iter.next().getValue(), "test first value");
            assertEquals(15, iter.next().getValue(), "test second value");
            assertEquals(15, iter.next().getValue(), "test third value");
            assertEquals(10, iter.next().getValue(), "test last value");
        }
    }

    @Nested
    @DisplayName("Word Scoring Tests")
    class WordScoringTests {

        @Test
        @DisplayName("Score for 'costarred' should be 33")
        void testCostarredScore() {
            assertEquals(33, new ScrambleWord("costarred").score());
        }

        @Test
        @DisplayName("Score for 'portered' should be 27")
        void testPorteredScore() {
            assertEquals(27, new ScrambleWord("portered").score());
        }

        @Test
        @DisplayName("Score for 'copters' should be 23")
        void testCoptersScore() {
            assertEquals(23, new ScrambleWord("copters").score());
        }

        @Test
        @DisplayName("Score for 'petard' should be 16")
        void testPetardScore() {
            assertEquals(16, new ScrambleWord("petard").score());
        }

        @Test
        @DisplayName("Score for 'dropt' should be 12")
        void testDroptScore() {
            assertEquals(12, new ScrambleWord("dropt").score());
        }

        @Test
        @DisplayName("Score for 'coax' should be 14")
        void testCoaxScore() {
            assertEquals(14, new ScrambleWord("coax").score());
        }

        @Test
        @DisplayName("Score for 'pew' should be 9")
        void testPewScore() {
            assertEquals(9, new ScrambleWord("pew").score());
        }

        @Test
        @DisplayName("Score for 'os' should be 1")
        void testOsScore() {
            assertEquals(1, new ScrambleWord("os").score());
        }

        @Test
        @DisplayName("Score for 'ox' should be 1")
        void testOxScore() {
            assertEquals(1, new ScrambleWord("ox").score());
        }
    }

    @Nested
    @DisplayName("Word Permutation Algorithm Tests")
    class PermutationAlgorithmTests {

        @Test
        @DisplayName("Dummy prefix implementation should find all permutations")
        void testDummyPrefixFindAllPermutations() {
            Character[][] twoByTwo = {
                    {'a', 'b'},
                    {'c', 'd'},
            };

            Scramble scramble = new Scramble(twoByTwo, new PrefixDictionary() {
                public boolean hasPrefix(String s1) {
                    return true;
                }

                public boolean hasWord(String s1) {
                    return true;
                }

                public void init() {
                }
            });

            Set<String> words = scramble.findScrambleWords();
            assertEquals(Scramble.findWordPermutations(4), words.size(),
                    "dummy prefix impl should find all word permutations for a 2x2 char board");
        }
    }

    @Nested
    @DisplayName("Dictionary Integration Tests")
    class DictionaryIntegrationTests {

        @Test
        @DisplayName("SimplePrefixDictionary should find 481 words")
        void testSimplePrefixDictionary() throws Exception {
            Scramble scramble = new Scramble(TEST_SCRAMBLE_BOARD,
                    new SimplePrefixDictionary("./resources/words.txt"));
            scramble.init();

            Set<String> words = scramble.findScrambleWords();
            assertEquals(481, words.size(), "SimplePrefixDictionary should find 481 words");

            verifyHighestScoringWords(scramble, "SimplePrefixDictionary");
        }

        @Test
        @DisplayName("TriePrefixDictionary should find 481 words")
        void testTriePrefixDictionary() throws Exception {
            Scramble scramble = new Scramble(TEST_SCRAMBLE_BOARD,
                    new TriePrefixDictionary("./resources/words.txt"));
            scramble.init();

            Set<String> words = scramble.findScrambleWords();
            assertEquals(481, words.size(), "TriePrefixDictionary should find 481 words");

            verifyHighestScoringWords(scramble, "TriePrefixDictionary");
        }

        @Test
        @DisplayName("TriePrefixDictionary with string constructor should find 481 words")
        void testTriePrefixDictionaryStringConstructor() throws Exception {
            Scramble scramble = new Scramble("lenonsimstasergv",
                    new TriePrefixDictionary("./resources/words.txt"));
            scramble.init();

            Set<String> words = scramble.findScrambleWords();
            assertEquals(481, words.size(), "TriePrefixDictionary (string constructor) should find 481 words");

            verifyHighestScoringWords(scramble, "TriePrefixDictionary (string constructor)");
        }

        private void verifyHighestScoringWords(Scramble scramble, String implementationName) {
            Set<Map.Entry<String, Integer>> entries = scramble.getWordsByHighestScoring().entrySet();
            Iterator<Map.Entry<String, Integer>> scoreIterator = entries.iterator();
            Map.Entry<String, Integer> score = scoreIterator.next();

            assertEquals("stramonies", score.getKey(),
                    implementationName + ": highest scoring word is stramonies");
            assertEquals(39, score.getValue(),
                    implementationName + ": highest scoring word is stramonies, score is 39");

            // Skip to 7th highest
            for (int i = 0; i < 6; i++) {
                score = scoreIterator.next();
            }

            assertEquals("assertion", score.getKey(),
                    implementationName + ": 7th highest scoring word is 'assertion'");
            assertEquals(30, score.getValue(),
                    implementationName + ": 7th highest scoring word is 'assertion', score is 30");
        }
    }

    @Nested
    @DisplayName("Q Word Handling Tests")
    class QWordTests {

        @Test
        @DisplayName("Should find 'quit' in Q board")
        void testFindQuit() throws Exception {
            Scramble scramble = new Scramble(TEST_SCRAMBLE_BOARD_WITH_QS,
                    new SimplePrefixDictionary("./resources/words.txt"));
            scramble.init();

            Set<String> qWords = scramble.findScrambleWords();
            assertTrue(qWords.contains("quit"), "should find quit");
        }

        @Test
        @DisplayName("Q words should be adjusted for length")
        void testQWordScoring() throws Exception {
            Scramble scramble = new Scramble(TEST_SCRAMBLE_BOARD_WITH_QS,
                    new SimplePrefixDictionary("./resources/words.txt"));
            scramble.init();
            scramble.findScrambleWords();

            TreeMap<String, Integer> qScores = scramble.getWordsByHighestScoring();
            assertEquals(13, qScores.get("quite"), "q words need to be adjusted for length");
        }
    }

    @Nested
    @DisplayName("Word Multiplier Tests")
    class WordMultiplierTests {

        @Test
        @DisplayName("Multiple word multipliers should be multiplicative")
        void testMultipleWordMultipliers() {
            ScrambleWord word = new ScrambleWord();

            ScrambleCharacter sc = new ScrambleCharacter('m', 0, 0);
            word.addLetter(sc);

            sc = new ScrambleCharacter('i', 1, 0);
            word.addLetter(sc);

            sc = new ScrambleCharacter('n', 2, 0);
            sc.setWordMultiplier(2);
            word.addLetter(sc);

            sc = new ScrambleCharacter('s', 3, 0);
            word.addLetter(sc);

            sc = new ScrambleCharacter('t', 4, 0);
            word.addLetter(sc);

            sc = new ScrambleCharacter('r', 5, 0);
            sc.setWordMultiplier(3);
            word.addLetter(sc);

            sc = new ScrambleCharacter('e', 5, 0);
            word.addLetter(sc);

            sc = new ScrambleCharacter('l', 6, 0);
            word.addLetter(sc);

            sc = new ScrambleCharacter('s', 7, 0);
            sc.setWordMultiplier(3);
            word.addLetter(sc);

            assertEquals(132, word.score(), "multiple multipliers are additive, word=" + word.getWord());
        }

        @Test
        @DisplayName("Word 'misparts' with multipliers should score 99")
        void testMispartsWithMultipliers() {
            ScrambleWord word = new ScrambleWord();

            ScrambleCharacter sc = new ScrambleCharacter('m', 0, 0);
            word.addLetter(sc);

            sc = new ScrambleCharacter('i', 1, 0);
            word.addLetter(sc);

            sc = new ScrambleCharacter('s', 2, 0);
            sc.setWordMultiplier(3);
            word.addLetter(sc);

            sc = new ScrambleCharacter('p', 3, 0);
            word.addLetter(sc);

            sc = new ScrambleCharacter('a', 4, 0);
            word.addLetter(sc);

            sc = new ScrambleCharacter('r', 5, 0);
            sc.setWordMultiplier(3);
            word.addLetter(sc);

            sc = new ScrambleCharacter('t', 6, 0);
            word.addLetter(sc);

            sc = new ScrambleCharacter('s', 7, 0);
            word.addLetter(sc);

            assertEquals(99, word.score(), "multiple multipliers are additive, word=" + word.getWord());
        }

        @Test
        @DisplayName("11 letter word 'reasserting' should score 39")
        void testReassertingScore() {
            ScrambleWord word = new ScrambleWord();

            word.addLetter(new ScrambleCharacter('r', 1, 0));
            word.addLetter(new ScrambleCharacter('e', 2, 0));
            word.addLetter(new ScrambleCharacter('a', 3, 0));
            word.addLetter(new ScrambleCharacter('s', 4, 0));
            word.addLetter(new ScrambleCharacter('s', 5, 0));
            word.addLetter(new ScrambleCharacter('e', 6, 0));
            word.addLetter(new ScrambleCharacter('r', 7, 0));
            word.addLetter(new ScrambleCharacter('t', 8, 0));
            word.addLetter(new ScrambleCharacter('i', 8, 0));
            word.addLetter(new ScrambleCharacter('n', 9, 0));
            word.addLetter(new ScrambleCharacter('g', 10, 0));

            assertEquals(39, word.score(), "11 letter word score: " + word.getWord());
        }
    }
}

package com.pac.scramble;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: paucarter
 * Date: 12/11/12
 * Time: 10:19 PM
 */
public class Scramble {


    private Set<ScrambleCharacter> used = new HashSet<ScrambleCharacter>();
    private Set<String> words = new TreeSet<String>();
    private Map<String,Integer> wordMap = new TreeMap<String,Integer>();
    private final PrefixDictionary dictionary;
	private ScrambleCharacter[][] sb = null;

    public static final String USAGE = "java com.pac.scramble.Scramble <16-character-input-board> ! for letter multipliers " +
			"and * for word multipliers";

    public static void main(String[] args)
            throws Exception {

        if(args.length == 1) {
            Scramble s = new Scramble(args[0], new TriePrefixDictionary("./resources/words.txt"));
            s.init();
            Set<String> words = s.findScrambleWords();
			System.err.println(s.prettyPrint());
			System.err.println("words.size: " + words.size());
			System.err.println(s.getWordsByHighestScoring());
		}
        else {
            System.err.println("USAGE: " + USAGE);
            System.exit(1);
        }
    }

	private String prettyPrint() {
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				s.append(sb[i][j].getString() + " ");
				if (j == 3) {
					s.append(System.getProperty("line.separator"));
				}
			}
		}
		return s.toString();
	}

	public Scramble(String input, PrefixDictionary dictionary) {
        this.dictionary = dictionary;
		this.sb = pb(input);
	}

    public Scramble(Character[][] board, PrefixDictionary dictionary) {
        this.dictionary = dictionary;
        this.sb = parseBoard(board);
    }

    public void init()
    throws Exception {
        dictionary.init();

    }

	public Map<String,Integer> getWordsByAlpha() {
		return wordMap;
	}

	public TreeMap<String,Integer> getWordsByHighestScoring() {
		ValueComparator valueComparator = new ValueComparator(wordMap);
		TreeMap<String, Integer> sortedScores = new TreeMap<String, Integer>(valueComparator);
		sortedScores.putAll(wordMap);

		return sortedScores;
	}

	private ScrambleCharacter[][] pb(String input) {
		ScrambleCharacter[][] b = new ScrambleCharacter[4][4];
		int k = 0;

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				char ch = input.charAt(k++);
				if (ch >= 'a' && ch <= 'z') {
					b[i][j] = new ScrambleCharacter(ch, i, j);
				}
				else if (ch >= '0' && ch <= '9') {
					// multiplier
					int multiplier = Integer.parseInt("" + ch);

					// next char is mult type word(*) or letter(!)
					char multiplierType = input.charAt(k++);
					ch = input.charAt(k++);
					ScrambleCharacter sc = new ScrambleCharacter(ch, i, j);
					if (multiplierType == '*') {
						sc.setWordMultiplier(multiplier);
					}
					else if (multiplierType == '!') {
						sc.setLetterMultiplier(multiplier);
					}
					else {
						throw new RuntimeException("bad multiplier value: " + multiplierType + "(board: " + input + ")");
					}
					b[i][j] = sc;
				}
			}
		}
		return b;
	}

	private ScrambleCharacter[][] parseBoard(Character[][] input) {
		ScrambleCharacter[][] board = new ScrambleCharacter[input.length][input[0].length];
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length; j++) {
                board[i][j] = new ScrambleCharacter(input[i][j], i, j);
            }
        }
        return board;
    }

    private static int findWordPermutations(int totalPossibleLetters) {
        // assumes square arrays for now
        int total = 0;
        // use n! / (n-r)!
        // words have to be at least 2 letters (r>= 2)
        int nFactorial = f(totalPossibleLetters);
        for (int i = totalPossibleLetters; i >= 2; i--) {
            total += (nFactorial / f(totalPossibleLetters - i));
        }

        return total;
    }

    // n!
    private static int f(int n) {
        if (n == 0) {
            return 1;
        }
        else {
            return n * f(n - 1);
        }
    }

    public Set<String> findScrambleWords() {

        for (int i = 0; i < sb.length; i++) {
            for (int j = 0; j < sb[i].length; j++) {
				used.clear();
//				ScrambleCharacter sc = new ScrambleCharacter(scrambleBoard[i][j], i, j);
//				recurse(new ScrambleCharacter(scrambleBoard[i][j], i, j),  new ScrambleWord(sc));
				recurse(sb[i][j],new ScrambleWord(sb[i][j]));
            }
        }

        return words;
    }

    /**
     * start at 0,0 and for each neighbor of that letter that hasn't already been used,
     * look up word in already found dictionary - continue if it's already there
     * then in word dictionary - add to found words if found
     * recurse
     */
    private void recurse(ScrambleCharacter scrambleCharacter, ScrambleWord scrambleWord) {
		used.add(scrambleCharacter);
        Set<ScrambleCharacter> neighbors = findUnusedNeighbors(scrambleCharacter, sb);
		for (ScrambleCharacter neighbor : neighbors) {

            ScrambleCharacter sc2 = sb[neighbor.getRow()][neighbor.getCol()];
            if (!used.contains(sc2)) {
                // check if has prefix
				ScrambleWord sw2 = scrambleWord.copy();
				sw2.addLetter(sc2);
				String prefix = sw2.getWord();
				if (dictionary.hasPrefix(prefix)) {
                    if (dictionary.hasWord(prefix)) {
                        words.add(prefix);
						addOrReplaceWord(sw2);
                    }
                    recurse(sc2, sw2);
                }
            }
        }
        used.remove(scrambleCharacter);

    }

    private void addOrReplaceWord(ScrambleWord word) {
		int score = word.score();
		if (!wordMap.containsKey(word.getWord())) {
			wordMap.put(word.getWord(), score);
			return;
		}
		if (score > wordMap.get(word.getWord())) {
			wordMap.put(word.getWord(), score);
		}
	}

    private static Set<ScrambleCharacter> findUnusedNeighbors(ScrambleCharacter scrambleCharacter, ScrambleCharacter[][] sb) {
        Set<ScrambleCharacter> a = new HashSet<ScrambleCharacter>();
        int row = scrambleCharacter.getRow();
        int col = scrambleCharacter.getCol();

        if (row - 1 >= 0) {
            if (col - 1 >= 0) {
                a.add(sb[row - 1][col - 1]);
            }
            a.add(sb[row - 1][col]);

            if (col + 1 < sb[row - 1].length) {
                a.add(sb[row - 1][col + 1]);
            }
        }

        if (col - 1 >= 0) {
            a.add(sb[row][col - 1]);
        }
        if (col + 1 < sb[row].length) {
            a.add(sb[row][col + 1]);
        }

        if (row + 1 < sb.length) {
            if (col - 1 >= 0) {
                a.add(sb[row + 1][col - 1]);
            }
            a.add(sb[row + 1][col]);

            if (col + 1 < sb[row + 1].length) {
                a.add(sb[row + 1][col + 1]);
            }
        }
		return a;
    }

    public static class Test {

        private static int count = 0;
        private static int succeeded = 0;
        private static int failed = 0;

        private static final Character[][] scream = {
                {'a', 'b', 'c'},
                {'d', 'e', 'f'},
                {'g', 'h', 'i'},
        };

        public static final Character[][] testScrambleBoard = {
                {'l', 'e', 'n', 'o'},
                {'n', 's', 'i', 'm'},
                {'s', 't', 'a', 's'},
                {'e', 'r', 'g', 'v'},
        };

        public static final Character[][] testScrambleBoardWithQs = {
                {'i', 'q'},
                {'t', 'e'}
        };

        public static void main(String[] args)
                throws Exception {
			Scramble scr = new Scramble(scream, null);
			Set<ScrambleCharacter> result = findUnusedNeighbors(new ScrambleCharacter('a', 0, 0), scr.sb);
			assertTrue(result.size() == 3, "size(0,0)");
			assertTrue(result.contains(new ScrambleCharacter('b', 0, 1)), "");
			assertTrue(result.contains(new ScrambleCharacter('e', 1, 1)), "");
			assertTrue(result.contains(new ScrambleCharacter('d', 1, 0)), "");

			result = findUnusedNeighbors(new ScrambleCharacter('e', 1, 1), scr.sb);
			assertTrue(result.size() == 8, "size(1,1)");
			assertTrue(result.contains(new ScrambleCharacter('a', 0, 0)), "");
			assertTrue(result.contains(new ScrambleCharacter('b', 0, 1)), "");
			assertTrue(result.contains(new ScrambleCharacter('c', 0, 2)), "");
			assertTrue(result.contains(new ScrambleCharacter('d', 1, 0)), "");
			assertTrue(result.contains(new ScrambleCharacter('f', 1, 2)), "");
			assertTrue(result.contains(new ScrambleCharacter('g', 2, 0)), "");
			assertTrue(result.contains(new ScrambleCharacter('h', 2, 1)), "");
			assertTrue(result.contains(new ScrambleCharacter('i', 2, 2)), "");

			result = findUnusedNeighbors(new ScrambleCharacter('i', 2, 2), scr.sb);
			assertTrue(result.size() == 3, "size(2,2)");
			assertTrue(result.contains(new ScrambleCharacter('e', 1, 1)), "");
			assertTrue(result.contains(new ScrambleCharacter('h', 2, 1)), "");
			assertTrue(result.contains(new ScrambleCharacter('f', 1, 2)), "");

			result = findUnusedNeighbors(new ScrambleCharacter('f', 1, 2), scr.sb);
			assertTrue(result.size() == 5, "size(1,2)");
			assertTrue(result.contains(new ScrambleCharacter('c', 0, 2)), "");
			assertTrue(result.contains(new ScrambleCharacter('b', 0, 1)), "");
			assertTrue(result.contains(new ScrambleCharacter('e', 1, 1)), "");
			assertTrue(result.contains(new ScrambleCharacter('h', 2, 1)), "");
			assertTrue(result.contains(new ScrambleCharacter('i', 2, 2)), "");

			result = findUnusedNeighbors(new ScrambleCharacter('d', 1, 0), scr.sb);
			assertTrue(result.size() == 5, "size(1,0)");
			assertTrue(result.contains(new ScrambleCharacter('a', 0, 0)), "");
			assertTrue(result.contains(new ScrambleCharacter('b', 0, 1)), "");
			assertTrue(result.contains(new ScrambleCharacter('e', 1, 1)), "");
			assertTrue(result.contains(new ScrambleCharacter('h', 2, 1)), "");
			assertTrue(result.contains(new ScrambleCharacter('g', 2, 0)), "");

			assertTrue(f(2) == 2, "2!");
			assertTrue(f(3) == 6, "3!");
			assertTrue(f(4) == 24, "4!");
			assertTrue(f(5) == 120, "5!");

			assertTrue(findWordPermutations(2) == 2, "2 letter combos");
			assertTrue(findWordPermutations(4) == 60, "4 letter combos");
			assertTrue(findWordPermutations(9) == 986400, "9 letter combos");

			ScrambleCharacter c = new ScrambleCharacter('a', 1, 1);
			ScrambleCharacter d = new ScrambleCharacter('a', 1, 1);
			assertTrue(c.equals(d), "equals test");
			assertTrue(c.equals(c), "equals test");

			d = new ScrambleCharacter('b', 1, 1);
			assertTrue(!c.equals(d), "not equals 1");

			d = new ScrambleCharacter('a', 0, 1);
			assertTrue(!c.equals(d), "not equals 2");

			d = new ScrambleCharacter('a', 1, 0);
			assertTrue(!c.equals(d), "not equals 3");

			Comparator<String> comparator = new PrefixComparator();
			List<String> s = new ArrayList<String>();
			s.add("a");
			s.add("abcdefgh");
			s.add("acdezxy");
			assertTrue(Collections.binarySearch(s, "abc", comparator) == 1, "comparator test");
			assertTrue(Collections.binarySearch(s, "ad", comparator) != 1, "comparator test 2");
			assertTrue(Collections.binarySearch(s, "z", comparator) != 1, "comparator test 3");

			Map<String, Integer> scores = new HashMap<String, Integer>();
			scores.put("that", 20);
			scores.put("this", 10);
			scores.put("theother", 15);
			scores.put("holla", 15);
			assertTrue(scores.size() == 4, "unsorted score map size");
			assertTrue(scores.get("that").equals(20), "simple get from scores");
			assertTrue(scores.get("holla").equals(15), "simple get2 from scores");

			ValueComparator vc = new ValueComparator(scores);
			Map<String, Integer> sortedMap = new TreeMap<String, Integer>(vc);
//            sortedMap.putAll(scores);
			sortedMap.put("that", 20);
			sortedMap.put("this", 10);
			sortedMap.put("theother", 15);
			sortedMap.put("holla", 15);

			assertTrue(sortedMap.size() == 4, "sorted map size");

			// this doesn't work and I don't quite get why... something to do with Comparator
			// not consistent with equals methinks grrrrrr
//            System.err.println(sortedMap.get("that"));
//            assertTrue(sortedMap.get("that").equals(20), "simple get");
			Iterator<Map.Entry<String, Integer>> iter = sortedMap.entrySet().iterator();

			assertTrue(iter.next().getValue() == 20, "test first value");
			assertTrue(iter.next().getValue() == 15, "test second value");
			assertTrue(iter.next().getValue() == 15, "test third value");
			assertTrue(iter.next().getValue() == 10, "test last value");

			assertTrue(new ScrambleWord("costarred").score() == 33, "costarred score");
			assertTrue(new ScrambleWord("portered").score() == 27, "portered score");
			assertTrue(new ScrambleWord("copters").score() == 23, "copters score");
			assertTrue(new ScrambleWord("petard").score() == 16, "petard score");
			assertTrue(new ScrambleWord("dropt").score() == 12, "dropt score");
			assertTrue(new ScrambleWord("coax").score() == 14, "coax score");
			assertTrue(new ScrambleWord("pew").score() == 9, "pew score");
			assertTrue(new ScrambleWord("os").score() == 1, "os score");
			assertTrue(new ScrambleWord("ox").score() == 1, "ox score");

			// quick test to see if our search algo can find all permutations
			// dummy implementation will treat all combinations as words
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
			assertTrue(words.size() == findWordPermutations(4), "dummy prefix impl should find: " +
                    findWordPermutations(4) + " word permutations for a 2x2 char board");

			Scramble theScramble = null;
            theScramble = new Scramble(testScrambleBoard, new SimplePrefixDictionary("./resources/words.txt"));
            testPrefixDictionaryImplementation(theScramble, "SimplePrefixDictionary");
//
            theScramble = new Scramble(testScrambleBoard, new TriePrefixDictionary("./resources/words.txt"));
            testPrefixDictionaryImplementation(theScramble, "TriePrefixDictionary");

			theScramble = new Scramble("lenonsimstasergv", new TriePrefixDictionary("./resources/words.txt"));
			testPrefixDictionaryImplementation(theScramble, "TriePrefixDictionary (string constructor)");
//
            theScramble = new Scramble(testScrambleBoardWithQs, new SimplePrefixDictionary("./resources/words.txt"));
            theScramble.init();
            Set<String> qWords = theScramble.findScrambleWords();
			assertTrue(qWords.contains("quit"), "should find quit");
			TreeMap<String,Integer> qScores = theScramble.getWordsByHighestScoring();
			assertTrue(13 == qScores.get("quite"), "q words need to be adjusted for length");

			theScramble = new Scramble("takjls3!e3!iaee3!xfr3*td", new TriePrefixDictionary("./resources/words.txt"));
			theScramble.init();
			TreeMap<String,Integer> pm  = theScramble.getWordsByHighestScoring();
			Map.Entry<String,Integer> e = pm.firstEntry();
//			assertTrue(e.getKey().equals("dextral"), "'dextral' is highest value word");
//			assertTrue(e.getValue().equals(106), "'106' is highest score");

//			assertTrue(329 == pm.size(), "solution map with multipliers");
//			assertTrue(93 == pm.get("extras"), "'extras' in multiplier puzzle");
//			System.err.println("pm.get(extras): " + pm.get("extras"));
//			assertTrue(87 == pm.get("extra"), "'extra' in multiplier puzzle");
//			assertTrue(pm.entrySet().iterator().next().getKey().equals("dextral"), "highest value in map");
//			summary();


//			System.err.println("16 char board (4x4) has " + findWordPermutations(16) + " possible permutations");

            testWordScoring();

        }

        private static void testWordScoring() {
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
            assertTrue(132 == word.score(), "multiple multipliers are additive, word=" + word.getWord());

            word = new ScrambleWord();
            sc = new ScrambleCharacter('m', 0, 0);
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
            assertTrue(99==word.score(), "multiple multipliers are additive, word=" + word.getWord());

            word = new ScrambleWord();
            sc = new ScrambleCharacter('r', 1, 0);
            word.addLetter(sc);
            sc = new ScrambleCharacter('e', 2, 0);
            word.addLetter(sc);
            sc = new ScrambleCharacter('a', 3, 0);
            word.addLetter(sc);
            sc = new ScrambleCharacter('s', 4, 0);
            word.addLetter(sc);
            sc = new ScrambleCharacter('s', 5, 0);
            word.addLetter(sc);
            sc = new ScrambleCharacter('e', 6, 0);
            word.addLetter(sc);
            sc = new ScrambleCharacter('r', 7, 0);
            word.addLetter(sc);
            sc = new ScrambleCharacter('t', 8, 0);
            word.addLetter(sc);
            sc = new ScrambleCharacter('i', 8, 0);
            word.addLetter(sc);
            sc = new ScrambleCharacter('n', 9, 0);
            word.addLetter(sc);
            sc = new ScrambleCharacter('g', 10, 0);
            word.addLetter(sc);
            assertTrue(39 == word.score(), "11 letter word score: " + word.getWord() + "; score="+word.score());

		}

        public static void testPrefixDictionaryImplementation(Scramble scramble, String implementationName)
                throws Exception {


            // quickie test run on a real puzzle for sanity
            // matched results with an actual scramble game
            scramble.init();
            long time = System.currentTimeMillis();
            Set<String> words = scramble.findScrambleWords();
			time = System.currentTimeMillis() - time;
            System.err.println("time for implementation " + implementationName + "=" + time);
            assertTrue(words.size() == 481, implementationName + " should find 481 words");

            Set<Map.Entry<String, Integer>> entries = scramble.getWordsByHighestScoring().entrySet();
            Iterator<Map.Entry<String, Integer>> scoreIterator = entries.iterator();
            Map.Entry<String, Integer> score = scoreIterator.next();

            // highest scoring element:
            assertTrue(score.getKey().equals("stramonies"), implementationName + ": highest scoring word is stramonies");
            assertTrue(score.getValue().equals(39), implementationName + ": highest scoring word is stramonies, score is 39");

            // 7th highest scoring element:
            score = scoreIterator.next();
			score = scoreIterator.next();
			score = scoreIterator.next();
			score = scoreIterator.next();
			score = scoreIterator.next();
			score = scoreIterator.next();
            assertTrue(score.getKey().equals("assertion"), implementationName + ": 7th highest scoring word is 'assertion'");
            assertTrue(score.getValue().equals(30), implementationName + ": 7th highest scoring word is 'assertion', score is 30");

//            183056 words loaded in dictionary
//            scores.size(): 481
//            time: 81
//            stramonies:39
//            monitress:33
//            monsteras:33
//            misenters:33
//            smartness:33
//            maitresse:32
//            assertion:30
//            gaminess:29
//            matiness:27
//            minsters:27
//            stasimon:27
//            etamines:27
//            misenter:27
//            samisens:27
//            monsters:27
//            monstera:27
//            smarties:26

        }

		public static void assertTrue(boolean expression, String s) {
            count++;
            if (!expression) {
                System.err.println("FAILED: " + s);
                failed++;
            }
            else {
//                System.err.println("SUCCEEDED: " + s);
                succeeded++;
            }
        }

        public static void summary() {
            System.err.println("Test Summary: " + count + " tests; " + succeeded + " succeeded; " + failed + " failed");
        }

    }

    private static class ValueComparator
            implements Comparator<String> {

        private Map<String, Integer> map = null;

        public ValueComparator(Map<String, Integer> map) {
            this.map = map;
        }

        public int compare(String s1, String s2) {
            if (map.get(s1) < (map.get(s2))) {
                return 1;
            }
			else if (map.get(s1) > map.get(s2)) {
				return -1;
			}
			return s1.compareTo(s2);
		}
    }
}

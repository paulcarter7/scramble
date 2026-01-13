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
	ScrambleCharacter[][] sb = null;

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
		return Collections.unmodifiableMap(wordMap);
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

    static int findWordPermutations(int totalPossibleLetters) {
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
    static int f(int n) {
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

        return Collections.unmodifiableSet(words);
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

    static Set<ScrambleCharacter> findUnusedNeighbors(ScrambleCharacter scrambleCharacter, ScrambleCharacter[][] sb) {
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

    static class ValueComparator
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

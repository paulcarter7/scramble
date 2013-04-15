package com.pac.scramble;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: paucarter
 * Date: 12/11/12
 * Time: 10:19 PM
 */
public class Scramble {

    public static final Character [] [] theScramble = {
            {'l', 'e', 'n', 'o'},
            {'n', 's', 'i', 'm'},
            {'s', 't', 'a', 's'},
            {'e', 'r', 'g', 'v'},
    };

    private static Set<ScrambleCharacter> used = new HashSet<ScrambleCharacter>();
    private static Set<String> strings = new TreeSet<String>();
    private static List<String> dictionary = new ArrayList<String>();
    private static Comparator<String> beginsWithComparator = new BeginsWithComparator();

    public static void main(String[] args)
    throws IOException {

        String fileName = args[0];

        BufferedReader file = null;
        try {
            file = new BufferedReader(new FileReader(fileName));
            String word = null;
            while ((word = file.readLine()) != null) {
                dictionary.add(word);
            }
        }
        catch (IOException ioe) {
            System.err.println("error reading file: " + fileName + ": " + ioe);
        }
        finally {
            file.close();
        }

        driver(theScramble);

        Map<String, Integer> scores = new HashMap<String, Integer>();
        for (String s : strings) {
            int score = scoreWord(s);
            scores.put(s, score);
        }
        ValueComparator valueComparator = new ValueComparator(scores);
        Map<String, Integer> sortedScores = new TreeMap<String, Integer>(valueComparator);

        sortedScores.putAll(scores);
        System.err.println("scores.size(): " + scores.size());
        for (Map.Entry<String, Integer> entry : sortedScores.entrySet()) {
            System.err.println(entry.getKey()+":"+entry.getValue());
        }
    }

    public static int scoreWord(String word) {
        int score = 0;
        if (word.length() == 2) {
            score=1;
        }
        else {
            for (int i = 0; i < word.length(); i++) {
                ScrambleEnum scrambleEnum = ScrambleEnum.valueOf("" + word.charAt(i));
                score+= scrambleEnum.value;
            }
        }

        switch (word.length()) {
            case 1:
            case 2:
            case 3:
            case 4:
                break;
            case 5:
                score+=3;
                break;
            case 6:
                score+=6;
                break;
            case 7:
                score+=10;
                break;
            case 8:
                score+=15;
                break;
            case 9:
                score+=20;
                break;
            case 10:
                score+=25;
                break;
            default:
                throw new RuntimeException("this word (" + word + ") is too long: " + word.length() + "; I only know" +
                        " how to score words up to 10 letters long");

        }
        // 5 letter word + 3 pts
        // 6 letter word + 6 pts
        // 7 letter word + 10 pts
        // 8 letter word + 15 pts
        // 9 letter word + 20 pts
        // 10 letter word + 25 pts
        return score;
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

    public static void driver(Character[][] scr) {
        for (int i = 0; i < scr.length; i++) {
            for (int j = 0; j < scr[i].length; j++) {
                used.clear();
                recurse(new ScrambleCharacter(scr[i][j], i, j), "" + scr[i][j]);
            }
        }

    }

    private static void recurse(ScrambleCharacter scrambleCharacter, String s) {

        used.add(scrambleCharacter);
        Set<ScrambleCharacter> neighbors = findUnusedNeighbors(scrambleCharacter, theScramble);
        for (ScrambleCharacter neighbor : neighbors) {

                ScrambleCharacter sc2 = new ScrambleCharacter(theScramble[neighbor.row][neighbor.col], neighbor.row, neighbor.col);
                if (!used.contains(sc2)) {
                    // check if has prefix
                    String prefix = s + sc2.scrambleEnum;
                    if(!hasPrefix(prefix)) {
//                        System.err.println("couldn't find: " + prefix);
                        continue;
                    }
                    else {
//                        System.err.println("found: " + prefix);
                        recurse(sc2, prefix);
                    }
                }
                else {
                    continue;
                }
        }
        used.remove(scrambleCharacter);

        // start at 0,0 and for each neighbor of that letter that hasn't already been used,
        //  look up word in already found dictionary - continue if it's already there
        //  then in word dictionary - add to found words if found
        //  recurse  //
    }

    public static boolean hasPrefix(String s) {
        // look up word in dictionary
        // add to words list

        // really sloppy performance-wise
        if (dictionary.contains(s)) {
            strings.add(s);
            return true;
        }
        else if(Collections.binarySearch(dictionary, s, beginsWithComparator) >=0) {
            return true;
        }
        else
            return false;
    }

    public static Set<ScrambleCharacter> findUnusedNeighbors(ScrambleCharacter scrambleCharacter, Character [] [] sc) {
        Set<ScrambleCharacter> a = new HashSet<ScrambleCharacter>();
        int row = scrambleCharacter.row;
        int col = scrambleCharacter.col;

        if (row - 1 >= 0) {
            if(col - 1 >= 0) {
                a.add(new ScrambleCharacter(sc[row - 1][col - 1], row-1, col-1));
            }
            a.add(new ScrambleCharacter(sc[row - 1][col], row - 1, col));

            if (col + 1 < sc[row - 1].length) {
                a.add(new ScrambleCharacter(sc[row - 1][col + 1], row - 1, col + 1));
            }
        }

        if(col - 1 >= 0) a.add(new ScrambleCharacter(sc[row][col - 1], row, col - 1));
        if(col + 1 < sc[row].length) a.add(new ScrambleCharacter(sc[row][col + 1], row, col + 1));

        if (row + 1 < sc.length) {
            if(col - 1 >= 0) {
                a.add(new ScrambleCharacter(sc[row + 1][col - 1], row + 1, col - 1));
            }
            a.add(new ScrambleCharacter(sc[row + 1][col], row + 1, col));

            if (col + 1 < sc[row + 1].length) a.add(new ScrambleCharacter(sc[row + 1][col + 1], row + 1, col + 1));
        }
        return a;
    }

    private static class ScrambleCharacter {
        private int row;
        private int col;
        private ScrambleEnum scrambleEnum;

        public ScrambleCharacter(char c, int row, int col) {
            scrambleEnum = ScrambleEnum.valueOf("" + c);
            this.row = row;
            this.col = col;
        }

        public String toString() {
            return new String (scrambleEnum.toString()+"("+row+","+col+"):"+ scrambleEnum.value);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ScrambleCharacter that = (ScrambleCharacter) o;

            if (scrambleEnum != that.scrambleEnum) return false;
            if (col != that.col) return false;
            if (row != that.row) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = row;
            result = 31 * result + col;
            result = 31 * result + scrambleEnum.hashCode();
            return result;
        }
    }

    public static class Test {

        private static int count = 0;
        private static int succeeded = 0;
        private static int failed = 0;

        private static final Character [][] scream = {
                {'a', 'b', 'c'},
                {'d', 'e', 'f'},
                {'g', 'h', 'i'},
        };

        public static void main(String[] args) {
            Set<ScrambleCharacter> result = findUnusedNeighbors(new ScrambleCharacter('a',0, 0), scream);
            assertTrue(result.size()==3, "size(0,0)");
            assertTrue(result.contains(new ScrambleCharacter('b',0,1)), "");
            assertTrue(result.contains(new ScrambleCharacter('e',1,1)), "");
            assertTrue(result.contains(new ScrambleCharacter('d',1,0)), "");

            result = findUnusedNeighbors(new ScrambleCharacter('e',1, 1), scream);
            assertTrue(result.size()==8, "size(1,1)");
            assertTrue(result.contains(new ScrambleCharacter('a',0,0)), "");
            assertTrue(result.contains(new ScrambleCharacter('b',0,1)), "");
            assertTrue(result.contains(new ScrambleCharacter('c',0,2)), "");
            assertTrue(result.contains(new ScrambleCharacter('d',1,0)), "");
            assertTrue(result.contains(new ScrambleCharacter('f',1,2)), "");
            assertTrue(result.contains(new ScrambleCharacter('g',2,0)), "");
            assertTrue(result.contains(new ScrambleCharacter('h',2,1)), "");
            assertTrue(result.contains(new ScrambleCharacter('i',2,2)), "");

            result = findUnusedNeighbors(new ScrambleCharacter('i',2, 2), scream);
            assertTrue(result.size()==3, "size(2,2)");
            assertTrue(result.contains(new ScrambleCharacter('e',1,1)), "");
            assertTrue(result.contains(new ScrambleCharacter('h',2,1)), "");
            assertTrue(result.contains(new ScrambleCharacter('f',1,2)), "");
//
            result = findUnusedNeighbors(new ScrambleCharacter('f',1, 2), scream);
            assertTrue(result.size()==5, "size(1,2)");
            assertTrue(result.contains(new ScrambleCharacter('c',0,2)), "");
            assertTrue(result.contains(new ScrambleCharacter('b',0,1)), "");
            assertTrue(result.contains(new ScrambleCharacter('e',1,1)), "");
            assertTrue(result.contains(new ScrambleCharacter('h',2,1)), "");
            assertTrue(result.contains(new ScrambleCharacter('i',2,2)), "");

            result = findUnusedNeighbors(new ScrambleCharacter('d',1, 0), scream);
            assertTrue(result.size()==5, "size(1,0)");
            assertTrue(result.contains(new ScrambleCharacter('a',0,0)), "");
            assertTrue(result.contains(new ScrambleCharacter('b',0,1)), "");
            assertTrue(result.contains(new ScrambleCharacter('e',1,1)), "");
            assertTrue(result.contains(new ScrambleCharacter('h',2,1)), "");
            assertTrue(result.contains(new ScrambleCharacter('g',2,0)), "");

            assertTrue(f(2)==2, "2!");
            assertTrue(f(3)==6, "3!");
            assertTrue(f(4)==24, "4!");
            assertTrue(f(5)==120, "5!");

            assertTrue(findWordPermutations(2)==2, "2 letter combos");
            assertTrue(findWordPermutations(4)==60, "4 letter combos");
            assertTrue(findWordPermutations(9)==986400, "9 letter combos");

            ScrambleCharacter c = new ScrambleCharacter('a', 1,1 );
            ScrambleCharacter d = new ScrambleCharacter('a', 1,1 );
            assertTrue(c.equals(d), "equals test");
            assertTrue(c.equals(c), "equals test");

            d = new ScrambleCharacter('b', 1,1 );
            assertTrue(!c.equals(d), "not equals 1");

            d = new ScrambleCharacter('a', 0,1 );
            assertTrue(!c.equals(d), "not equals 2");

            d = new ScrambleCharacter('a', 1,0 );
            assertTrue(!c.equals(d), "not equals 3");

            Comparator<String> comparator = new BeginsWithComparator();
            List<String> s = new ArrayList<String>();
            s.add("a");
            s.add("abcdefgh");
            s.add("acdezxy");
            assertTrue(Collections.binarySearch(s, "abc", comparator) == 1, "comparator test");
            assertTrue(Collections.binarySearch(s, "ad", comparator) !=1, "comparator test 2");
            assertTrue(Collections.binarySearch(s, "z", comparator) !=1, "comparator test 3");

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
            Iterator <Map.Entry<String,Integer>> iter = sortedMap.entrySet().iterator();

            assertTrue(iter.next().getValue() == 20, "test first value");
            assertTrue(iter.next().getValue() == 15, "test second value");
            assertTrue(iter.next().getValue() == 15, "test third value");
            assertTrue(iter.next().getValue() == 10, "test last value");

            assertTrue(scoreWord("costarred")==33, "costarred score");
            assertTrue(scoreWord("portered")==27, "portered score");
            assertTrue(scoreWord("copters")==23, "copters score");
            assertTrue(scoreWord("petard")==16, "petard score");
            assertTrue(scoreWord("dropt")==12, "dropt score");
            assertTrue(scoreWord("coax")==14, "coax score");
            assertTrue(scoreWord("pew")==9, "pew score");
            assertTrue(scoreWord("os")==1, "os score");
            assertTrue(scoreWord("ox")==1, "ox score");
            summary();

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
            System.err.println("" + count + " tests; " + succeeded + " succeeded; " + failed + " failed");
        }

    }

    public static class BeginsWithComparator
            implements Comparator<String> {

        public int compare(String word, String prefix) {
//            System.err.println("prefix: \"" + prefix + "\"; word: \"" + word + "\"");
            if(word.startsWith(prefix)) {
                return 0;
            }
            else {
                return word.compareTo(prefix);
            }
        }
    }

    private static class ValueComparator
            implements Comparator<String> {

        private Map<String,Integer> map = null;

        public ValueComparator(Map<String, Integer> map) {
            this.map = map;
        }

        public int compare(String s1, String s2) {
            if (map.get(s1) <= (map.get(s2))) {
                return 1;
            }
            return -1;
        }
    }
}

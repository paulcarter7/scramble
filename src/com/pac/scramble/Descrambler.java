package com.pac.scramble;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: paucarter
 * Date: 1/11/13
 * Time: 6:33 PM
 */
public class Descrambler
implements ScrambleDictionary{


    public Set<ScrambleCharacter> findUnusedNeighbors(ScrambleCharacter scrambleCharacter, Character [] [] sc) {
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
}

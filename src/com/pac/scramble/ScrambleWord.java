package com.pac.scramble;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by pcarter on 3/15/14.
 */
public class ScrambleWord {
	private List<ScrambleCharacter> letters = new LinkedList<ScrambleCharacter>();
	private int wordMultiplier = 1;
	private StringBuffer word = new StringBuffer();

	public ScrambleWord() {}

	public ScrambleWord(ScrambleCharacter character) {
		addLetter(character);
	}

	// only for testing since we're faking the indexes
	ScrambleWord(String str) {
		for (int i = 0; i < str.length(); i++) {
			addLetter(new ScrambleCharacter(str.charAt(i), i, 0));
		}
	}

	public ScrambleWord copy() {
		ScrambleWord w = new ScrambleWord();
		for (ScrambleCharacter letter : letters) {
			w.addLetter(letter);
		}
		return w;
	}

	public int score() {
		int score = 0;
		for (ScrambleCharacter letter : letters) {
			score += letter.score();
		}
        score = score * wordMultiplier;

		// Qu just counts as 1 letter
		int adjustedLength = word.length();

		if (word.toString().contains("q")) {
			adjustedLength= adjustedLength - 1;
		}

        switch (adjustedLength) {

			case 1:
				throw new RuntimeException("can't have 1 letter words: " + word);
			case 2:
				// short circuit - 2 letter words are always 1 pt
				return 1;
			case 3:
			case 4:
				break;
			case 5:
				score += 3;
				break;
			case 6:
				score += 6;
				break;
			case 7:
				score += 10;
				break;
			case 8:
				score += 15;
				break;
			case 9:
				score += 20;
				break;
			case 10:
				score += 25;
				break;
			case 11:
				score += 25;
				break;
			default:
                throw new RuntimeException("this word (" + word + ") is too long: " + word.length() + "; I only know" +
                        " how to score words up to 11 letters long");

		}

        return score;
	}

	public void addLetter(ScrambleCharacter character) {
		if (character.getWordMultiplier() != 1) {
            if (wordMultiplier != 1) {
                wordMultiplier += character.getWordMultiplier();
            }
            else {
                wordMultiplier = character.getWordMultiplier();
            }
        }
        assert !letters.contains(character);

		letters.add(character);
		word.append(character.getString());
	}

	public String getWord() {
		return word.toString();
	}

}

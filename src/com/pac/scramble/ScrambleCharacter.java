package com.pac.scramble;

/**
 * Created with IntelliJ IDEA.
 * User: paucarter
 * Date: 1/11/13
 * Time: 6:47 PM
 */
public class ScrambleCharacter {
	private int row;
	private int col;
	private ScrambleEnum scrambleEnum;
	private int letterMultiplier = 1;
	private int wordMultiplier = 1;

	public ScrambleCharacter(char c, int row, int col) {
		if (c != 'q') {
			scrambleEnum = ScrambleEnum.valueOf("" + c);
		}
		else {
			scrambleEnum = ScrambleEnum.qu;
		}
		this.row = row;
		this.col = col;
	}

	public String toString() {
		return new String(scrambleEnum.toString() + "(" + row + "," + col + "):" + scrambleEnum.value);
	}

	public String getString() {
		return scrambleEnum.toString();
	}
	public int getWordMultiplier() {
		return wordMultiplier;
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

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public int score() {
		return scrambleEnum.value * letterMultiplier;
	}

	@Override
	public int hashCode() {
		int result = row;
		result = 31 * result + col;
		result = 31 * result + scrambleEnum.hashCode();
		return result;
	}
}

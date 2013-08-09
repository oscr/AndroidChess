package io.oscr.androidchess.model;

//import static com.google.common.base.Preconditions.checkNotNull;
//import static com.google.common.base.Preconditions.checkArgument;
//import static com.google.common.base.Preconditions.checkPositionIndex;

import io.oscr.androidchess.utils.Constants;

import java.io.Serializable;

/**
 * TODO 
 * 
 * Immutable. 
 * 
 * Represents a position on the Chess board.  
 */
public final class BoardPosition implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String[] intToFileLetter = { "A", "B", "C", "D", "E", "F", "G", "H" };
	private static final String stringToInt = "ABCDEFGH";

	private final int file;
	private final int rank;

	/**
	 * TODO
	 * 
	 * Given a position in a string will create a representation of that position.
	 * 
	 * @throws NullPointerException if position is null. 
	 * @throws IllegalArgumentException if position argument isn't valid.
	 * @param position to represent.
	 */
	public BoardPosition(String position) {
		//checkNotNull(position, "Argument position was null. Expected non null");
		//checkArgument(position.length() == 2, "Argument position was incorrect length");
		
		position = position.toUpperCase();
		file = stringToInt.indexOf(position.charAt(0));

		// -1 because chess board coordinates aren't zero indexed
		rank = Integer.parseInt(position.substring(1)) - 1;

		//checkPositionIndex(file, Constants.BOARD_MAX_POSITION);
		//checkPositionIndex(rank, Constants.BOARD_MAX_POSITION);

	}

	/**
	 * Given a file and rank will create a representation of that position. 
	 * 
	 * <b>Note that file and rank are zero indexed!</b>
	 * @param file to represent. Zero indexed.
	 * @param rank to represent. Zero indexed.
	 */
	public BoardPosition(final int file, final int rank) {
		//checkArgument(file >= Constants.BOARD_MIN_POSITION, "Argument file is smaller that specified: %s", file);
		//checkArgument(rank >= Constants.BOARD_MIN_POSITION, "Argument rank is smaller that specified: %s", rank);
		
		//checkArgument(file <= Constants.BOARD_MAX_POSITION, "Argument file is larger that specified: %s", file);
		//checkArgument(rank <= Constants.BOARD_MAX_POSITION, "Argument rank is larger that specified: %s", rank);
		this.file = file;
		this.rank = rank;

	}

	/**
	 * Gives the zero indexed rank.
	 * 
	 * @return rank. 
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * Gives the zero indexed file.
	 * 
	 * @return file.
	 */
	public int getFile() {
		return file;
	}

	/**
	 * Two positions are equal if their file and rank are equal.
	 * 
	 */
	@Override
	public boolean equals(Object o) {
		if (o == null || o.getClass() != getClass())
			return false;

		BoardPosition that = (BoardPosition) o;
		return that.getFile() == file && that.getRank() == rank;

	}

	@Override
	public int hashCode() {
		return file * 3 + rank * 5;
	}

	@Override
	public String toString() {
		return intToFileLetter[file] + (rank + 1);
	}
	
}
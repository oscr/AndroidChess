package io.oscr.androidchess.model;

import io.oscr.androidchess.utils.Constants;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkPositionIndex;

/**
 * Immutable. Marked as final.
 *
 * Represents a position on the chess board. The internal representation is zero indexed while
 * providing toString method to obtain it in human readable form.
 */
public final class BoardPosition implements Serializable {
	// Used in order to convert file value to letter.
    private static final String[] intToFileLetter = { "A", "B", "C", "D", "E", "F", "G", "H" };

    // Used to convert a letter to int.
    private static final String stringToInt = "ABCDEFGH";

	private final int file;
	private final int rank;

	/**
	 * Given a position in a string will create a representation of that position.
	 * 
	 * @throws NullPointerException if position argument is null.
	 * @throws IllegalArgumentException if position argument is invalid length.
     * @throws IndexOutOfBoundsException if position is outside the bounds defined in Constants.
     * @see Constants for board limitations.
	 * @param position to represent.
	 */
	public BoardPosition(String position) {
		checkNotNull(position, "Argument position was null. Expected non null");
		checkArgument(position.length() == 2, "Argument position was incorrect length");
		
		position = position.toUpperCase();
		file = stringToInt.indexOf(position.charAt(0));

		// -1 because chess board coordinates aren't zero indexed
		rank = Integer.parseInt(position.substring(1)) - 1;

        checkPositionIndex(file, Constants.BOARD_MAX_POSITION);
		checkPositionIndex(rank, Constants.BOARD_MAX_POSITION);
	}

	/**
	 * Given a file and rank will create a representation of that position. <b>Note that file and rank
     * are zero indexed!</b>
     *
     * @throws IllegalArgumentException if file or rank is greater than or or smaller than limits
     * defined in Constants.
     * @see Constants for board limitations.
     * @param file to represent. Zero indexed.
	 * @param rank to represent. Zero indexed.
	 */
	public BoardPosition(final int file, final int rank) {
		checkArgument(file >= Constants.BOARD_MIN_POSITION, "Argument file is smaller that specified: %s", file);
		checkArgument(rank >= Constants.BOARD_MIN_POSITION, "Argument rank is smaller that specified: %s", rank);
		checkArgument(file <= Constants.BOARD_MAX_POSITION, "Argument file is larger that specified: %s", file);
		checkArgument(rank <= Constants.BOARD_MAX_POSITION, "Argument rank is larger that specified: %s", rank);

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
	 * Two BoardPosition objects are considered equal if their file and rank are equal. Equals
     * will use getClass() to ensure that it is invoked on two BoardPosition objects.
	 */
	@Override
	public boolean equals(Object o) {
		if (o == null || o.getClass() != getClass())
			return false;

		BoardPosition that = (BoardPosition) o;
		return that.file == file && that.rank == rank;
	}

    /**
     * The hash code is generated according to best practice using file and rank.
     */
	@Override
	public int hashCode() {
		return file * 3 + rank * 5;
	}

    /**
     * Provides a human readable representation of a position. Will convert the zero indexed
     * representation to the standard annotation.
     *
     * @return string representation.
     */
	@Override
	public String toString() {
		return intToFileLetter[file] + (rank + 1);
	}
}
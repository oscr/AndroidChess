package io.oscr.androidchess.model;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Immutable. Marked as final.
 *
 * Represents the moving of a piece from a position to another.
 * Therefore it contains two BoardPositions: from and to.
 */
public final class Move {
	public final BoardPosition from;
	public final BoardPosition to;

	/**
	 * Takes two BoardPosition's representing where the moved piece is
     * positioned (from) and where it shall be moved (to).
     *
     * @see BoardPosition
	 * @throws NullPointerException if from or to is null.
	 * @param from Where the piece is placed.
	 * @param to Where the piece should be moved.
	 */
	public Move(BoardPosition from, BoardPosition to){
 		checkNotNull(from, "Argument from was null. Expected non null");
 		checkNotNull(to, "Argument to was null. Expected non null");
		this.from = from;
		this.to = to;
	}
}
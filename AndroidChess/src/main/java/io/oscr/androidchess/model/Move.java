package io.oscr.androidchess.model;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Immutable. Represents the moving of a piece from a position to another.
 * Therefore it contains two BoardPositions: from and to.
 */
public final class Move {
	public final BoardPosition from;
	public final BoardPosition to;

	/**
	 * TODO
	 * 
	 * @throws NullPointerException if from or to is null.
	 * @param from
	 * @param to
	 */
	public Move(BoardPosition from, BoardPosition to){
 		checkNotNull(from, "Argument from was null. Expected non null");
 		checkNotNull(to, "Argument to was null. Expected non null");
		this.from = from;
		this.to = to;
	}
}
package io.oscr.androidchess.model.pieces;

/**
 * Defines all the different colors of chess pieces. Provides some auxiliary functions to increase
 * the readability of code in other places.
 */
public enum PieceColor {
	WHITE, BLACK;

    /**
     * Given a PieceColor it will return the opposite PieceColor.
     *
     * @param current PieceColor to change.
     * @return opposite PieceColor than argument.
     */
	public static PieceColor switchTurn(PieceColor current) {
		return current == WHITE ? BLACK : WHITE;

	}
}
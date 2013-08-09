package io.oscr.androidchess.model.pieces;

public enum PieceColor {
	WHITE, BLACK;

	public static PieceColor switchTurn(PieceColor current) {
		return current == WHITE ? BLACK : WHITE;

	}

	public static boolean isWhite(PieceColor current) {
		return current == WHITE;
	}

}
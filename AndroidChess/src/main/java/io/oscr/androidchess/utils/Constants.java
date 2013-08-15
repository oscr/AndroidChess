package io.oscr.androidchess.utils;

import io.oscr.androidchess.model.BoardPosition;
import io.oscr.androidchess.model.pieces.PieceColor;

/**
 * Contains all constants used by the application.
 */
public enum Constants {
	;

    public static final String[] FILES = {"A", "B", "C", "D", "E", "F", "G", "H"};
	
	// Note that these are zero index. This is actually a little strange now
	// when BoardPositions can be created with standard notation (E1, B4... etc)
	public static final int BOARD_MIN_POSITION = 0;
	public static final int BOARD_MAX_POSITION = 7;

    public static final int BOARD_SIZE = 8;

    // Defines home row for pawns
	public static final int WHITE_HOME_ROW = 1;
	public static final int BLACK_HOME_ROW = 6;

    // Move delta for pawns
	public static final int WHITE_PAWN_MOVE_DELTA = 1;
	public static final int BLACK_PAWN_MOVE_DELTA = -1;

    // Move delta for Rooks
	public static final int[][] ROOK_MOVE_DELTA = {{-1, 0},{1, 0},{0, -1},{0, 1}};

    // Move delta for Bishops
    public static final int[][] BISHOP_MOVE_DELTA = {{-1, 1},{1, -1},{1, 1},{-1, -1}};
	
	// Needed for Pawn promotion
	public static final int WHITE_PAWN_LAST_RANK = 7;
	public static final int BLACK_PAWN_LAST_RANK = 0;
	
    // Needed for implementing the castle rule
	// Constants for Black castle
	public static final BoardPosition BLACK_KING_START = new BoardPosition("E8");
    public static final BoardPosition B_KINGSIDE_KING = new BoardPosition("G8");
    public static final BoardPosition B_QUEENSIDE_KING = new BoardPosition("C8");
    public static final BoardPosition B_KINGSIDE_ROOK_END = new BoardPosition("F8");
    public static final BoardPosition B_QUEENSIDE_ROOK_END = new BoardPosition("D8");
    public static final BoardPosition B_KINGSIDE_ROOK_START = new BoardPosition("H8");
    public static final BoardPosition B_QUEENSIDE_ROOK_START = new BoardPosition("A8");

    public static final BoardPosition B_KINGSIDE_SECOND_EMPTY = new BoardPosition("G8");
    public static final BoardPosition B_KINGSIDE_FIRST_EMPTY = new BoardPosition("F8");
    
    public static final BoardPosition B_QUEENSIDE_FIRST_EMPTY = new BoardPosition("B8");
    public static final BoardPosition B_QUEENSIDE_SECOND_EMPTY = new BoardPosition("C8");
    public static final BoardPosition B_QUEENSIDE_THIRD_EMPTY = new BoardPosition("D8");
    
    // Constants for White castle
    public static final BoardPosition W_KINGSIDE_FIRST_EMPTY = new BoardPosition("G1");
    public static final BoardPosition W_KINGSIDE_SECOND_EMPTY = new BoardPosition("F1");
    
    public static final BoardPosition W_QUEENSIDE_FIRST_EMPTY = new BoardPosition("B1");
    public static final BoardPosition W_QUEENSIDE_SECOND_EMPTY = new BoardPosition("C1");
    public static final BoardPosition W_QUEENSIDE_THIRD_EMPTY = new BoardPosition("D1");
    
    public static final BoardPosition WHITE_KING_START = new BoardPosition("E1");
    public static final BoardPosition W_KINGSIDE_KING = new BoardPosition("G1");
    public static final BoardPosition W_QUEENSIDE_KING = new BoardPosition("C1");
    public static final BoardPosition W_KINGSIDE_ROOK_END = new BoardPosition("F1");
    public static final BoardPosition W_QUEENSIDE_ROOK_END = new BoardPosition("D1");
    public static final BoardPosition W_KINGSIDE_ROOK_START = new BoardPosition("H1");
    public static final BoardPosition W_QUEENSIDE_ROOK_START = new BoardPosition("A1");

    /**
     * Returns the home row for a pawn. This is needed when determining if the pawn has moved before
     * and hence can move two steps forward.
     *
     * @param color What sides home row is wanted.
     * @return the rank zero index which is the home row
     */
	public static int getHomeRow(PieceColor color) {
		return color == PieceColor.WHITE
				? Constants.WHITE_HOME_ROW
				: Constants.BLACK_HOME_ROW;
	}

    /**
     * Returns the movement delta for pawns for the specified color.
     *
     * @param color What sides delta is wanted.
     * @return the pawn movement delta.
     */
	public static int getMoveDelta(PieceColor color) {
		return color == PieceColor.WHITE
				? Constants.WHITE_PAWN_MOVE_DELTA
				: Constants.BLACK_PAWN_MOVE_DELTA;
	}
}
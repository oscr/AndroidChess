package io.oscr.androidchess.model.pieces;

/**
 * Defines all the different types of chess pieces on the board. Also provides a String representation
 * of the chess type via the toString method.
 */
public enum PieceType {
	PAWN, ROOK, KNIGHT, BISHOP, QUEEN, KING;

    /**
     * Returns a human readable representation of the type of Chess piece.
     *
     * @return String representation of the type.
     */
    @Override
    public String toString() {
        switch(this) {
            case PAWN: return "Pawn";
            case ROOK: return "Rook";
            case KNIGHT: return "Knight";
            case BISHOP: return "Bishop";
            case QUEEN: return "Queen";
            case KING: return "King";
            default: throw new IllegalArgumentException("Unknown piece type");
        }
    }
}

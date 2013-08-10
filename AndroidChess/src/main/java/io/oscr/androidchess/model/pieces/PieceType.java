package io.oscr.androidchess.model.pieces;

public enum PieceType {
	PAWN, ROOK, KNIGHT, BISHOP, QUEEN, KING;

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

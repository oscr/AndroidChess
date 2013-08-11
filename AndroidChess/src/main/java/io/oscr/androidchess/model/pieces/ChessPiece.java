package io.oscr.androidchess.model.pieces;

/**
 * Immutable. Marked as final.
 *
 * Represents a chess piece on the board.
 */
public final class ChessPiece implements IChessPiece {
	private final PieceColor color;
	private final PieceType type;

    /**
     * Creates a new ChessPiece.
     *
     * A chess piece is represented by it's color and type.
     *
     * @see PieceColor defines the different piece colors.
     * @see PieceType defines the different piece types.
     * @param color what color the piece has.
     * @param piece what type of piece it is.
     */
	public ChessPiece(final PieceColor color, final PieceType piece){
		this.color = color;
		this.type = piece;
	}

    /**
     * Returns the type of the piece that the object represents.
     *
     * @return the type of piece.
     */
	@Override
	public PieceType getPieceType() {
		return type;
	}

    /**
     * Returns the color of the piece that the object represents.
     *
     * @return the color of piece.
     */
	@Override
	public PieceColor getPieceColor() {
		return color;
	}
}
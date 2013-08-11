package io.oscr.androidchess.model.pieces;

/**
 * The specification used  the functionality for ChessPiece
 *
 */
public interface IChessPiece {

    /**
     * Returns the type of the piece that the object represents.
     *
     * @return the type of piece.
     */
    public abstract PieceType getPieceType();

    /**
     * Returns the color of the piece that the object represents.
     *
     * @return the color of piece.
     */
    public abstract PieceColor getPieceColor();
}

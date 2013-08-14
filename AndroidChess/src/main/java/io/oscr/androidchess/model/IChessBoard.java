package io.oscr.androidchess.model;

import io.oscr.androidchess.model.pieces.ChessPiece;
import io.oscr.androidchess.model.pieces.IChessPiece;
import io.oscr.androidchess.model.pieces.PieceColor;

/**
 * Specifies the behavior that is needed for the ChessBoard abstraction. It will keep the state
 * of large parts of the Chess game.
 */
public interface IChessBoard {

    /**
     * Checks if the given BoardPosition is empty (doesn't contain a chess piece).
     *
     * @param position to check if empty.
     * @return true if empty, otherwise false.
     */
    boolean isEmpty(BoardPosition position);

    /**
     * Checks if the position determined by file and rank is empty (doesn't contain a chess piece).
     *
     * @param file of position to check.
     * @param rank of position to check.
     * @return true if empty, otherwise false.
     */
    boolean isEmpty(int file, int rank);

    /**
     * Will set the King as moved if the King is placed on the parameter BoardPosition. By setting
     * the King as moved, castling isn't allowed for that color. Note that if the King isn't placed
     * on the specified BoardPosition no change is done.
     *
     * @param position to check if King moved to.
     */
	void setKingMoved(BoardPosition position);

    /**
     * Checks if the specified PieceColor's King has moved.
     *
     * @param pieceColor what PieceColor to check.
     * @return true if the specified colors King has moved, otherwise false.
     */
    boolean isKingMoved(PieceColor pieceColor);

    /**
     * Returns the chess piece placed on the specified BoardPosition. Will return null if no
     * piece is placed on that position.
     *
     * @param position to return chess piece from.
     * @return reference to the ChessPiece on position, otherwise null.
     */
	IChessPiece getChessPiece(BoardPosition position);

    /**
     * Returns the chess piece placed on the BoardPosition specified by the file and rank. Will
     * return null if no piece is placed on that position.
     *
     * @param file of position to check for piece.
     * @param rank of position to check for piece.
     * @return
     */
	IChessPiece getChessPiece(int file, int rank);

    /**
     * Returns the PieceColor of player whose turn it is.
     *
     * @return PieceColor representing the player who's turn it is.
     */
	PieceColor getTurn();

    /**
     * When invoked will change the turn to the opposite side (switch PieceColor).
     */
	void switchTurn();

    /**
     * Performs a move. Will move the chess piece on the BoardPosition from till the BoardPosition to.
     *
     * @param from where the chess piece is currently.
     * @param to where the chess piece should be moved to.
     */
	void move(BoardPosition from, BoardPosition to);

    /**
     * Promotes a pawn to another piece type.
     *
     * @param chessPiece what piece the pawn should be promoted to.
     * @param position where the pawn is currently.
     */
    void setPromotion(ChessPiece chessPiece, BoardPosition position);

    /**
     * Gives the BoardPosition that a pawn moves to when it captures by en passant. If no pawn
     * can be captured by en passant will return null.
     *
     * @return the BoardPosition that a pawn moves to in order to capture by en passant. If
     * no capture possible null.
     */
	BoardPosition getEnPassant();

    /**
     * Gives the BoardPosition of the pawn that can be captured by en passant. Should no pawn
     * be possible to capture by en passant it will return null.
     *
     * @return the BoardPosition of the pawn that can be captured by en passant. If
     * no capture possible null.
     */
    BoardPosition getEnPassantPawn();

    /**
     * Sets the GameBoard values needed for en passant.
     *
     * @param enPassant the position that a pawn will move to in order to capture.
     * @param enPassantPawn the pawn that will be captured by en passant.
     */
	void setEnPassant(BoardPosition enPassant, BoardPosition enPassantPawn);

    /**
     * Clears the en passant state that is set by setEnPassant
     *
     * @see IChessBoard#setEnPassant(BoardPosition, BoardPosition)
     */
    void removeEnPassantPawn();
}
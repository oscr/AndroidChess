package io.oscr.androidchess.model;

import io.oscr.androidchess.model.pieces.ChessPiece;
import io.oscr.androidchess.model.pieces.IChessPiece;
import io.oscr.androidchess.model.pieces.PieceColor;

public interface IChessBoard {
	boolean isEmpty(int file, int rank);

	void setKingMoved(BoardPosition position);

	IChessPiece getChessPiece(BoardPosition position);

	IChessPiece getChessPiece(int file, int rank);

	boolean isKingMoved(PieceColor pieceColor);

	boolean isEmpty(BoardPosition position);
	
	PieceColor getTurn();

	void switchTurn();

	void move(BoardPosition from, BoardPosition to, Move rookMove);

    void setPromotion(ChessPiece chessPiece, BoardPosition to);

	BoardPosition getEnPassant();

	void setEnPassant(BoardPosition enPassant, BoardPosition enPassantPawn);

	BoardPosition getEnPassantPawn();

    void removeEnPassantPawn();
}
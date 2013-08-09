package io.oscr.androidchess.model;

import io.oscr.androidchess.model.pieces.IChessPiece;
import io.oscr.androidchess.model.pieces.PieceColor;

public interface IChessBoard {
	boolean isEmpty(int file, int rank);

	void setKingMoved(BoardPosition position);

	void move(BoardPosition from, BoardPosition to);

	IChessPiece getChessPiece(BoardPosition position);

	IChessPiece getChessPiece(int file, int rank);

	boolean isKingMoved(PieceColor pieceColor);

	boolean isEmpty(BoardPosition position);
	
	PieceColor getTurn();

	void switchTurn();

	void move(BoardPosition from, BoardPosition to, Move rookMove);

	BoardPosition getEnPassant();

	void setEnPassant(BoardPosition enPassant, BoardPosition enPassantPawn);

	BoardPosition getEnPassantPawn();

}
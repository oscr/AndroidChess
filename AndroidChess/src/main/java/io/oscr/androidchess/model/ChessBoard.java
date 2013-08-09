package io.oscr.androidchess.model;

import java.util.Arrays;

import io.oscr.androidchess.model.pieces.PieceColor;
import io.oscr.androidchess.model.pieces.ChessPiece;
import io.oscr.androidchess.model.pieces.IChessPiece;
import io.oscr.androidchess.model.pieces.PieceType;
import io.oscr.androidchess.utils.Constants;

//import static com.google.common.base.Preconditions.checkNotNull;
//import static com.google.common.base.Preconditions.checkArgument;

public class ChessBoard implements IChessBoard {
	private IChessPiece[][] board = new IChessPiece[8][8];
	
	private BoardPosition enPassant = null;
	private BoardPosition enPassantPawn = null;
	
	private boolean whiteKingIsMoved = false;
	private boolean blackKingIsMoved = false;
	
	private PieceColor turn = PieceColor.WHITE;
	
	public ChessBoard(){
		board[0][0] = new ChessPiece(PieceColor.WHITE, PieceType.ROOK);
		board[7][0] = new ChessPiece(PieceColor.WHITE, PieceType.ROOK);
		board[1][0] = new ChessPiece(PieceColor.WHITE, PieceType.KNIGHT);
		board[6][0] = new ChessPiece(PieceColor.WHITE, PieceType.KNIGHT);
		board[2][0] = new ChessPiece(PieceColor.WHITE, PieceType.BISHOP);
		board[5][0] = new ChessPiece(PieceColor.WHITE, PieceType.BISHOP);
		board[3][0] = new ChessPiece(PieceColor.WHITE, PieceType.QUEEN);
		board[4][0] = new ChessPiece(PieceColor.WHITE, PieceType.KING);
		for(int i = 0; i < board.length; i++){
			board[i][1] = new ChessPiece(PieceColor.WHITE, PieceType.PAWN);
		}
		
		board[0][7] = new ChessPiece(PieceColor.BLACK, PieceType.ROOK);
		board[7][7] = new ChessPiece(PieceColor.BLACK, PieceType.ROOK);
		board[1][7] = new ChessPiece(PieceColor.BLACK, PieceType.KNIGHT);
		board[6][7] = new ChessPiece(PieceColor.BLACK, PieceType.KNIGHT);
		board[2][7] = new ChessPiece(PieceColor.BLACK, PieceType.BISHOP);
		board[5][7] = new ChessPiece(PieceColor.BLACK, PieceType.BISHOP);
		board[3][7] = new ChessPiece(PieceColor.BLACK, PieceType.QUEEN);
		board[4][7] = new ChessPiece(PieceColor.BLACK, PieceType.KING);
		for(int i = 0; i < board.length; i++){
			board[i][6] = new ChessPiece(PieceColor.BLACK, PieceType.PAWN);
		}
		
	}
	
	public ChessBoard(ChessBoard other){
		// Copy primitive data types
		this.whiteKingIsMoved = other.whiteKingIsMoved;
		this.blackKingIsMoved = other.blackKingIsMoved;
		this.enPassant = other.enPassant;
		this.enPassantPawn = other.enPassantPawn;
		this.turn = other.turn;
		
		// Deep copy array content
		for(int i = 0; i < board.length; i++){
			board[i] = Arrays.copyOf(other.board[i], other.board.length);
		}
		
	}
	
	@Override
	public boolean isEmpty(int file, int rank){

        // Because Google Guava crashes the emulator
        if(file < Constants.BOARD_MIN_POSITION || rank < Constants.BOARD_MIN_POSITION)
            throw new IllegalArgumentException();

        // Because Google Guava crashes the emulator
        if(file > Constants.BOARD_MAX_POSITION || rank > Constants.BOARD_MAX_POSITION)
            throw new IllegalArgumentException();

		//checkArgument(file >= Constants.BOARD_MIN_POSITION, "Argument file is smaller that specified: %s", file);
		//checkArgument(file <= Constants.BOARD_MAX_POSITION, "Argument file is larger that specified: %s", file);
		//checkArgument(rank >= Constants.BOARD_MIN_POSITION, "Argument rank is smaller that specified: %s", rank);
		//checkArgument(rank <= Constants.BOARD_MAX_POSITION, "Argument rank is larger that specified: %s", rank);
						
		return board[file][rank] == null;
	}
	
	
	@Override
	public void setKingMoved(BoardPosition position){
		IChessPiece piece = getChessPiece(position);
		if(piece.getPieceType() == PieceType.KING){
			if(piece.getPieceColor() == PieceColor.WHITE){
				whiteKingIsMoved = true;
			} else {
				blackKingIsMoved = true;
			}
		}
	}
	
	@Override
	public void move(BoardPosition from, BoardPosition to, Move rookMove){
		if(rookMove != null){
			moveRookCastle(rookMove.from, rookMove.to);
			
		}
		move(from, to);
	}
	
 	@Override
	public void move(BoardPosition from, BoardPosition to){

        if(from == null || to == null)
            throw new NullPointerException();

 		//checkNotNull(from, "Argument from was null. Expected non null");
 		//checkNotNull(to, "Argument to was null. Expected non null");
 		
 		// Move the piece from square
 		IChessPiece piece = board[from.getFile()][from.getRank()];
 		board[from.getFile()][from.getRank()] = null;
 		
 		// TODO Implement what pieces have been captured here
 		
 		// Place the piece on it's new position
 		board[to.getFile()][to.getRank()] = piece;
 		
 		
 	}
 	
 	@Override
 	public PieceColor getTurn(){
 		return turn;
 	}
 	
 	/*
 	 * The reason for a separate method to switch turn and not just have it in move() is 
 	 * because right now it makes the isCheck method much more efficient. It can be used 
 	 * to control that a user doesn't move a piece that places their own king in check 
 	 * and it can be used to see if a move places the opponent king in check.  
 	 */
 	@Override 
 	public void switchTurn(){
 		turn = PieceColor.switchTurn(turn);
 		
 	}
 	
 	private void moveRookCastle(BoardPosition p1, BoardPosition p2){
			board[p2.getFile()][p2.getRank()] = board[p1.getFile()][p1.getRank()];
			board[p1.getFile()][p1.getRank()] = null;
 	}
	
	@Override
	public IChessPiece getChessPiece(BoardPosition position) {
		//checkNotNull(position, "Argument was null. Expected non null");
		return getChessPiece(position.getFile(), position.getRank());
	}

	@Override
	public IChessPiece getChessPiece(int file, int rank) {
		//checkArgument(file >= Constants.BOARD_MIN_POSITION, "Argument file is smaller that specified: %s", file);
		//checkArgument(file <= Constants.BOARD_MAX_POSITION, "Argument file is larger that specified: %s", file);
		//checkArgument(rank >= Constants.BOARD_MIN_POSITION, "Argument rank is smaller that specified: %s", file);
		//checkArgument(rank <= Constants.BOARD_MAX_POSITION, "Argument rank is larger that specified: %s", file);

        // Because Google Guava crashes the emulator
        if(file < Constants.BOARD_MIN_POSITION || rank < Constants.BOARD_MIN_POSITION)
            throw new IllegalArgumentException();

        // Because Google Guava crashes the emulator
        if(file > Constants.BOARD_MAX_POSITION || rank > Constants.BOARD_MAX_POSITION)
            throw new IllegalArgumentException();

		return board[file][rank];
	}

	@Override
	public boolean isKingMoved(PieceColor pieceColor) {
		return pieceColor == PieceColor.WHITE 
				? whiteKingIsMoved
				: blackKingIsMoved;
	}

	@Override
	public boolean isEmpty(BoardPosition position) {
		return isEmpty(position.getFile(), position.getRank());
	}

	public void setPromotion(ChessPiece chessPiece, BoardPosition to) {
		board[to.getFile()][to.getRank()] = chessPiece;
		
	}
	
	public void removeEnPassantPawn(){
		board[enPassantPawn.getFile()][enPassantPawn.getRank()] = null;
	}

	@Override
	public void setEnPassant(BoardPosition enPassant, BoardPosition enPassantPawn) {
		this.enPassant = enPassant;
		this.enPassantPawn = enPassantPawn;
		
	}

	@Override
	public BoardPosition getEnPassantPawn() {
		return enPassantPawn;
	}
	
	@Override
	public BoardPosition getEnPassant(){
		return enPassant;
	}
}
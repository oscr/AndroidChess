package io.oscr.androidchess.model;

import java.util.Arrays;

import io.oscr.androidchess.model.pieces.PieceColor;
import io.oscr.androidchess.model.pieces.ChessPiece;
import io.oscr.androidchess.model.pieces.IChessPiece;
import io.oscr.androidchess.model.pieces.PieceType;

import static com.google.common.base.Preconditions.checkNotNull;

public class ChessBoard implements IChessBoard {
	private IChessPiece[][] board = new IChessPiece[8][8];
	
	private BoardPosition enPassant = null;
	private BoardPosition enPassantPawn = null;
	
	private boolean whiteKingIsMoved = false;
	private boolean blackKingIsMoved = false;
	
	private PieceColor turn = PieceColor.WHITE;

    /**
     * Creates a board in normal starting state.
     */
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

    /**
     * Copy constructor. Will instantiate the object with the same state as the parameter argument.
     *
     * @param other ChessBoard to copy state of.
     */
	public ChessBoard(final ChessBoard other){
		// Copy primitive data types and references
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

    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException if file or rank is outside board limits.
     */
	@Override
	public boolean isEmpty(final int file, final int rank){
		return isEmpty(new BoardPosition(file, rank));
	}

    /**
     * {@inheritDoc}
     * @throws NullPointerException if position is null.
     */
    @Override
    public boolean isEmpty(final BoardPosition position) {
        checkNotNull(position, "Argument from was null. Expected non null");
        return board[position.getFile()][position.getRank()] == null;
    }

    /**
     * {@inheritDoc}
     */
	@Override
	public void setKingMoved(final BoardPosition position){
		IChessPiece piece = getChessPiece(position);
		if(piece.getPieceType() == PieceType.KING){
			if(piece.getPieceColor() == PieceColor.WHITE){
				whiteKingIsMoved = true;
			} else {
				blackKingIsMoved = true;
			}
		}
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public void move(final BoardPosition from, final BoardPosition to){
        checkNotNull(from, "Argument from was null. Expected non null");
        checkNotNull(to, "Argument to was null. Expected non null");

 		// Move the piece from square
 		IChessPiece piece = board[from.getFile()][from.getRank()];
 		board[from.getFile()][from.getRank()] = null;

 		// TODO Implement what pieces have been captured here

 		// Place the piece on it's new position
 		board[to.getFile()][to.getRank()] = piece;
 	}

    /**
     * {@inheritDoc}
     */
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
    /**
     * {@inheritDoc}
     */
 	@Override
 	public void switchTurn(){
 		turn = PieceColor.switchTurn(turn);

 	}

    /**
     * {@inheritDoc}
     * @throws NullPointerException if position is null.
     */
	@Override
	public IChessPiece getChessPiece(final BoardPosition position) {
		checkNotNull(position, "Argument was null. Expected non null");
		return board[position.getFile()][position.getRank()];
	}

    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException if file or rank isn't within board limits.
     */
	@Override
	public IChessPiece getChessPiece(final int file, final int rank) {
		return getChessPiece(new BoardPosition(file, rank));
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public boolean isKingMoved(final PieceColor pieceColor) {
		return pieceColor == PieceColor.WHITE
				? whiteKingIsMoved
				: blackKingIsMoved;
	}

    /**
     * {@inheritDoc}
     */
    @Override
	public void setPromotion(final ChessPiece chessPiece, final BoardPosition to) {
		board[to.getFile()][to.getRank()] = chessPiece;
	}

    /**
     * {@inheritDoc}
     */
    @Override
	public void removeEnPassantPawn(){
		board[enPassantPawn.getFile()][enPassantPawn.getRank()] = null;
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public void setEnPassant(final BoardPosition enPassant, final BoardPosition enPassantPawn) {
		this.enPassant = enPassant;
		this.enPassantPawn = enPassantPawn;
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public BoardPosition getEnPassantPawn() {
		return enPassantPawn;
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public BoardPosition getEnPassant(){
		return enPassant;
	}
}
package io.oscr.androidchess.model;

import android.graphics.drawable.ColorDrawable;

import io.oscr.androidchess.model.event.PromotionEvent;
import io.oscr.androidchess.model.event.RedrawAllEvent;
import io.oscr.androidchess.model.event.RedrawEvent;
import io.oscr.androidchess.model.pieces.ChessPiece;
import io.oscr.androidchess.model.pieces.IChessPiece;
import io.oscr.androidchess.model.pieces.PieceColor;
import io.oscr.androidchess.model.pieces.PieceType;
import io.oscr.androidchess.model.theme.FunnyTheme;
import io.oscr.androidchess.model.theme.IChessTheme;
import io.oscr.androidchess.model.theme.NormalTheme;
import io.oscr.androidchess.utils.Constants;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Set;

public class ChessModel implements IObservable, IChessModel {
	private final PropertyChangeSupport observers = new PropertyChangeSupport(this);
	// TODO There is now an IChessBoard interface. Should migrate?
	private ChessBoard board;
	private IChessTheme chessTheme = new NormalTheme();

	private BoardPosition fromPosition;

	private PieceColor playing = PieceColor.WHITE;

	public ChessModel() {
		board = new ChessBoard();
	}

	@Override
	public void addObserver(final PropertyChangeListener observer) {
		observers.addPropertyChangeListener(observer);
	}

	@Override
	public void removeObserver(final PropertyChangeListener observer) {
		observers.removePropertyChangeListener(observer);
	}

	@Override
	public void selectPosition(BoardPosition position) {
		if (playing == board.getTurn()) {
			if (fromPosition == null) {
				// Make sure it's the players own piece that's selected
				IChessPiece piece = board.getChessPiece(position);
				if (piece != null && piece.getPieceColor() == playing) {
					fromPosition = position;
                    observers.firePropertyChange(null, false, new RedrawEvent(new BoardPosition[]{fromPosition}));
				}

			} else {
				if (position.equals(fromPosition)) {
					fromPosition = null;
                    observers.firePropertyChange(null, false, new RedrawEvent(new BoardPosition[]{position}));

				} else {
					Set<BoardPosition> legalMoves = MovementRules.getLegalMoves(fromPosition, board);
					if (legalMoves.contains(position) && isValidPosition(fromPosition, position)) {

						if (isPawnPromotion(fromPosition, position)) {
							observers.firePropertyChange(null, false, new PromotionEvent(fromPosition, position));

						} else {
							if (isCastlingMove(fromPosition, position)) {
								Move move = isLegalCastlingMove(fromPosition, position);
								if (move != null) {
									move(fromPosition, position, move);
									// Will mark the King as moved if it's moved
									board.setKingMoved(position);

                                    BoardPosition bp = fromPosition;
                                    fromPosition = null;
                                    observers.firePropertyChange(null, false, new RedrawEvent(new BoardPosition[]{bp, position, move.from, move.to}));
								}

							} else {
								// WARNING: This method has side effects. May
								// change game state!
								applyPawnLogic(position);

								move(fromPosition, position);
								// Will mark the King as moved if it's moved
								board.setKingMoved(position);

                                // This has to be done because if the fromPosition isn't null then it will be marked as selected
                                // when redrawing the
                                //
                                BoardPosition bp = fromPosition;
                                fromPosition = null;
                                observers.firePropertyChange(null, false, new RedrawEvent(new BoardPosition[]{bp, position}));

							}
						}
					}
				}
			}
		}
	}

	/*
	 * WARNING. This method creates a lot of side effects. 
	 * TODO En passant. There may be better way.
	 */
	public void applyPawnLogic(BoardPosition to) {
		// Pawn movement allows for en passant
		IChessPiece fromPiece = board.getChessPiece(fromPosition); 
		if(fromPiece.getPieceType() == PieceType.PAWN){
			final int HOME_ROW = Constants.getHomeRow(fromPiece);
			final int MOVE_DELTA = Constants.getMoveDelta(fromPiece);

            // Checks if an passant is possible and sets an passant state in gameboard
			if(fromPosition.getRank() == HOME_ROW && Math.abs((fromPosition.getRank() - to.getRank())) > 1 ){
				board.setEnPassant(new BoardPosition(to.getFile(), to.getRank() - MOVE_DELTA), to);
				// ATTENTION!
				return;
				
			} else if(to.equals(board.getEnPassant())){
				BoardPosition bp = board.getEnPassantPawn();
                board.removeEnPassantPawn();
                observers.firePropertyChange(null, false, new RedrawEvent(new BoardPosition[]{bp}));
			}
		
		}
		
		// For all moves except two square pawn move ->
		// remove the en passant.
		board.setEnPassant(null, null);

	}

	private Move isLegalCastlingMove(BoardPosition from, BoardPosition to) {
		if (from.equals(Constants.WHITE_KING_START)) {
			if (to.equals(Constants.W_KINGSIDE_KING)
					&& isLegalCastle(Constants.WHITE_KING_START, Constants.W_KINGSIDE_ROOK_START, 0)) {
				return new Move(Constants.W_KINGSIDE_ROOK_START, Constants.W_KINGSIDE_ROOK_END);

			} else if (to.equals(Constants.W_QUEENSIDE_KING)
					&& isLegalCastle(Constants.WHITE_KING_START, Constants.W_QUEENSIDE_ROOK_START, 1)) {
				return new Move(Constants.W_QUEENSIDE_ROOK_START, Constants.W_QUEENSIDE_ROOK_END);
			}

		} else if (from.equals(Constants.BLACK_KING_START)) {
			if (to.equals(Constants.B_KINGSIDE_KING)
					&& isLegalCastle(Constants.BLACK_KING_START, Constants.B_KINGSIDE_ROOK_START, 0)) {
				return new Move(Constants.B_KINGSIDE_ROOK_START, Constants.B_KINGSIDE_ROOK_END);

			} else if (to.equals(Constants.B_QUEENSIDE_KING)
					&& isLegalCastle(Constants.BLACK_KING_START, Constants.B_QUEENSIDE_ROOK_START, 1)) {
				return new Move(Constants.B_QUEENSIDE_ROOK_START, Constants.B_QUEENSIDE_ROOK_END);
			}
		}
		return null;

	}

	/*
	 * Ensure that no square between the King and Rook can be reached by opposing side. 
	 * If an opposing piece can reach an square between it's not a legal castle.
	 * Also the King may not castle out of check.
	 */
	private boolean isLegalCastle(BoardPosition from, BoardPosition to, int delta) {
		int start = Math.min(from.getFile(), to.getFile());
		int end = Math.max(from.getFile(), to.getFile());

		if (delta != 0) {
			start++;
			end++;
		}

		for (; start < end; start++) {
			BoardPosition position = new BoardPosition(start, from.getRank());
			for (int file = 0; file <= Constants.BOARD_MAX_POSITION; file++) {
				for (int rank = 0; rank <= Constants.BOARD_MAX_POSITION; rank++) {
					IChessPiece piece = board.getChessPiece(file, rank);
					if (piece != null && piece.getPieceColor() != board.getTurn()) {
						if (MovementRules.getLegalMoves(new BoardPosition(file, rank), board).contains(position)) {
							return false;
						}
					}
				}
			}
		}

		return true;
	}

	private boolean isCastlingMove(BoardPosition from, BoardPosition to) {
		if (from.equals(Constants.WHITE_KING_START)) {
			if (to.equals(Constants.W_KINGSIDE_KING)) {
				return true;

			} else if (to.equals(Constants.W_QUEENSIDE_KING)) {
				return true;
			}
		} else if (from.equals(Constants.BLACK_KING_START)) {
			if (to.equals(Constants.B_KINGSIDE_KING)) {
				return true;

			} else if (to.equals(Constants.B_QUEENSIDE_KING)) {
				return true;

			}

		}
		return false;

	}

	private boolean isPawnPromotion(BoardPosition from, BoardPosition to) {
		IChessPiece piece = board.getChessPiece(from);
		if (piece.getPieceType() == PieceType.PAWN) {
			if (to.getRank() == Constants.WHITE_PAWN_LAST_RANK || to.getRank() == Constants.BLACK_PAWN_LAST_RANK) {
				return true;

			}
		}
		return false;
	}

	@Override
	public ColorDrawable getBoardColor(BoardPosition boardPosition) {
		int file = boardPosition.getFile();
		int rank = boardPosition.getRank();

		if (isSelected(boardPosition)) {
			return chessTheme.getSelectedSquare();
		}

		if (file % 2 != 0) {
			if (rank % 2 == 0) {
				return chessTheme.getOddSquare();
			} else {
				return chessTheme.getEvenSquare();
			}

		} else {
			if (rank % 2 == 0) {
				return chessTheme.getEvenSquare();
			} else {
				return chessTheme.getOddSquare();
			}
		}
	}

	@Override
	public String getPieceString(BoardPosition position) {
		IChessPiece piece = board.getChessPiece(position);
		if (piece == null)
			return null;

		return chessTheme.getPieceString(piece);
	}

	@Override
	public void newGame(PieceColor white) {
		board = new ChessBoard();
		fromPosition = null;
		playing = PieceColor.WHITE;

		observers.firePropertyChange("", false, new RedrawAllEvent());

	}

	@Override
	public String getDisplayInformation() {
		String information = "Turn: " + board.getTurn();

		if (isCheckmate()) {
			return information + " is checkmate!";
		} else if (isCheck()) {
			return information + " is check!";
		} else {
			return information;
		}

	}

	/**
	 * This will check if the current player is in check
	 * 
	 * @return
	 */
	private boolean isCheck() {
		// Find the King
		BoardPosition kingPosition = null;
		for (int file = 0; file <= Constants.BOARD_MAX_POSITION; file++) {
			for (int rank = 0; rank <= Constants.BOARD_MAX_POSITION; rank++) {
				IChessPiece piece = board.getChessPiece(file, rank);
				if (piece != null && piece.getPieceType() == PieceType.KING && piece.getPieceColor() == board.getTurn()) {
					kingPosition = new BoardPosition(file, rank);
				}
			}
		}

		// If this ever happens... Disaster
		if (kingPosition == null) {
			throw new IllegalStateException("Could not find the " + board.getTurn() + " KING!!!");

		}

		// If any piece can capture the King then it's check
		for (int file = 0; file <= Constants.BOARD_MAX_POSITION; file++) {
			for (int rank = 0; rank <= Constants.BOARD_MAX_POSITION; rank++) {
				IChessPiece piece = board.getChessPiece(file, rank);
				if (piece != null && piece.getPieceColor() != board.getTurn()) {
					if (MovementRules.getLegalMoves(new BoardPosition(file, rank), board).contains(kingPosition)) {
						return true;
					}
				}
			}
		}

		return false;

	}

	private boolean isCheckmate() {
		if (isCheck()) {
			for (int file = 0; file < 8; file++) {
				for (int rank = 0; rank < 8; rank++) {
					IChessPiece piece = board.getChessPiece(file, rank);
					if (piece != null && piece.getPieceColor() == board.getTurn()) {
						BoardPosition from = new BoardPosition(file, rank);
						for (BoardPosition move : MovementRules.getLegalMoves(from, board)) {
							if (isValidPosition(from, move)) {
								return false;
							}
						}

					}
				}
			}
			return true;
		}
		return false;
	}

	private boolean isSelected(BoardPosition boardPosition) {
		return boardPosition.equals(fromPosition);

	}

	private void move(BoardPosition from, BoardPosition to) {
		// TODO En passant
		board.move(from, to);
		board.switchTurn();
		playing = PieceColor.switchTurn(playing);

	}

	private void move(BoardPosition from, BoardPosition to, Move move) {
		// TODO En passant
		board.move(from, to, move);
		board.switchTurn();
		playing = PieceColor.switchTurn(playing);

	}

	/*
	 * Used to see if a position is valid. In other words that the player
	 * doesn't move a piece that places the own king in check.
	 */
	private boolean isValidPosition(BoardPosition from, BoardPosition to) {
		ChessBoard backupBoard = new ChessBoard(board);
		board.move(from, to);
		boolean isValid = !isCheck();
		board = backupBoard;

		return isValid;
	}

	@Override
	public void changeColorTheme() {
		chessTheme = chessTheme.getClass() == NormalTheme.class ? new FunnyTheme() : new NormalTheme();
		observers.firePropertyChange("", false, new RedrawAllEvent());

	}

	@Override
	public void setPromotion(PieceType type, BoardPosition from, BoardPosition to) {
		PieceColor color = board.getTurn();
		move(from, to);
		board.setPromotion(new ChessPiece(color, type), to);
        fromPosition = null;
        observers.firePropertyChange(null, false, new RedrawEvent(new BoardPosition[]{from, to}));
	}
}
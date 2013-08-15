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

/*
- Detection of castle moves. Better to have some kind of flag?
 */


/**
 * Represents the model in the MVC model.
 *
 */
public class ChessModel implements IChessModel {
	private final PropertyChangeSupport observers = new PropertyChangeSupport(this);
	private IChessBoard board;
	private IChessTheme chessTheme = new NormalTheme();

	private BoardPosition fromPosition;

	private PieceColor playing = PieceColor.WHITE;

	public ChessModel() {
		board = new ChessBoard();
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public void addObserver(final PropertyChangeListener observer) {
		observers.addPropertyChangeListener(observer);
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public void removeObserver(final PropertyChangeListener observer) {
		observers.removePropertyChangeListener(observer);
	}

    /**
     * {@inheritDoc}
     */
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
								// WARNING: This method has side effects. May change game state!
								applyPawnLogic(position);

								move(fromPosition, position, null);
								// Will mark the King as moved if it's moved
								board.setKingMoved(position);

                                // This has to be done because if the fromPosition isn't null then it will be marked as selected
                                // when redrawing the
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

    /**
     * {@inheritDoc}
     */
	@Override
	public ColorDrawable getBoardColor(BoardPosition boardPosition) {
		int file = boardPosition.getFile();
		int rank = boardPosition.getRank();

		if (boardPosition.equals(fromPosition)) {
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

    /**
     * {@inheritDoc}
     */
	@Override
	public String getPieceString(BoardPosition position) {
		IChessPiece piece = board.getChessPiece(position);
		if (piece == null)
			return null;

		return chessTheme.getPieceString(piece);
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPromotion(final PieceType type, final BoardPosition from, final BoardPosition to) {
        PieceColor color = board.getTurn();
        move(from, to, null);
        board.setPromotion(new ChessPiece(color, type), to);
        fromPosition = null;
        observers.firePropertyChange(null, false, new RedrawEvent(new BoardPosition[]{from, to}));
    }

    /**
     * {@inheritDoc}
     */
	@Override
	public void newGame(PieceColor white) {
		board = new ChessBoard();
		fromPosition = null;
		playing = PieceColor.WHITE;
		observers.firePropertyChange("", false, new RedrawAllEvent());
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public void changeColorTheme() {
        chessTheme = chessTheme.getClass() == NormalTheme.class ? new FunnyTheme() : new NormalTheme();
        observers.firePropertyChange("", false, new RedrawAllEvent());
    }

    /**
     * {@inheritDoc}
     */
	@Override
	public String getDisplayInformation() {
		String information = "" + board.getTurn();

		if (isCheckmate()) {
			return information + " is checkmate!";
		} else if (isCheck()) {
			return information + " is check!";
		} else {
			return "Turn: " + information;
		}
	}

    /*
     * The following code is not exposed through the interface. It consists of helper methods that
     * are needed to implement the ChessModel.
     *
     */


	/**
	 * Will check if the current player defined by turn is in check.
	 *
	 * @return true if in check, otherwise false.
	 */
	private boolean isCheck() {
		// Find the King on the chess board
		BoardPosition kingPosition = null;
		for (int file = 0; file <= Constants.BOARD_MAX_POSITION; file++) {
			for (int rank = 0; rank <= Constants.BOARD_MAX_POSITION; rank++) {
				IChessPiece piece = board.getChessPiece(file, rank);
				if (piece != null && piece.getPieceType() == PieceType.KING && piece.getPieceColor() == board.getTurn()) {
					kingPosition = new BoardPosition(file, rank);
				}
			}
		}

		/*
		 * A King piece should always be found! If we can't find one there is something
		 * is very wrong. Therefore we make sure that the game is in an correct state.
		 */
		if (kingPosition == null) {
			throw new IllegalStateException("Could not find the " + board.getTurn() + " KING!!!");
		}

        /*
         * The following algorithm determines if the King is in check. It does so by checking if
         * any opposing piece can make a move that reaches the BoardPosition where the King is
         * placed.
         */
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

    /**
     * Will analyze the current game board and determine if there is a move that removes the current
     * player from check. If no such move the player is checkmate. If such a move exists the player
     * is just in check.
     *
     * @return true if current player is checkmate, otherwise false.
     */
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

    private void move(final BoardPosition from, final BoardPosition to, final Move rookMove) {
        if(rookMove != null)
            board.move(rookMove.from, rookMove.to);

        board.move(from, to);
        board.switchTurn();
        playing = PieceColor.switchTurn(playing);
    }

    /*
     * Analyzes the game state and sets the game into a state that allows for en passant
     * in the next move.
     */
    private void applyPawnLogic(BoardPosition to) {
        // Only a moved pawn may allow for en passant.
        IChessPiece fromPiece = board.getChessPiece(fromPosition);
        if(fromPiece.getPieceType() == PieceType.PAWN){
            final int HOME_ROW = Constants.getHomeRow(fromPiece.getPieceColor());
            final int MOVE_DELTA = Constants.getMoveDelta(fromPiece.getPieceColor());

            /* In order for en passant to be possible there are a number of criterias that need
             * to be met. The moving pawn must be 1) unmoved prior to this moved and 2) make a
             * two square move.
             *
             * Following code checks for the above mentioned criterias and sets the gameboard into
             * en passant state if it holds.
             */
            // Checks if an passant is possible and sets an passant state in gameboard
            if(fromPosition.getRank() == HOME_ROW && Math.abs((fromPosition.getRank() - to.getRank())) > 1 ){
                board.setEnPassant(new BoardPosition(to.getFile(), to.getRank() - MOVE_DELTA), to);
                // ATTENTION!
                return;

            /*
             * If the move that is made is an en passant capture we want to remove the pawn that set
             * the game into en passant state.
             */
            } else if(to.equals(board.getEnPassant())){
                BoardPosition bp = board.getEnPassantPawn();
                board.removeEnPassantPawn();
                observers.firePropertyChange(null, false, new RedrawEvent(new BoardPosition[]{bp}));
            }
        }
        // For all moves except two square pawn move we want to remove the en passant state.
        board.setEnPassant(null, null);
    }

    /*
     * Used to see if a position is valid. In other words that the player
     * doesn't move a piece that places the own king in check.
     */
    private boolean isValidPosition(final BoardPosition from, final BoardPosition to) {
        final ChessBoard backupBoard;
        // Important to check for null and it's the same class before
        // making a cast.
        if(board != null && board.getClass() == ChessBoard.class){
            backupBoard = new ChessBoard((ChessBoard)board);
        } else {
            throw new IllegalStateException("Board in unacceptable state (null or not ChessBoard class");
        }
        board.move(from, to);
        boolean isValid = !isCheck();
        board = backupBoard;

        return isValid;
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

    /**
     * Ensures that no square between the King and Rook can be reached by opposing side.
     * If an opposing piece can reach an square between it's not a legal castle.
     * Also the King may not castle out of check.
     *
     * @param from BoardPosition piece moving from.
     * @param to BoardPosition piece moving to,
     * @param delta What side to check for intercepting pieces.
     * @return true if legal castle move, otherwise false.
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

    /**
     * Will check if a castle move is being performed.
     *
     * @param from BoardPosition piece moving from.
     * @param to BoardPosition piece moving to,
     * @return true if castle move, otherwise false.
     */
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

    /**
     * Checks if a pawn has reached the last row. Will return true if it has, otherwise false.
     *
     * @param from BoardPosition piece moving from.
     * @param to BoardPosition piece moving to,
     * @return true if pawn is on the last row, otherwise false
     */
    private boolean isPawnPromotion(BoardPosition from, BoardPosition to) {
        IChessPiece piece = board.getChessPiece(from);
        if (piece.getPieceType() == PieceType.PAWN) {
            if (to.getRank() == Constants.WHITE_PAWN_LAST_RANK || to.getRank() == Constants.BLACK_PAWN_LAST_RANK) {
                return true;
            }
        }
        return false;
    }
}
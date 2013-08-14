package io.oscr.androidchess.model;

import io.oscr.androidchess.model.pieces.IChessPiece;
import io.oscr.androidchess.model.pieces.PieceColor;
import io.oscr.androidchess.model.pieces.PieceType;
import io.oscr.androidchess.utils.Constants;

import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Immutable. No side effects.
 *
 * MovementRules provides all movement logic for the game. In other words given a position and a
 * game board it will find all legal moves for the piece placed on that position.
 *
 * MovementRules is guaranteed to be side effect free. No changes will be made to the global game state.
 */
public enum MovementRules {
	;

    /**
     * Given a BoardPosition from and IGameBoard board will return all legal moves for the piece
     * placed on from square in the board with some limitations.
     *
     * The set that is returned contains all legal moves possible for the piece to perform, and
     * where there isn't a friendly piece blocking it from moving to that position. However the
     * move may place the King in check or fail to stop a check. This is not validated by this
     * method and needs to be checked separately. Since it's a set it's guaranteed to be unique.
     *
     * The method will throw NullPointerException if the from or board parameter is null. Also
     * if there is no IChessPiece in the position specified by from in board a NullPointerException
     * will be thrown.
     *
     * @throws NullPointerException is from, to or the from position in board is null.
     * @param from what position is the piece placed on.
     * @param board the current gameboard.
     * @return a set of legal BoardPosition.
     */
	public static Set<BoardPosition> getLegalMoves(BoardPosition from, IChessBoard board) {
		checkNotNull(from, "Argument from was null. Expected not null");
		checkNotNull(board, "Argument board was null. Expected not null");

		IChessPiece piece = board.getChessPiece(from);
        checkNotNull(piece, "Piece was null. Excepted not null");
		switch (piece.getPieceType()) {
		case PAWN:
			return getLegalPawnMoves(from, board);
		case ROOK:
			return getLegalRookMoves(from, board);
		case BISHOP:
			return getLegalBishopMoves(from, board);
		case KNIGHT:
			return getLegalKnightMoves(from, board);
		case QUEEN:
			return getLegalQueenMoves(from, board);
		case KING:
			return getLegalKingMoves(from, board);
		default:
			throw new IllegalStateException("Unknown piece type: " + piece.getPieceType());
		}
	}

    /**
     * Performs the task of calculating all legal moves for the King. The method checks if a castle
     * is possible to perform and adds it if it is.
     *
     * @param from what position is the piece placed on.
     * @param board the current GameBoard.
     * @return a set of legal BoardPosition.
     */
	private static Set<BoardPosition> getLegalKingMoves(BoardPosition from, IChessBoard board) {
		final Set<BoardPosition> lm = new HashSet<BoardPosition>();

        /*
         * Represents the "box" of moves the King can perform around itself.
         * The center (where the King is placed) isn't filtered but that isn't
         * a problem since a piece can't capture any piece of the same color.
         * Hence the King won't be able to capture himself :)
         */
        final int KING_MOVE_SQUARE_START = -1;
        final int KING_MOVE_SQUARE_END = 1;
        for (int i = KING_MOVE_SQUARE_START; i <= KING_MOVE_SQUARE_END; i++) {
            for (int j = KING_MOVE_SQUARE_START; j <= KING_MOVE_SQUARE_END; j++) {
				int file = from.getFile() + i;
				int rank = from.getRank() + j;
				addPositionIfValid(board, lm, from, file, rank);
			}
		}

        /*
         * The following code performs the necessary checks to see if a castle move is legal
         * and if so adds it to the set of legal moves. Important to note is that no check for
         * intermediate capture is performed here. However all squares between the rook and the
         * king are ensured to be empty.
         */
		final IChessPiece KING_PIECE = board.getChessPiece(from);

        // If the King has moved it can't castle
        if (!board.isKingMoved(KING_PIECE.getPieceColor())) {
            // In case King is white
			if (KING_PIECE.getPieceColor() == PieceColor.WHITE) {

                // Handles white castle king side.
                IChessPiece rookPiece = board.getChessPiece(Constants.W_KINGSIDE_ROOK_START);
				if (rookPiece != null && rookPiece.getPieceColor() == KING_PIECE.getPieceColor()
						&& rookPiece.getPieceType() == PieceType.ROOK
						&& board.isEmpty(Constants.W_KINGSIDE_SECOND_EMPTY)
						&& board.isEmpty(Constants.W_KINGSIDE_FIRST_EMPTY)) {
					lm.add(Constants.W_KINGSIDE_KING);
				}

                // Handles white castle queen side.
				rookPiece = board.getChessPiece(Constants.W_QUEENSIDE_ROOK_START);
				if (rookPiece != null && rookPiece.getPieceColor() == board.getChessPiece(from).getPieceColor()
						&& rookPiece.getPieceType() == PieceType.ROOK
						&& board.isEmpty(Constants.W_QUEENSIDE_FIRST_EMPTY)
						&& board.isEmpty(Constants.W_QUEENSIDE_SECOND_EMPTY)
						&& board.isEmpty(Constants.W_QUEENSIDE_THIRD_EMPTY)) {
					lm.add(Constants.W_QUEENSIDE_KING);
				}

            // In case King is Black
			} else {
                // Handles black castle king side.
				IChessPiece rookPiece = board.getChessPiece(Constants.B_KINGSIDE_ROOK_START);
				if (rookPiece != null && rookPiece.getPieceColor() == board.getChessPiece(from).getPieceColor()
						&& rookPiece.getPieceType() == PieceType.ROOK
						&& board.isEmpty(Constants.B_KINGSIDE_FIRST_EMPTY)
						&& board.isEmpty(Constants.B_KINGSIDE_SECOND_EMPTY)) {
					lm.add(Constants.B_KINGSIDE_KING);
				}

                // Handles black castle queen side.
				rookPiece = board.getChessPiece(Constants.B_QUEENSIDE_ROOK_START);
				if (rookPiece != null && rookPiece.getPieceColor() == board.getChessPiece(from).getPieceColor()
						&& rookPiece.getPieceType() == PieceType.ROOK
						&& board.isEmpty(Constants.B_QUEENSIDE_FIRST_EMPTY)
						&& board.isEmpty(Constants.B_QUEENSIDE_SECOND_EMPTY)
						&& board.isEmpty(Constants.B_QUEENSIDE_THIRD_EMPTY)) {
					lm.add(Constants.B_QUEENSIDE_KING);
				}
			}
		}
		return lm;
	}

	private static Set<BoardPosition> getLegalQueenMoves(BoardPosition from, IChessBoard board) {
		Set<BoardPosition> lm = new HashSet<BoardPosition>();

		for (int[] delta : Constants.BISHOP_MOVE_DELTA) {
			// The order of the delta content doesn't matter really
			addAllPosition(lm, board, from, delta[0], delta[1]);
		}

		for (int[] delta : Constants.ROOK_MOVE_DELTA) {
			// The order of the delta content doesn't matter really
			addAllPosition(lm, board, from, delta[0], delta[1]);
		}

		return lm;

	}

	private static Set<BoardPosition> getLegalRookMoves(BoardPosition from, IChessBoard board) {
		Set<BoardPosition> lm = new HashSet<BoardPosition>();

		for (int[] delta : Constants.ROOK_MOVE_DELTA) {
			// The order of the delta content doesn't matter really
			addAllPosition(lm, board, from, delta[0], delta[1]);
		}

		return lm;
	}

	private static void addAllPosition(final Set<BoardPosition> lm, final IChessBoard board, final BoardPosition from,
			final int DELTA_FILE, final int DELTA_RANK) {

		// Declaration
		for (int file = from.getFile() + DELTA_FILE, rank = from.getRank() + DELTA_RANK;
		// Condition
		Constants.BOARD_MIN_POSITION <= file && file <= Constants.BOARD_MAX_POSITION
				&& Constants.BOARD_MIN_POSITION <= rank && rank <= Constants.BOARD_MAX_POSITION;
		// Step
		file += DELTA_FILE, rank += DELTA_RANK) {

			if (board.isEmpty(file, rank)) {
				lm.add(new BoardPosition(file, rank));
				continue;

			}

			if (board.getChessPiece(file, rank).getPieceColor() != board.getChessPiece(from).getPieceColor()) {
				lm.add(new BoardPosition(file, rank));

			}
			break;

		}
	}

	private static Set<BoardPosition> getLegalKnightMoves(BoardPosition from, IChessBoard board) {
		Set<BoardPosition> lm = new HashSet<BoardPosition>();

		final int rank = from.getRank();
		final int file = from.getFile();

		// upper left
		addPositionIfValid(board, lm, from, file - 1, rank + 2);

		// upper right
		addPositionIfValid(board, lm, from, file + 1, rank + 2);

		// left upper
		addPositionIfValid(board, lm, from, file - 2, rank + 1);

		// left lower
		addPositionIfValid(board, lm, from, file - 2, rank - 1);

		// lower left
		addPositionIfValid(board, lm, from, file - 1, rank - 2);

		// lower right
		addPositionIfValid(board, lm, from, file + 1, rank - 2);

		// right upper
		addPositionIfValid(board, lm, from, file + 2, rank + 1);

		// right lower
		addPositionIfValid(board, lm, from, file + 2, rank - 1);

		return lm;
	}

	private static Set<BoardPosition> getLegalBishopMoves(BoardPosition from, IChessBoard board) {
		Set<BoardPosition> lm = new HashSet<BoardPosition>();

		for (int[] delta : Constants.BISHOP_MOVE_DELTA) {
			// The order of the delta content doesn't matter really
			addAllPosition(lm, board, from, delta[0], delta[1]);
		}
		return lm;
	}

	private static Set<BoardPosition> getLegalPawnMoves(final BoardPosition from, final IChessBoard board) {
		final Set<BoardPosition> lm = new HashSet<BoardPosition>();

		// If Pawn is on the last row it will be promoted.
		if (from.getRank() == Constants.WHITE_PAWN_LAST_RANK || from.getRank() == Constants.BLACK_PAWN_LAST_RANK) {
			return lm;
		}

		IChessPiece piece = board.getChessPiece(from);
		final int HOME_ROW = Constants.getHomeRow(piece);
		final int MOVE_DELTA = Constants.getMoveDelta(piece);

		final int file = from.getFile();
		final int rank = from.getRank();

		// Is en passant a possible move?
		BoardPosition position = board.getEnPassantPawn();
		if(position != null){
			// In order to en passant we need to be on the same rank. 
			if(from.getRank() == position.getRank() 
					// And on either 1 position higher or lower file
					&& (Math.abs(from.getFile() - position.getFile()) == 1)){
				lm.add(board.getEnPassant());
			}
		}
		
		// Check if square in front is available
		if (board.isEmpty(file, rank + MOVE_DELTA)) {
			lm.add(new BoardPosition(file, rank + MOVE_DELTA));

			// If we are on the starting row and nothing is in the way we can
			// take two steps forward
			if (rank == HOME_ROW && board.isEmpty(file, rank + 2 * MOVE_DELTA)) {
				lm.add(new BoardPosition(file, rank + 2 * MOVE_DELTA));
			}
		}

		// Check for capture left
		if (file > 0) {
			IChessPiece left = board.getChessPiece(file - 1, rank + MOVE_DELTA);
			if (left != null && left.getPieceColor() != piece.getPieceColor()) {
				lm.add(new BoardPosition(file - 1, rank + MOVE_DELTA));
			}
		}

		// Check for capture right
		if (file < 7) {
			IChessPiece right = board.getChessPiece(file + 1, rank + MOVE_DELTA);
			if (right != null && right.getPieceColor() != piece.getPieceColor()) {
				lm.add(new BoardPosition(file + 1, rank + MOVE_DELTA));
			}
		}

		return lm;
	}

	private static void addPositionIfValid(final IChessBoard board, final Set<BoardPosition> legalMoves,
			final BoardPosition from, final int toFile, final int toRank) {

		BoardPosition to;
		try {
			to = new BoardPosition(toFile, toRank);

		} catch (IllegalArgumentException iae) {
			return;
		}

		// Check if to square is either empty or opposite color so that it
		// may be captured.
		IChessPiece piece = board.getChessPiece(to);
		if (piece == null || piece.getPieceColor() != board.getChessPiece(from).getPieceColor()) {
			legalMoves.add(to);
		}
	}
}
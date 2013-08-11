package io.oscr.androidchess.test.model;

import junit.framework.TestCase;

import io.oscr.androidchess.model.BoardPosition;
import io.oscr.androidchess.model.ChessBoard;
import io.oscr.androidchess.model.IChessBoard;
import io.oscr.androidchess.model.pieces.IChessPiece;
import io.oscr.androidchess.model.pieces.PieceColor;
import io.oscr.androidchess.model.pieces.PieceType;
import io.oscr.androidchess.utils.Constants;

/**
 * TODO
 */
public class ChessBoardTest extends TestCase {
    private final String[] FILES = Constants.FILES;
    private IChessBoard board;

    public void setUp() {
        board = new ChessBoard();
    }

    public void test_changeTurnChangesTurn() throws Exception {
        PieceColor turn = board.getTurn();
        board.switchTurn();
        assertFalse(turn == board.getTurn());
        board.switchTurn();
        assertTrue(turn == board.getTurn());
    }

    /*
     * Checks the game state at the start of a game to ensure that
     * it's in a correct state.
     */
    public void test_ensureStartingProperties() throws Exception {
        // Check that the King isn't marked as moved
        assertFalse(board.isKingMoved(PieceColor.WHITE));
        assertFalse(board.isKingMoved(PieceColor.BLACK));

        // White always moves first
        assertTrue(board.getTurn() == PieceColor.WHITE);

        // Game isn't in "en passant" state.
        assertTrue(board.getEnPassant() == null);
        assertTrue(board.getEnPassantPawn() == null);
    }

    public void test_getWhiteKingProp() throws Exception {
        IChessPiece piece = board.getChessPiece(new BoardPosition("E1"));
        assertFalse(piece == null);
        assertTrue(piece.getPieceColor() == PieceColor.WHITE);
        assertTrue(piece.getPieceType() == PieceType.KING);

    }

    /*
     * Just checks that the black and white army is where it should and
     * that the middle ground is empty
     */
    public void test_ensureFilledBoard() throws Exception {
        // White pieces start area
        for (int rank = 1; rank <= 2; rank++) {
            for (String file : FILES) {
                BoardPosition position = new BoardPosition(file + rank);
                assertFalse(board.isEmpty(position));

                IChessPiece piece = board.getChessPiece(position);
                assertFalse(piece == null);
                assertTrue(piece.getPieceColor() == PieceColor.WHITE);
            }
        }

        // Empty start area
        for (int rank = 3; rank <= 6; rank++) {
            for (String file : FILES) {
                BoardPosition position = new BoardPosition(file + rank);
                assertTrue(board.isEmpty(position));

                IChessPiece piece = board.getChessPiece(position);
                assertTrue(piece == null);
            }
        }

        // Black pieces start area
        for (int rank = 7; rank <= 8; rank++) {
            for (String file : FILES) {
                BoardPosition position = new BoardPosition(file + rank);
                assertFalse(board.isEmpty(position));

                IChessPiece piece = board.getChessPiece(position);
                assertFalse(piece == null);
                assertTrue(piece.getPieceColor() == PieceColor.BLACK);
            }
        }

    }

    public void test_countPiecesIsCorrectAtStart() throws Exception {
        countPieces(PieceColor.WHITE, PieceType.PAWN, 8);
        countPieces(PieceColor.BLACK, PieceType.PAWN, 8);

        countPieces(PieceColor.WHITE, PieceType.BISHOP, 2);
        countPieces(PieceColor.BLACK, PieceType.BISHOP, 2);

        countPieces(PieceColor.WHITE, PieceType.ROOK, 2);
        countPieces(PieceColor.BLACK, PieceType.ROOK, 2);

        countPieces(PieceColor.WHITE, PieceType.KNIGHT, 2);
        countPieces(PieceColor.BLACK, PieceType.KNIGHT, 2);

        countPieces(PieceColor.WHITE, PieceType.QUEEN, 1);
        countPieces(PieceColor.BLACK, PieceType.QUEEN, 1);

        countPieces(PieceColor.WHITE, PieceType.KING, 1);
        countPieces(PieceColor.BLACK, PieceType.KING, 1);
    }

    public void test_getChessPieceGivesSame() throws Exception {
        for(int rank = 0; rank < Constants.BOARD_MAX_POSITION; rank++){
            for(int file = 0; file < Constants.BOARD_MAX_POSITION; file++){
                IChessPiece p1 = board.getChessPiece(file, rank);
                IChessPiece p2 = board.getChessPiece(new BoardPosition(FILES[file] + (rank+1)));

                if(p1 == null){
                    assertTrue(p2 == null);
                } else {
                    assertTrue(p1.getPieceColor() == p2.getPieceColor());
                    assertTrue(p1.getPieceType() == p2.getPieceType());
                }
            }
        }
    }

    public void test_countPieces16Each() throws Exception {
        countColor(PieceColor.WHITE, 16);
        countColor(PieceColor.BLACK, 16);
    }

    /*
     * Helper method.
     *
     * Counts the number of pieces for a given color and compares it to the
     * expected argument.
     */
    private void countColor(final PieceColor COLOR, final int EXPECTED){
        int count = 0;
        for(int rank = 1; rank <= 8; rank++){
            for (String file: FILES){
                IChessPiece piece = board.getChessPiece(new BoardPosition(file + rank));
                if(piece != null && piece.getPieceColor() == COLOR){
                    count++;
                }
            }
        }

        assertEquals(EXPECTED, count);
    }

    /*
     * Helper method.
     *
     * Counts the number of pieces for the specified color and type and compares to the
     * expected amount.
     */
    private void countPieces(final PieceColor COLOR, final PieceType TYPE, final int EXPECTED){
        int count = 0;
        for(int rank = 1; rank <= 8; rank++){
            for (String file: FILES){
                IChessPiece piece = board.getChessPiece(new BoardPosition(file + rank));
                if(piece != null
                        && piece.getPieceColor() == COLOR
                        && piece.getPieceType() == TYPE){
                    count++;
                }
            }
        }

        assertEquals(EXPECTED, count);
    }
}
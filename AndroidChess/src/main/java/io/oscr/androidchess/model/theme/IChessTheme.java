package io.oscr.androidchess.model.theme;

import android.graphics.drawable.ColorDrawable;

import io.oscr.androidchess.model.pieces.IChessPiece;

/**
 * Specifies the functionality of color themes.
 *
 * These are a type of configuration objects that keep the colors for the chess board, color for
 * the selected board position and name of image resources for chess pieces. Thus allowing the
 * ChessModel to change color theme in a simple way.
 */
public interface IChessTheme {

    /**
     * Gives the color for the currently selected square.
     *
     * @return ColorDrawable for the selected BoardPosition.
     */
    public ColorDrawable getSelectedSquare();

    /**
     * Gives the color for odd squares. Provides one of the two chessboard colors.
     *
     * @return ColorDrawable for odd BoardPositions.
     */
    public ColorDrawable getOddSquare();

    /**
     * Gives the color for even squares. Provides one of the two chessboard colors.
     *
     * @return ColorDrawable for even BoardPositions.
     */
    public ColorDrawable getEvenSquare();

    /**
     * Gives the string name of the chess piece image resource that is associated with this
     * type of piece.
     *
     * @param chessPiece to obtain image resource name for.
     * @return the resource name for given argument.
     */
    public String getPieceString(IChessPiece chessPiece);
}
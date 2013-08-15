package io.oscr.androidchess.model;

import android.graphics.drawable.ColorDrawable;

import io.oscr.androidchess.model.pieces.PieceColor;
import io.oscr.androidchess.model.pieces.PieceType;

import java.beans.PropertyChangeListener;

/**
 * Specifies the behavior that the model in the MVC pattern will expose to the controller.
 */
public interface IChessModel extends IObservable {

    /**
     * If no position is selected prior this will set the selected position to the parameter argument.
     * If a BoardPosition is selected prior then the following will happen: if it's the same BoardPosition
     * then the selection will be removed. If the selected BoardPosition and current are different then
     * an attempt to move will be performed.
     *
     * @see
     * @param position to select.
     */
	public abstract void selectPosition(BoardPosition position);

    /**
     * Returns the background Color for a specified square. If the BoardPosition is selected the color
     * will be that of the specified selected color. Otherwise the normal square color for that position.
     *
     * @see io.oscr.androidchess.model.theme.IChessTheme
     * @param boardPosition to get background color for.
     * @return a ColorDrawable with correct background color.
     */
	public abstract ColorDrawable getBoardColor(BoardPosition boardPosition);

    /**
     * Returns the image resource name for a the ChessPiece on the specified BoardPosition. If no
     * piece is placed on that position it will return null.
     *
     * @see io.oscr.androidchess.model.theme.IChessTheme#getPieceString(io.oscr.androidchess.model.pieces.IChessPiece)
     * @param boardPosition
     * @return string representing the image resource name if non empty BoardPosition, otherwise null.
     */
	public abstract String getPieceString(BoardPosition boardPosition);

    /**
     * Starts a new Game. They player will have the specified color given.
     *
     * @param color  that the player will play as.
     */
	public abstract void newGame(PieceColor color);

    /**
     * Returns status information for the progressing game. Allows the user to be informed about
     * game status events such as check, checkmate etc.
     *
     * @return String containing game status information.
     */
	public abstract String getDisplayInformation();

    /**
     * Proof of concept method shown that colors are easy to change in the game.
     *
     * Will just switch between the two defined color themes.
     */
	public abstract void changeColorTheme();

    /*
     * Handles the promotion of a pawn to another piece. Invoked by the controller after
     * the user has selected what piece the pawn should be promoted to.
     */
	public abstract void setPromotion(PieceType type, BoardPosition from, BoardPosition to);
}
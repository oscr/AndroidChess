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
     *
     * @param boardPosition
     * @return
     */
	public abstract ColorDrawable getBoardColor(BoardPosition boardPosition);

    /**
     *
     * @param boardPosition
     * @return
     */
	public abstract String getPieceString(BoardPosition boardPosition);

    /**
     *
     * @param white
     */
	public abstract void newGame(PieceColor white);

    /**
     *
     * @return
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
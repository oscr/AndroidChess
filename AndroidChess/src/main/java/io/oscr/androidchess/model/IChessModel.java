package io.oscr.androidchess.model;

import android.graphics.drawable.ColorDrawable;

import io.oscr.androidchess.model.pieces.PieceColor;
import io.oscr.androidchess.model.pieces.PieceType;

import java.beans.PropertyChangeListener;

/**
 *
 */
public interface IChessModel extends IObservable {
	public abstract void selectPosition(BoardPosition position);
	
	public abstract ColorDrawable getBoardColor(BoardPosition boardPosition);

	public abstract String getPieceString(BoardPosition boardPosition);

	public abstract void newGame(PieceColor white);

	public abstract String getDisplayInformation();

	public abstract void changeColorTheme();

	public abstract void setPromotion(PieceType type, BoardPosition from, BoardPosition to);
}
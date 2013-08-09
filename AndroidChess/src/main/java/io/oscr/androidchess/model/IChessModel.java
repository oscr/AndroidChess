package io.oscr.androidchess.model;

import io.oscr.androidchess.model.pieces.PieceColor;
import io.oscr.androidchess.model.pieces.PieceType;

import java.beans.PropertyChangeListener;

public interface IChessModel {

	public abstract void selectPosition(BoardPosition position);
	
	public abstract void addObserver(PropertyChangeListener observer);

	public abstract void removeObserver(PropertyChangeListener observer);

	public abstract int getBoardColor(BoardPosition boardPosition);

	public abstract String getPieceString(BoardPosition boardPosition);

	public abstract void newGame(PieceColor white);

	public abstract String getDisplayInformation();

	public abstract void changeColorTheme();

	public abstract void setPromotion(PieceType type, BoardPosition from, BoardPosition to);

}
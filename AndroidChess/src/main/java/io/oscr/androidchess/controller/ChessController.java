package io.oscr.androidchess.controller;

import android.graphics.drawable.ColorDrawable;

import io.oscr.androidchess.model.BoardPosition;
import io.oscr.androidchess.model.IChessModel;
import io.oscr.androidchess.model.event.PromotionEvent;
import io.oscr.androidchess.model.pieces.PieceColor;
import io.oscr.androidchess.model.pieces.PieceType;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Immutable in the sense that the model reference never changes. Marked as final.
 *
 * Represents the Controller in the MVC model. Simply delegates invocations from the view to
 * the model. Contains no other state than reference to model.
 */
public final class ChessController {
	private final IChessModel model;

    /**
     * Creates an Controller object.
     *
     * Checks argument and will throw NullPointerException if argument should be null. No
     * other sanity checks are performed.
     *
     * @throws NullPointerException if model argument is null.
     * @param model to delegate invocations to.
     */
	public ChessController(final IChessModel model){
        checkNotNull(model, "Argument model was null. Expected non null");
		this.model = model;
	}

    /**
     * Used for determining the background color of a chess board square. Given the zero indexed
     * file and rank will return a ColorDrawable for that position of the chessboard.
     *
     * @see IChessModel#getBoardColor(io.oscr.androidchess.model.BoardPosition)
     * @throws IllegalArgumentException if file or rank outside board limits.
     * @param file zero indexed file.
     * @param rank zero indexed rank.
     * @return a ColorDrawable for the specified position.
     */
	public ColorDrawable getBoardColor(final int file, final int rank) {
		return model.getBoardColor(new BoardPosition(file, rank));
	}

    /**
     * Gets the resource name for a chess piece image.
     *
     * @see IChessModel#getPieceString(io.oscr.androidchess.model.BoardPosition)
     * @param file where the piece is positioned.
     * @param rank where the piece is positioned.
     * @return resource image name or null if no piece at specified position.
     */
	public String getPieceString(final int file, final int rank) {
		return model.getPieceString(new BoardPosition(file, rank));
	}

    /**
     * Selects a position to either move from or to.
     *
     * @see IChessModel#selectPosition(io.oscr.androidchess.model.BoardPosition)
     * @param position selected.
     */
    public void selectPosition(final BoardPosition position) {
        model.selectPosition(position);
    }

    /**
     * Starts a new game.
     *
     * @see IChessModel#newGame(io.oscr.androidchess.model.pieces.PieceColor)
     * @param color to play as.
     */
	public void newGame(final PieceColor color) {
		model.newGame(color);
	}

    /**
     * @see IChessModel#getDisplayInformation()
     * @return String containing display info.
     */
	public String getDisplayInformation() {
		return model.getDisplayInformation();
	}

    /**
     * @see io.oscr.androidchess.model.IChessModel#changeColorTheme()
     */
	public void changeColorTheme() {
		model.changeColorTheme();
	}

    /**
     * Callback after PromotionEvent has been received by View.
     *
     * @see IChessModel#setPromotion(io.oscr.androidchess.model.pieces.PieceType, io.oscr.androidchess.model.BoardPosition, io.oscr.androidchess.model.BoardPosition)
     * @param type PieceType to promote to.
     * @param promotion PromotionEvent that was received by View.
     */
	public void setPromotion(final PieceType type, final PromotionEvent promotion) {
		model.setPromotion(type, promotion.from, promotion.to);
	}
}
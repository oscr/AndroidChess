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
 * Controller class in the MVC pattern. Simply delegates invocations from the view to the model.
 * Contains no other state than reference to model.
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
	public ChessController(IChessModel model){
        checkNotNull(model, "Argument model was null. Expected non null");
		this.model = model;
	}

    /**
     * Used for determining the background color of a chess board square. Given the zero indexed
     * file and rank will return a ColorDrawable for that position of the chessboard.
     *
     * @param file zero indexed
     * @param rank zero indexed
     * @return a ColorDrawable for the specified
     */
	public ColorDrawable getBoardColor(int file, int rank) {
		return model.getBoardColor(new BoardPosition(file, rank));
	}


    /**
     *
     * @param file
     * @param rank
     * @return
     */
	public String getPieceString(int file, int rank) {
		return model.getPieceString(new BoardPosition(file, rank));
	}

	public void selectPosition(int file, int rank) {
		model.selectPosition(new BoardPosition(file, rank));
	}

    public void selectPosition(BoardPosition position) {
        model.selectPosition(position);
    }

	public void newGame(PieceColor white) {
		model.newGame(white);
	}

	public String getDisplayInformation() {
		return model.getDisplayInformation();
	}

	public void changeColorTheme() {
		model.changeColorTheme();
	}

	public void setPromotion(PieceType type, PromotionEvent promotion) {
		model.setPromotion(type, promotion.from, promotion.to);
	}
}
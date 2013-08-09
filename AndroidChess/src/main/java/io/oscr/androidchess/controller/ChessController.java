package io.oscr.androidchess.controller;

import android.graphics.drawable.ColorDrawable;

import io.oscr.androidchess.model.BoardPosition;
import io.oscr.androidchess.model.IChessModel;
import io.oscr.androidchess.model.event.PromotionEvent;
import io.oscr.androidchess.model.pieces.PieceColor;
import io.oscr.androidchess.model.pieces.PieceType;

public class ChessController {
	private IChessModel model;
	
	public ChessController(IChessModel model){
		this.model = model;
	}
	
	public ColorDrawable getBoardColor(int file, int rank) {
		return model.getBoardColor(new BoardPosition(file, rank));
	}

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
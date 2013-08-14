package io.oscr.androidchess.model.theme;

import io.oscr.androidchess.model.pieces.IChessPiece;
import io.oscr.androidchess.model.pieces.PieceColor;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

/**
 * A more funny color theme to demonstrate how easy it is to change them.
 */
public class FunnyTheme implements IChessTheme {
    /**
     * Color for selected BoardPosition
     */
    private final ColorDrawable selectedColor = new ColorDrawable(Color.YELLOW);

    /**
     * Color for odd squares.
     */
    private final ColorDrawable oddColor = new ColorDrawable(Color.RED);

    /**
     * Color for even squares.
     */
    private final ColorDrawable evenColor = new ColorDrawable(Color.GREEN);

    /**
     * {@inheritDoc}
     */
    @Override
    public ColorDrawable getSelectedSquare() {
        return selectedColor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ColorDrawable getOddSquare() {
        return oddColor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ColorDrawable getEvenSquare() {
        return evenColor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
	public String getPieceString(IChessPiece chessPiece) {
		if(chessPiece == null){
			return null;
		}
		String colorPrefix = chessPiece.getPieceColor() == PieceColor.WHITE ? "w" : "b";
		switch (chessPiece.getPieceType()) {
		case PAWN:
            return colorPrefix + "p";
		case ROOK:
            return colorPrefix + "r";
		case KNIGHT:
            return colorPrefix + "n";
		case BISHOP:
            return colorPrefix +  "b";
		case QUEEN:
            return colorPrefix + "q";
		case KING:
            return colorPrefix + "k";
		default:
            throw new IllegalStateException("Unknown piece state" + chessPiece.getPieceType().toString());
		}
	}
}
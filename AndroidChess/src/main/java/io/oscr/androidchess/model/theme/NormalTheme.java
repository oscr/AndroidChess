package io.oscr.androidchess.model.theme;

import io.oscr.androidchess.model.pieces.IChessPiece;
import io.oscr.androidchess.model.pieces.PieceColor;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

/**
 * A classic chess color theme.
 */
public class NormalTheme implements IChessTheme {
    /**
     * Color for selected BoardPosition
     */
    private final ColorDrawable selectedColor = new ColorDrawable(Color.YELLOW);

    /**
     * Color for odd squares.
     */
	private final ColorDrawable oddColor = new ColorDrawable(Color.WHITE);

    /**
     * Color for even squares.
     */
	private final ColorDrawable evenColor = new ColorDrawable(Color.GRAY);

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
        String pieceSuffix = null;

        switch (chessPiece.getPieceType()) {
            case PAWN:
                pieceSuffix = "p";
                break;
            case ROOK:
                pieceSuffix = "r";
                break;
            case KNIGHT:
                pieceSuffix = "n";
                break;
            case BISHOP:
                pieceSuffix = "b";
                break;
            case QUEEN:
                pieceSuffix = "q";
                break;
            case KING:
                pieceSuffix = "k";
                break;
            default:
                throw new IllegalStateException("Unknown piece state" + chessPiece.getPieceType().toString());
        }
        return colorPrefix + pieceSuffix;
    }
}
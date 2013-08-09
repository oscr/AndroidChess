package io.oscr.androidchess.model.theme;

import io.oscr.androidchess.model.pieces.IChessPiece;
import io.oscr.androidchess.model.pieces.PieceColor;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

public class NormalTheme implements IChessTheme {
	private final ColorDrawable selectedColor = new ColorDrawable(Color.YELLOW);
	private final ColorDrawable oddColor = new ColorDrawable(Color.WHITE);
	private final ColorDrawable evenColor = new ColorDrawable(Color.GRAY);

	@Override
	public ColorDrawable getSelectedSquare() {
		return selectedColor;
	}

	@Override
	public ColorDrawable getOddSquare() {
		return oddColor;
	}

	@Override
	public ColorDrawable getEvenSquare() {
		return evenColor;
	}

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
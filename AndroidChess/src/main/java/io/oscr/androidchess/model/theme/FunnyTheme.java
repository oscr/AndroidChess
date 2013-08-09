package io.oscr.androidchess.model.theme;

import io.oscr.androidchess.model.pieces.IChessPiece;
import io.oscr.androidchess.model.pieces.PieceColor;

import android.graphics.Color;

public class FunnyTheme implements IChessTheme {
    private final int selectedColor = Color.CYAN;
    private final int oddColor = Color.RED;
    private final int evenColor = Color.GREEN;

    @Override
    public int getSelectedSquare() {
        return selectedColor;
    }

    @Override
    public int getOddSquare() {
        return oddColor;
    }

    @Override
    public int getEvenSquare() {
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
package io.oscr.androidchess.model.theme;

import android.graphics.drawable.ColorDrawable;

import io.oscr.androidchess.model.pieces.IChessPiece;

/**
 * TODO
 */
public interface IChessTheme {
        public ColorDrawable getSelectedSquare();
        public ColorDrawable getOddSquare();
        public ColorDrawable getEvenSquare();
		public String getPieceString(IChessPiece chessPiece);
}

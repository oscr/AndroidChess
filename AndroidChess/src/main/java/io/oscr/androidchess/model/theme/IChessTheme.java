package io.oscr.androidchess.model.theme;

import io.oscr.androidchess.model.pieces.IChessPiece;

// TODO Android doesn't support Color reference. Create Color wrapper class to improve readability.
public interface IChessTheme {
        public int getSelectedSquare();
        public int getOddSquare();
        public int getEvenSquare();
		public String getPieceString(IChessPiece chessPiece);
}

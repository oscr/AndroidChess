package io.oscr.androidchess.model.pieces;

public final class ChessPiece implements IChessPiece {
	private final PieceColor color;
	private final PieceType type;
	
	public ChessPiece(PieceColor color, PieceType piece){
		this.color = color;
		this.type = piece;
	}

	@Override
	public PieceType getPieceType() {
		return type;
	}

	@Override
	public PieceColor getPieceColor() {
		return color;
	}
		
}
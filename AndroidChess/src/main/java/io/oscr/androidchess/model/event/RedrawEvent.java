package io.oscr.androidchess.model.event;

import io.oscr.androidchess.model.BoardPosition;

public class RedrawEvent implements ChessEvent{
    public final BoardPosition[] positions;

    public RedrawEvent(BoardPosition[] positions){
        this.positions = positions;
    }
}

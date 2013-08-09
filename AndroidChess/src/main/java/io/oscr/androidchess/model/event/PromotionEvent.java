package io.oscr.androidchess.model.event;

import io.oscr.androidchess.model.BoardPosition;

public final class PromotionEvent {
    public final BoardPosition from;
    public final BoardPosition to;

    public PromotionEvent(BoardPosition from, BoardPosition to) {
        this.from = from;
        this.to = to;
    }
}
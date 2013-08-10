package io.oscr.androidchess.model.event;

import io.oscr.androidchess.model.BoardPosition;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Immutable. Marked as final.
 *
 * Used when a pawn reaches the last row and needs to be replaced by another piece type.
 */
public final class PromotionEvent implements ChessEvent {
    public final BoardPosition from;
    public final BoardPosition to;

    /**
     * Creates a immutable PromotionEvent.
     *
     * The arguments are checked for null and will throw NullPointerException if
     * any argument should be null. Does not check if the BoardPositions are valid.
     * This should already be performed by BoardPosition.
     *
     * @throws NullPointerException if from or to is null
     * @param from where the piece moved from.
     * @param to where the piece moved to.
     */
    public PromotionEvent(BoardPosition from, BoardPosition to) {
        checkNotNull(from, "Argument from was null. Expected non null");
        checkNotNull(from, "Argument from was null. Expected non null");

        this.from = from;
        this.to = to;
    }
}
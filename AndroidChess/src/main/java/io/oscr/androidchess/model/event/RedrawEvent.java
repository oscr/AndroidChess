package io.oscr.androidchess.model.event;

import io.oscr.androidchess.model.BoardPosition;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Immutable if BoardPosition objects also are immutable. Marked as final.
 *
 * Used to identify when a multiset of BoardPositions need to be redrawn by the view. Instead of forcing
 * a redraw of all the squares as with RedrawAllEvent, this allows specific BoardPositions to be redrawn
 * and therefore being more efficient.
 */
public final class RedrawEvent implements ChessEvent{
    public final BoardPosition[] positions;

    /**
     * Creates an immutable RedrawEvent.
     *
     * The constructor will throw NullPointerException if the argument given is null OR if any
     * reference within the argument array is null.
     *
     * Note that while the BoardPositions in the argument array should be unique, no check is done to
     * guarantee that they in fact are unique. It's up to the caller to ensure that this condition
     * holds.
     *
     * @throws NullPointerException if position argument is null or if it contains null.
     * @param positions the BoardPositions that should be redrawn.
     */
    public RedrawEvent(BoardPosition[] positions){
        checkNotNull(positions, "Argument from was null. Expected non null");

        // Check that the array doesn't contain null
        for(BoardPosition bp : positions){
            if(bp == null){
                throw new NullPointerException("Argument in positions was null. Expected non null");
            }
        }
        this.positions = positions;
    }
}
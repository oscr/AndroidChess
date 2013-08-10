package io.oscr.androidchess.model.event;

/**
 * Stateless and hence immutable. Marked as final.
 *
 * Used to identify when the whole board needs to be redrawn. For example
 * when changing the color theme.
 */
public final class RedrawAllEvent implements ChessEvent {}

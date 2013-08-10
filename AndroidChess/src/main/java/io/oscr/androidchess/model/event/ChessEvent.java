package io.oscr.androidchess.model.event;

/**
 * A marker interface used by events classes in the application.
 *
 * When events are passed in via PropertyChangedEvents this interface allows easy
 * identifying of valid events by using instanceof.
 */
public interface ChessEvent {}

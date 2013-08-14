package io.oscr.androidchess.model;

import java.beans.PropertyChangeListener;

/**
 * Instead of extending the Observable class and using implementation inheritance the IObservable
 * interface provides a "cleaner" way to specify that a class is observable. By using this interface
 * we can avoid unnecessary implementation inheritance and instead use specification inheritance.
 *
 * @see ChessModel for more concrete example.
 */
public interface IObservable {
    /**
     * Allows a class implementing PropertyChangeListener to register itself as a listeners to
     * the class.
     *
     * @param observer adds observer to receive PropertyChangeEvents.
     */
    void addObserver(PropertyChangeListener observer);

    /**
     * Allows a class implementing PropertyChangeListener to unregister itself as a listener to
     * the implementing class.
     *
     * @param observer removes observer from receiving PropertyChangeEvents.
     */
    void removeObserver(PropertyChangeListener observer);
}
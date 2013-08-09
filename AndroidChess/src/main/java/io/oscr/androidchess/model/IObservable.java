package io.oscr.androidchess.model;

import java.beans.PropertyChangeListener;

public interface IObservable {
        void addObserver(PropertyChangeListener observer);
        void removeObserver(PropertyChangeListener observer);
}
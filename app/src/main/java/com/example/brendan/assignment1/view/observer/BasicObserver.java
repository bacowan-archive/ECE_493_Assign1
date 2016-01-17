package com.example.brendan.assignment1.view.observer;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Brendan on 1/13/2016.
 */
public class BasicObserver implements Observer {

    private Collection<Observable> observables = new ArrayList<>();

    public void addObservable(Observable observable) {
        observables.add(observable);
    }

    @Override
    public void notifyObservers(MessageType type, Object arg) {
        for (Observable observable : observables)
            observable.notifyObservable(type, arg);
    }
}

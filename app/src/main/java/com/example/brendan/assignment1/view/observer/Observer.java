package com.example.brendan.assignment1.view.observer;

/**
 * Created by Brendan on 1/13/2016.
 */
public interface Observer {
    void notifyObservers(MessageType type, Object arg);
}

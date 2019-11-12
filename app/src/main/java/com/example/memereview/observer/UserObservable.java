package com.example.memereview.observer;

public interface UserObservable {
    public void register(UserObserver observer);
    public void notifyAllObservers();
}

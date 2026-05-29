package com.sda.resumeanalyzer.observer;

import java.util.ArrayList;
import java.util.List;

import com.sda.resumeanalyzer.model.MatchResult;

public class JobAlertSubject {

    private final List<JobObserver> observers = new ArrayList<>();

    public void addObserver(JobObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(JobObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(MatchResult result) {
        for (JobObserver observer : observers) {
            observer.update(result);
        }
    }
}
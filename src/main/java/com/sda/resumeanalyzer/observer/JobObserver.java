package com.sda.resumeanalyzer.observer;

import com.sda.resumeanalyzer.model.MatchResult;

public interface JobObserver {
    void update(MatchResult result);
}
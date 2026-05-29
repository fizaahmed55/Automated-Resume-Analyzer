package com.sda.resumeanalyzer.strategy;

import com.sda.resumeanalyzer.model.Job;
import com.sda.resumeanalyzer.model.MatchResult;
import com.sda.resumeanalyzer.model.Resume;

public interface MatchingStrategy {
    MatchResult match(Resume resume, Job job);
}
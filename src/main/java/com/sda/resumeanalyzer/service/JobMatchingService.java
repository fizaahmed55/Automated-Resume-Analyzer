package com.sda.resumeanalyzer.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.sda.resumeanalyzer.model.Job;
import com.sda.resumeanalyzer.model.MatchResult;
import com.sda.resumeanalyzer.model.Resume;

public class JobMatchingService {

    private final JobSearchService jobSearchService;
    private final JobSpecificAtsScoringService jobSpecificAtsScoringService;

    public JobMatchingService() {
        this.jobSearchService = new JobSearchService();
        this.jobSpecificAtsScoringService = new JobSpecificAtsScoringService();
    }

    public List<MatchResult> matchJobs(Resume resume) {
        List<Job> jobs = jobSearchService.findJobsForResume(resume);
        List<MatchResult> results = new ArrayList<>();

        for (Job job : jobs) {
            MatchResult result = jobSpecificAtsScoringService.calculateJobSpecificAts(resume, job);
            results.add(result);
        }

        results.sort(Comparator.comparingDouble(MatchResult::getMatchPercentage).reversed());

        if (results.size() > 20) {
            return results.subList(0, 20);
        }

        return results;
    }
}
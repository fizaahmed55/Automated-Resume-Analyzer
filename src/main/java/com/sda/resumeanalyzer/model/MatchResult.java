package com.sda.resumeanalyzer.model;

import java.util.List;

public class MatchResult {

    private Job job;
    private double matchPercentage;
    private List<String> matchedSkills;
    private List<String> missingSkills;
    private List<String> matchedKeywords;
    private List<String> missingKeywords;
    private String verdict;
    private String reason;
    private List<String> tailoredSuggestions;

    public MatchResult(Job job,
                       double matchPercentage,
                       List<String> matchedSkills,
                       List<String> missingSkills,
                       List<String> matchedKeywords,
                       List<String> missingKeywords,
                       String verdict,
                       String reason,
                       List<String> tailoredSuggestions) {
        this.job = job;
        this.matchPercentage = matchPercentage;
        this.matchedSkills = matchedSkills;
        this.missingSkills = missingSkills;
        this.matchedKeywords = matchedKeywords;
        this.missingKeywords = missingKeywords;
        this.verdict = verdict;
        this.reason = reason;
        this.tailoredSuggestions = tailoredSuggestions;
    }

    public Job getJob() {
        return job;
    }

    public double getMatchPercentage() {
        return matchPercentage;
    }

    public List<String> getMatchedSkills() {
        return matchedSkills;
    }

    public List<String> getMissingSkills() {
        return missingSkills;
    }

    public List<String> getMatchedKeywords() {
        return matchedKeywords;
    }

    public List<String> getMissingKeywords() {
        return missingKeywords;
    }

    public String getVerdict() {
        return verdict;
    }

    public String getReason() {
        return reason;
    }

    public List<String> getTailoredSuggestions() {
        return tailoredSuggestions;
    }
}
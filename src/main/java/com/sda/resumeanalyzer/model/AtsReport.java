package com.sda.resumeanalyzer.model;

import java.util.List;

public class AtsReport {

    private Resume resume;
    private double atsScore;
    private double skillsScore;
    private double contactScore;
    private double educationScore;
    private double projectScore;
    private double keywordScore;
    private double formattingScore;
    private List<String> suggestions;

    public AtsReport(Resume resume, double atsScore, double skillsScore, double contactScore,
                     double educationScore, double projectScore, double keywordScore,
                     double formattingScore, List<String> suggestions) {
        this.resume = resume;
        this.atsScore = atsScore;
        this.skillsScore = skillsScore;
        this.contactScore = contactScore;
        this.educationScore = educationScore;
        this.projectScore = projectScore;
        this.keywordScore = keywordScore;
        this.formattingScore = formattingScore;
        this.suggestions = suggestions;
    }

    public Resume getResume() {
        return resume;
    }

    public double getAtsScore() {
        return atsScore;
    }

    public double getSkillsScore() {
        return skillsScore;
    }

    public double getContactScore() {
        return contactScore;
    }

    public double getEducationScore() {
        return educationScore;
    }

    public double getProjectScore() {
        return projectScore;
    }

    public double getKeywordScore() {
        return keywordScore;
    }

    public double getFormattingScore() {
        return formattingScore;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }
}
package com.sda.resumeanalyzer.service;

import java.util.List;

import com.sda.resumeanalyzer.model.AtsReport;
import com.sda.resumeanalyzer.model.Resume;

public class AtsScoringService {

    private final SuggestionService suggestionService = new SuggestionService();

    public AtsReport generateReport(Resume resume) {
        double skillsScore = calculateSkillsScore(resume);
        double contactScore = calculateContactScore(resume);
        double educationScore = calculateEducationScore(resume);
        double projectScore = calculateProjectScore(resume);
        double keywordScore = calculateKeywordScore(resume);
        double formattingScore = calculateFormattingScore(resume);

        double finalScore =
                skillsScore * 0.30 +
                contactScore * 0.15 +
                educationScore * 0.15 +
                projectScore * 0.15 +
                keywordScore * 0.15 +
                formattingScore * 0.10;

        List<String> suggestions = suggestionService.generateSuggestions(resume);

        return new AtsReport(
                resume,
                finalScore,
                skillsScore,
                contactScore,
                educationScore,
                projectScore,
                keywordScore,
                formattingScore,
                suggestions
        );
    }

    private double calculateSkillsScore(Resume resume) {
        int skillCount = resume.getSkills().size();

        if (skillCount >= 10) {
            return 100;
        }

        return Math.min(100, skillCount * 10);
    }

    private double calculateContactScore(Resume resume) {
        double score = 0;

        if (!resume.getEmail().equals("Not found")) {
            score += 50;
        }

        if (!resume.getPhone().equals("Not found")) {
            score += 50;
        }

        return score;
    }

    private double calculateEducationScore(Resume resume) {
        if (!resume.getEducation().equals("Not clearly mentioned")) {
            return 100;
        }

        return 40;
    }

    private double calculateProjectScore(Resume resume) {
        if (resume.isHasProjects()) {
            return 100;
        }

        return 30;
    }

    private double calculateKeywordScore(Resume resume) {
        String text = resume.getRawText().toLowerCase();
        double score = 0;

        if (text.contains("project")) {
            score += 20;
        }

        if (text.contains("skills")) {
            score += 20;
        }

        if (text.contains("education")) {
            score += 20;
        }

        if (text.contains("experience") || text.contains("internship")) {
            score += 20;
        }

        if (text.contains("github") || text.contains("linkedin")) {
            score += 20;
        }

        return Math.min(score, 100);
    }

    private double calculateFormattingScore(Resume resume) {
        String text = resume.getRawText();

        if (text.length() < 300) {
            return 40;
        }

        if (text.length() > 3000) {
            return 70;
        }

        return 90;
    }
}
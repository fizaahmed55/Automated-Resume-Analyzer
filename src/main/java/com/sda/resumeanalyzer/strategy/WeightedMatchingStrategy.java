package com.sda.resumeanalyzer.strategy;

import java.util.ArrayList;
import java.util.List;

import com.sda.resumeanalyzer.model.Job;
import com.sda.resumeanalyzer.model.MatchResult;
import com.sda.resumeanalyzer.model.Resume;
import com.sda.resumeanalyzer.service.TailoredResumeService;

public class WeightedMatchingStrategy implements MatchingStrategy {

    private final TailoredResumeService tailoredResumeService = new TailoredResumeService();

    @Override
    public MatchResult match(Resume resume, Job job) {
        List<String> matchedSkills = new ArrayList<>();
        List<String> missingSkills = new ArrayList<>();
        List<String> matchedKeywords = new ArrayList<>();
        List<String> missingKeywords = new ArrayList<>();

        String resumeText = resume.getRawText().toLowerCase();

        for (String requiredSkill : job.getRequiredSkills()) {
            if (containsSkill(resume, requiredSkill)) {
                matchedSkills.add(requiredSkill);
            } else {
                missingSkills.add(requiredSkill);
            }
        }

        for (String keyword : job.getKeywords()) {
            if (resumeText.contains(keyword.toLowerCase())) {
                matchedKeywords.add(keyword);
            } else {
                missingKeywords.add(keyword);
            }
        }

        double skillScore = calculatePercentage(matchedSkills.size(), job.getRequiredSkills().size());
        double keywordScore = calculatePercentage(matchedKeywords.size(), job.getKeywords().size());
        double levelScore = calculateLevelScore(resume, job);

        double finalScore =
                skillScore * 0.60 +
                keywordScore * 0.30 +
                levelScore * 0.10;

        finalScore = Math.min(100, finalScore);

        String verdict = generateVerdict(finalScore);
        String reason = generateReason(finalScore, matchedSkills, missingSkills, matchedKeywords, missingKeywords);

        List<String> tailoredSuggestions = tailoredResumeService.generateSuggestions(
                resume,
                job,
                missingSkills,
                missingKeywords
        );

        return new MatchResult(
                job,
                finalScore,
                matchedSkills,
                missingSkills,
                matchedKeywords,
                missingKeywords,
                verdict,
                reason,
                tailoredSuggestions
        );
    }

    private boolean containsSkill(Resume resume, String requiredSkill) {
        for (String skill : resume.getSkills()) {
            if (skill.equalsIgnoreCase(requiredSkill)) {
                return true;
            }
        }

        return resume.getRawText().toLowerCase().contains(requiredSkill.toLowerCase());
    }

    private double calculatePercentage(int matched, int total) {
        if (total == 0) {
            return 0;
        }

        return ((double) matched / total) * 100;
    }

    private double calculateLevelScore(Resume resume, Job job) {
        if (job.getExperienceLevel().equalsIgnoreCase("Beginner")) {
            return 100;
        }

        if (resume.getExperienceLevel().equalsIgnoreCase(job.getExperienceLevel())) {
            return 100;
        }

        return 60;
    }

    private String generateVerdict(double score) {
        if (score >= 80) {
            return "Strong Apply";
        }

        if (score >= 65) {
            return "Apply After Small Improvements";
        }

        if (score >= 50) {
            return "Improve Resume First";
        }

        return "Low Match";
    }

    private String generateReason(double score,
                                  List<String> matchedSkills,
                                  List<String> missingSkills,
                                  List<String> matchedKeywords,
                                  List<String> missingKeywords) {
        if (score >= 80) {
            return "Resume strongly matches this job based on skills and keywords.";
        }

        if (score >= 65) {
            return "Resume matches many requirements, but some important skills or keywords are missing.";
        }

        if (score >= 50) {
            return "Resume has partial relevance, but needs stronger tailoring for this job.";
        }

        return "Resume has low alignment with this job.";
    }
}
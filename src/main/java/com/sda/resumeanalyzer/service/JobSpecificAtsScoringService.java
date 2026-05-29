package com.sda.resumeanalyzer.service;

import java.util.ArrayList;
import java.util.List;

import com.sda.resumeanalyzer.model.AtsReport;
import com.sda.resumeanalyzer.model.Job;
import com.sda.resumeanalyzer.model.MatchResult;
import com.sda.resumeanalyzer.model.Resume;

public class JobSpecificAtsScoringService {

    private final AtsScoringService atsScoringService = new AtsScoringService();
    private final TailoredResumeService tailoredResumeService = new TailoredResumeService();

    public MatchResult calculateJobSpecificAts(Resume resume, Job job) {
        AtsReport generalReport = atsScoringService.generateReport(resume);

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

        double requiredSkillsScore = percentage(matchedSkills.size(), job.getRequiredSkills().size());
        double keywordScore = percentage(matchedKeywords.size(), job.getKeywords().size());
        double structureScore = generalReport.getFormattingScore();
        double contactScore = generalReport.getContactScore();
        double projectEducationScore = (generalReport.getProjectScore() + generalReport.getEducationScore()) / 2.0;

        double finalAtsScore =
                requiredSkillsScore * 0.40 +
                keywordScore * 0.25 +
                structureScore * 0.15 +
                contactScore * 0.10 +
                projectEducationScore * 0.10;

        String verdict = generateVerdict(finalAtsScore);
        String reason = generateReason(finalAtsScore, matchedSkills, missingSkills, matchedKeywords, missingKeywords);

        List<String> tailoredSuggestions = tailoredResumeService.generateSuggestions(
                resume,
                job,
                missingSkills,
                missingKeywords
        );

        return new MatchResult(
                job,
                finalAtsScore,
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

    private double percentage(int matched, int total) {
        if (total == 0) {
            return 0;
        }

        return ((double) matched / total) * 100;
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
            return "Resume strongly matches this job based on required skills and job keywords.";
        }

        if (score >= 65) {
            return "Resume matches several requirements, but some important skills or keywords are missing.";
        }

        if (score >= 50) {
            return "Resume has partial relevance, but needs stronger tailoring for this job.";
        }

        return "Resume has low alignment with this job description.";
    }
}
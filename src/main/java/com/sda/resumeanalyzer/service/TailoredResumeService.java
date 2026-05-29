package com.sda.resumeanalyzer.service;

import java.util.ArrayList;
import java.util.List;

import com.sda.resumeanalyzer.model.Job;
import com.sda.resumeanalyzer.model.Resume;

public class TailoredResumeService {

    public List<String> generateSuggestions(Resume resume, Job job,
                                            List<String> missingSkills,
                                            List<String> missingKeywords) {
        List<String> suggestions = new ArrayList<>();

        suggestions.add("Tailor your resume headline or summary toward: " + job.getTitle() + ".");

        if (!missingSkills.isEmpty()) {
            suggestions.add("Missing skills for this job: " + missingSkills + ". Add them only if you genuinely know them.");
        }

        if (!missingKeywords.isEmpty()) {
            suggestions.add("Missing job keywords: " + missingKeywords + ". Use relevant keywords naturally in your projects or skills section.");
        }

        if (!resume.isHasProjects()) {
            suggestions.add("Add 1–2 academic or personal projects related to " + job.getTitle() + ".");
        }

        if (!resume.getRawText().toLowerCase().contains("github")) {
            suggestions.add("Add your GitHub profile or project repository link if available.");
        }

        if (!resume.getRawText().toLowerCase().contains("linkedin")) {
            suggestions.add("Add your LinkedIn profile link.");
        }

        if (job.getTitle().toLowerCase().contains("java")) {
            suggestions.add("For Java roles, mention OOP concepts, Java projects, SQL/database work, and Git usage.");
        }

        if (job.getTitle().toLowerCase().contains("frontend")) {
            suggestions.add("For frontend roles, mention HTML, CSS, JavaScript, responsive design, and UI projects.");
        }

        if (job.getTitle().toLowerCase().contains("qa") || job.getTitle().toLowerCase().contains("tester")) {
            suggestions.add("For QA roles, mention test cases, bug reports, manual testing, and quality assurance concepts.");
        }

        if (job.getTitle().toLowerCase().contains("database")) {
            suggestions.add("For database roles, mention SQL queries, MySQL, DBMS concepts, and database projects.");
        }

        if (job.getTitle().toLowerCase().contains("ai") || job.getTitle().toLowerCase().contains("machine learning")) {
            suggestions.add("For AI roles, mention Python, ML basics, datasets, model training, or NLP projects if you have done them.");
        }

        return suggestions;
    }
}

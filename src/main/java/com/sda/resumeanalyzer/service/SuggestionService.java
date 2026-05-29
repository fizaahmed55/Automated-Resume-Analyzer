package com.sda.resumeanalyzer.service;

import java.util.ArrayList;
import java.util.List;

import com.sda.resumeanalyzer.model.Resume;

public class SuggestionService {

    public List<String> generateSuggestions(Resume resume) {
        List<String> suggestions = new ArrayList<>();

        if (resume.getSkills().size() < 6) {
            suggestions.add("Add more technical skills such as Java, SQL, Git, HTML, CSS, testing, or REST API.");
        }

        if (resume.getEmail().equals("Not found")) {
            suggestions.add("Add a professional email address.");
        }

        if (resume.getPhone().equals("Not found")) {
            suggestions.add("Add a contact number.");
        }

        if (resume.getEducation().equals("Not clearly mentioned")) {
            suggestions.add("Add a clear education section with university, degree, and semester.");
        }

        if (!resume.isHasProjects()) {
            suggestions.add("Add at least 2 academic or personal projects with technologies used.");
        }

        if (!resume.isHasCertifications()) {
            suggestions.add("Add certifications or online courses if available.");
        }

        if (!resume.getRawText().toLowerCase().contains("github")) {
            suggestions.add("Add your GitHub profile link to show your coding work.");
        }

        if (!resume.getRawText().toLowerCase().contains("linkedin")) {
            suggestions.add("Add your LinkedIn profile link.");
        }

        if (suggestions.isEmpty()) {
            suggestions.add("Your resume looks good. Add measurable achievements to make it stronger.");
        }

        return suggestions;
    }
}
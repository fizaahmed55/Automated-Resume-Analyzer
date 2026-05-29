package com.sda.resumeanalyzer.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sda.resumeanalyzer.model.Resume;

public class ResumeAnalysisService {

	private final List<String> knownSkills = Arrays.asList(
	        "java", "python", "c++", "c#", "html", "css", "javascript",
	        "sql", "mysql", "oop", "object oriented programming",
	        "data structures", "algorithms", "git", "github",
	        "testing", "sqa", "qa", "manual testing", "automation testing",
	        "test cases", "bug reports", "quality assurance",
	        "spring", "spring boot", "rest api", "api", "json",
	        "machine learning", "ai", "artificial intelligence", "nlp",
	        "database", "dbms", "queries",
	        "ui", "ux", "figma", "wireframe", "prototype",
	        "communication", "teamwork", "problem solving",
	        "leadership", "documentation", "technical writing",
	        "wordpress", "responsive design", "frontend", "backend"
	);

    public Resume analyzeResume(String text) {
        Resume resume = new Resume();

        if (text == null) {
            text = "";
        }

        resume.setRawText(text);
        resume.setCandidateName(extractName(text));
        resume.setEmail(extractEmail(text));
        resume.setPhone(extractPhone(text));
        resume.setSkills(extractSkills(text));
        resume.setEducation(extractEducation(text));
        resume.setExperienceLevel(detectExperienceLevel(text));
        resume.setTargetRole(detectTargetRole(text));
        resume.setHasProjects(containsAny(text, "project", "projects", "github", "portfolio"));
        resume.setHasCertifications(containsAny(text, "certificate", "certification", "certified"));

        return resume;
    }

    private String extractName(String text) {
        String[] lines = text.split("\\R");

        for (String line : lines) {
            String cleaned = line.trim();

            if (!cleaned.isEmpty()
                    && cleaned.length() <= 50
                    && !cleaned.toLowerCase().contains("resume")
                    && !cleaned.contains("@")
                    && !cleaned.matches(".*\\d.*")) {
                return cleaned;
            }
        }

        return "Candidate";
    }

    private String extractEmail(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "Not found";
        }

        java.util.regex.Pattern regex = java.util.regex.Pattern.compile(
                "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
        );

        java.util.regex.Matcher matcher = regex.matcher(text);

        if (matcher.find()) {
            return matcher.group();
        }

        return "Not found";
    }
    private String extractPhone(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "Not found";
        }

        // Common Pakistani/international phone formats:
        // 03001234567
        // 0300-1234567
        // 0300 1234567
        // +923001234567
        // +92 300 1234567
        // +92-300-1234567
        String[] patterns = {
                "(\\+92[\\s-]?[0-9]{3}[\\s-]?[0-9]{7})",
                "(03[0-9]{2}[\\s-]?[0-9]{7})",
                "(03[0-9]{2}[\\s-]?[0-9]{3}[\\s-]?[0-9]{4})",
                "(\\+?[0-9]{10,15})"
        };

        for (String pattern : patterns) {
            java.util.regex.Pattern regex = java.util.regex.Pattern.compile(pattern);
            java.util.regex.Matcher matcher = regex.matcher(text);

            if (matcher.find()) {
                return matcher.group().trim();
            }
        }

        return "Not found";
    }

    private List<String> extractSkills(String text) {
        List<String> foundSkills = new ArrayList<>();
        String lowerText = text.toLowerCase();

        for (String skill : knownSkills) {
            if (lowerText.contains(skill.toLowerCase()) && !foundSkills.contains(skill)) {
                foundSkills.add(skill);
            }
        }

        return foundSkills;
    }

    private String extractEducation(String text) {
        String lowerText = text.toLowerCase();

        if (containsAny(lowerText, "software engineering", "computer science", "information technology", "bsse", "bscs")) {
            return "Software Engineering / Computer Science related education detected";
        }

        if (containsAny(lowerText, "university", "college", "degree", "semester", "bachelor", "bs")) {
            return "Education section detected";
        }

        return "Not clearly mentioned";
    }

    private String detectExperienceLevel(String text) {
        String lowerText = text.toLowerCase();

        if (containsAny(lowerText, "senior", "5 years", "6 years", "7 years")) {
            return "Senior";
        }

        if (containsAny(lowerText, "2 years", "3 years", "intermediate")) {
            return "Intermediate";
        }

        if (containsAny(lowerText, "internship", "intern", "semester", "student", "fresh", "beginner")) {
            return "Beginner";
        }

        return "Beginner";
    }

    private String detectTargetRole(String text) {
        String lowerText = text.toLowerCase();

        if (containsAny(lowerText, "testing", "sqa", "qa", "manual testing", "test cases")) {
            return "SQA / QA Tester Intern";
        }

        if (containsAny(lowerText, "frontend", "html", "css", "javascript", "responsive")) {
            return "Frontend Developer Intern";
        }

        if (containsAny(lowerText, "java", "oop", "sql", "backend")) {
            return "Java Developer Intern";
        }

        if (containsAny(lowerText, "python", "machine learning", "ai", "nlp")) {
            return "AI / Machine Learning Intern";
        }

        if (containsAny(lowerText, "database", "sql", "mysql", "dbms")) {
            return "Database Assistant Intern";
        }

        return "Software Engineering Intern";
    }

    private boolean containsAny(String text, String... keywords) {
        String lowerText = text.toLowerCase();

        for (String keyword : keywords) {
            if (lowerText.contains(keyword.toLowerCase())) {
                return true;
            }
        }

        return false;
    }
}
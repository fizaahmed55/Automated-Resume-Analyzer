package com.sda.resumeanalyzer.service;

import com.sda.resumeanalyzer.model.Resume;

public class ResumeDomainService {

    public String detectDomain(Resume resume) {
        String text = resume.getRawText().toLowerCase();
        String skills = resume.getSkills().toString().toLowerCase();

        String combined = text + " " + skills;

        int javaScore = score(combined, "java", "oop", "object oriented", "sql", "mysql", "backend", "spring");
        int frontendScore = score(combined, "html", "css", "javascript", "frontend", "ui", "responsive", "react");
        int qaScore = score(combined, "testing", "qa", "sqa", "manual testing", "test cases", "bug", "quality assurance");
        int aiScore = score(combined, "python", "machine learning", "ai", "artificial intelligence", "nlp", "data");
        int databaseScore = score(combined, "database", "sql", "mysql", "dbms", "queries", "tables");

        int max = javaScore;
        String domain = "Software Engineering Intern";

        if (frontendScore > max) {
            max = frontendScore;
            domain = "Frontend Developer Intern";
        }

        if (qaScore > max) {
            max = qaScore;
            domain = "SQA / QA Tester Intern";
        }

        if (aiScore > max) {
            max = aiScore;
            domain = "AI / Machine Learning Intern";
        }

        if (databaseScore > max) {
            max = databaseScore;
            domain = "Database Assistant Intern";
        }

        if (javaScore >= max) {
            domain = "Java Developer Intern";
        }

        return domain;
    }

    private int score(String text, String... keywords) {
        int score = 0;

        for (String keyword : keywords) {
            if (text.contains(keyword.toLowerCase())) {
                score++;
            }
        }

        return score;
    }
}
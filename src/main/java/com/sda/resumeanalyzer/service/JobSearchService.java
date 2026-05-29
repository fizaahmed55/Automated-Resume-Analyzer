package com.sda.resumeanalyzer.service;

import java.util.ArrayList;
import java.util.List;

import com.sda.resumeanalyzer.model.Job;
import com.sda.resumeanalyzer.model.Resume;
import com.sda.resumeanalyzer.repository.JobRepository;

public class JobSearchService {

    private final JobRepository jobRepository = new JobRepository();
    private final ResumeDomainService resumeDomainService = new ResumeDomainService();

    public List<Job> findJobsForResume(Resume resume) {
        String detectedDomain = resumeDomainService.detectDomain(resume);
        List<Job> allJobs = jobRepository.getAllJobs();
        List<Job> relevantJobs = new ArrayList<>();

        String domainLower = detectedDomain.toLowerCase();
        String resumeText = resume.getRawText().toLowerCase();

        for (Job job : allJobs) {
            String jobText = (
                    job.getTitle() + " " +
                    job.getDescription() + " " +
                    job.getRequiredSkills() + " " +
                    job.getKeywords()
            ).toLowerCase();

            if (isRelevant(domainLower, resumeText, jobText)) {
                relevantJobs.add(job);
            }
        }

        if (relevantJobs.size() < 10) {
            for (Job job : allJobs) {
                if (!relevantJobs.contains(job)) {
                    relevantJobs.add(job);
                }

                if (relevantJobs.size() >= 20) {
                    break;
                }
            }
        }

        return relevantJobs;
    }

    private boolean isRelevant(String domain, String resumeText, String jobText) {
        if (domain.contains("java") && containsAny(jobText, "java", "backend", "software", "trainee")) {
            return true;
        }

        if (domain.contains("frontend") && containsAny(jobText, "frontend", "html", "css", "javascript", "web")) {
            return true;
        }

        if (domain.contains("qa") && containsAny(jobText, "qa", "sqa", "testing", "tester")) {
            return true;
        }

        if (domain.contains("ai") && containsAny(jobText, "ai", "machine learning", "python", "data")) {
            return true;
        }

        if (domain.contains("database") && containsAny(jobText, "database", "sql", "mysql", "data")) {
            return true;
        }

        for (String word : resumeText.split("\\s+")) {
            if (word.length() > 4 && jobText.contains(word)) {
                return true;
            }
        }

        return false;
    }

    private boolean containsAny(String text, String... keywords) {
        for (String keyword : keywords) {
            if (text.contains(keyword.toLowerCase())) {
                return true;
            }
        }

        return false;
    }
}
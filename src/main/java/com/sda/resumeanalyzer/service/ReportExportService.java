package com.sda.resumeanalyzer.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.sda.resumeanalyzer.model.AtsReport;
import com.sda.resumeanalyzer.model.MatchResult;
import com.sda.resumeanalyzer.model.Resume;

public class ReportExportService {

    public void exportReport(File file, AtsReport report, List<MatchResult> matches) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            Resume resume = report.getResume();

            writer.write("AI-BASED RESUME ANALYZER & JOB MATCHER REPORT\n");
            writer.write("================================================\n\n");

            writer.write("Candidate Name: " + resume.getCandidateName() + "\n");
            writer.write("Email: " + resume.getEmail() + "\n");
            writer.write("Phone: " + resume.getPhone() + "\n");
            writer.write("Target Role: " + resume.getTargetRole() + "\n");
            writer.write("Experience Level: " + resume.getExperienceLevel() + "\n");
            writer.write("Education: " + resume.getEducation() + "\n\n");

            writer.write("Detected Skills:\n");
            writer.write(resume.getSkills().toString() + "\n\n");

            writer.write("ATS SCORE BREAKDOWN\n");
            writer.write("-------------------\n");
            writer.write("Final ATS Score: " + String.format("%.2f", report.getAtsScore()) + "%\n");
            writer.write("Skills Score: " + String.format("%.2f", report.getSkillsScore()) + "%\n");
            writer.write("Contact Score: " + String.format("%.2f", report.getContactScore()) + "%\n");
            writer.write("Education Score: " + String.format("%.2f", report.getEducationScore()) + "%\n");
            writer.write("Project Score: " + String.format("%.2f", report.getProjectScore()) + "%\n");
            writer.write("Keyword Score: " + String.format("%.2f", report.getKeywordScore()) + "%\n");
            writer.write("Formatting Score: " + String.format("%.2f", report.getFormattingScore()) + "%\n\n");

            writer.write("Suggestions:\n");
            for (String suggestion : report.getSuggestions()) {
                writer.write("- " + suggestion + "\n");
            }

            writer.write("\nTop Job Matches:\n");
            writer.write("----------------\n");

            if (matches != null) {
                for (MatchResult match : matches) {
                    writer.write("\nJob: " + match.getJob().getTitle() + "\n");
                    writer.write("Company: " + match.getJob().getCompany() + "\n");
                    writer.write("Platform: " + match.getJob().getPlatform() + "\n");
                    writer.write("Location: " + match.getJob().getLocation() + "\n");
                    writer.write("Match: " + String.format("%.2f", match.getMatchPercentage()) + "%\n");
                    writer.write("Verdict: " + match.getVerdict() + "\n");
                    writer.write("Matched Skills: " + match.getMatchedSkills() + "\n");
                    writer.write("Missing Skills: " + match.getMissingSkills() + "\n");
                    writer.write("Apply/Search Link: " + match.getJob().getApplyLink() + "\n");
                }
            }
        }
    }
}
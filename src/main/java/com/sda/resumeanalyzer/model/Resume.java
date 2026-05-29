package com.sda.resumeanalyzer.model;

import java.util.ArrayList;
import java.util.List;

public class Resume {

    private String candidateName;
    private String email;
    private String phone;
    private String rawText;
    private List<String> skills;
    private String education;
    private String experienceLevel;
    private String targetRole;
    private boolean hasProjects;
    private boolean hasCertifications;

    public Resume() {
        this.skills = new ArrayList<>();
        this.candidateName = "Candidate";
        this.email = "Not found";
        this.phone = "Not found";
        this.education = "Not clearly mentioned";
        this.experienceLevel = "Beginner";
        this.targetRole = "Software Engineering Student";
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getRawText() {
        return rawText;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public String getTargetRole() {
        return targetRole;
    }

    public void setTargetRole(String targetRole) {
        this.targetRole = targetRole;
    }

    public boolean isHasProjects() {
        return hasProjects;
    }

    public void setHasProjects(boolean hasProjects) {
        this.hasProjects = hasProjects;
    }

    public boolean isHasCertifications() {
        return hasCertifications;
    }

    public void setHasCertifications(boolean hasCertifications) {
        this.hasCertifications = hasCertifications;
    }
}
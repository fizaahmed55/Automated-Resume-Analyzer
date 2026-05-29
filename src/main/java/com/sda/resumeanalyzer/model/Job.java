package com.sda.resumeanalyzer.model;

import java.util.List;

public class Job {

    private String title;
    private String company;
    private String platform;
    private String location;
    private String jobType;
    private String experienceLevel;
    private List<String> requiredSkills;
    private List<String> keywords;
    private String description;
    private String applyLink;

    public Job(String title, String company, String platform, String location,
               String jobType, String experienceLevel, List<String> requiredSkills,
               List<String> keywords, String description, String applyLink) {
        this.title = title;
        this.company = company;
        this.platform = platform;
        this.location = location;
        this.jobType = jobType;
        this.experienceLevel = experienceLevel;
        this.requiredSkills = requiredSkills;
        this.keywords = keywords;
        this.description = description;
        this.applyLink = applyLink;
    }

    public String getTitle() {
        return title;
    }

    public String getCompany() {
        return company;
    }

    public String getPlatform() {
        return platform;
    }

    public String getLocation() {
        return location;
    }

    public String getJobType() {
        return jobType;
    }

    public String getExperienceLevel() {
        return experienceLevel;
    }

    public List<String> getRequiredSkills() {
        return requiredSkills;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public String getDescription() {
        return description;
    }

    public String getApplyLink() {
        return applyLink;
    }
}
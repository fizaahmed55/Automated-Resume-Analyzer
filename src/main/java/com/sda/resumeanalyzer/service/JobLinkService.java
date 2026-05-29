package com.sda.resumeanalyzer.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class JobLinkService {

    public String generateLinkedInSearchLink(String role, String location) {
        return "https://www.linkedin.com/jobs/search/?keywords="
                + encode(role)
                + "&location="
                + encode(location);
    }

    public String generateIndeedSearchLink(String role, String location) {
        return "https://www.indeed.com/jobs?q="
                + encode(role)
                + "&l="
                + encode(location);
    }

    public String generateGoogleJobsSearchLink(String role, String location) {
        return "https://www.google.com/search?q="
                + encode(role + " jobs " + location);
    }

    public String generateRemotiveSearchLink(String role) {
        return "https://remotive.com/remote-jobs/search?search="
                + encode(role);
    }

    public String generateRozeeSearchLink(String role) {
        return "https://www.rozee.pk/job/jsearch/q/"
                + encode(role);
    }

    public String generateInternshalaSearchLink(String role) {
        return "https://internshala.com/internships/keywords-"
                + encode(role);
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
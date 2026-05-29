package com.sda.resumeanalyzer.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sda.resumeanalyzer.model.Job;
import com.sda.resumeanalyzer.service.JobLinkService;

public class JobRepository {

    private final JobLinkService linkService = new JobLinkService();

    public List<Job> getAllJobs() {
        List<Job> jobs = new ArrayList<>();

        jobs.add(new Job(
                "Java Developer Intern",
                "Multiple Companies",
                "LinkedIn",
                "Pakistan / Remote",
                "Internship",
                "Beginner",
                Arrays.asList("java", "oop", "sql", "git"),
                Arrays.asList("java", "oop", "database", "software engineering", "backend", "internship"),
                "Java internship for students with OOP, SQL, Git, and basic backend knowledge.",
                linkService.generateLinkedInSearchLink("Java Developer Intern", "Pakistan")
        ));

        jobs.add(new Job(
                "Junior Java Developer",
                "Software Houses",
                "LinkedIn",
                "Pakistan",
                "Entry Level",
                "Beginner",
                Arrays.asList("java", "oop", "mysql", "git"),
                Arrays.asList("java", "junior developer", "oop", "mysql", "github", "backend"),
                "Junior Java role suitable for candidates with Java, OOP, MySQL, and Git basics.",
                linkService.generateLinkedInSearchLink("Junior Java Developer", "Pakistan")
        ));

        jobs.add(new Job(
                "Trainee Software Engineer",
                "Software Companies",
                "Rozee.pk",
                "Pakistan",
                "Entry Level",
                "Beginner",
                Arrays.asList("java", "oop", "sql", "problem solving"),
                Arrays.asList("software engineering", "trainee", "java", "sql", "programming", "problem solving"),
                "Trainee software engineering role for fresh students and beginners.",
                linkService.generateRozeeSearchLink("Trainee Software Engineer")
        ));

        jobs.add(new Job(
                "Frontend Developer Intern",
                "Multiple Companies",
                "Indeed",
                "Remote",
                "Internship",
                "Beginner",
                Arrays.asList("html", "css", "javascript", "git"),
                Arrays.asList("frontend", "html", "css", "javascript", "responsive design", "web development"),
                "Frontend internship for students with HTML, CSS, JavaScript, and basic web development knowledge.",
                linkService.generateIndeedSearchLink("Frontend Developer Intern", "Remote")
        ));

        jobs.add(new Job(
                "Junior Frontend Developer",
                "Web Agencies",
                "LinkedIn",
                "Remote / Pakistan",
                "Entry Level",
                "Beginner",
                Arrays.asList("html", "css", "javascript"),
                Arrays.asList("frontend", "ui", "web", "javascript", "responsive", "portfolio"),
                "Junior frontend role focused on website UI, responsive pages, and JavaScript basics.",
                linkService.generateLinkedInSearchLink("Junior Frontend Developer", "Pakistan")
        ));

        jobs.add(new Job(
                "Remote Junior Web Developer",
                "Remote Companies",
                "Remotive",
                "Remote",
                "Entry Level",
                "Beginner",
                Arrays.asList("html", "css", "javascript", "problem solving"),
                Arrays.asList("remote", "web developer", "frontend", "javascript", "html", "css"),
                "Remote junior web development jobs for candidates with frontend basics.",
                linkService.generateRemotiveSearchLink("junior web developer")
        ));

        jobs.add(new Job(
                "Backend Developer Intern",
                "Tech Startups",
                "Google Jobs",
                "Remote",
                "Internship",
                "Beginner",
                Arrays.asList("java", "sql", "api", "database"),
                Arrays.asList("backend", "api", "database", "java", "server", "sql"),
                "Backend internship for students who know Java, SQL, APIs, and basic database concepts.",
                linkService.generateGoogleJobsSearchLink("Backend Developer Intern", "Remote")
        ));

        jobs.add(new Job(
                "Database Assistant Intern",
                "Multiple Companies",
                "LinkedIn",
                "Pakistan",
                "Internship",
                "Beginner",
                Arrays.asList("sql", "mysql", "database", "problem solving"),
                Arrays.asList("database", "sql", "mysql", "dbms", "queries", "tables"),
                "Suitable for students who know SQL, MySQL, and basic database concepts.",
                linkService.generateLinkedInSearchLink("Database Intern SQL MySQL", "Pakistan")
        ));

        jobs.add(new Job(
                "Junior Database Assistant",
                "Data Companies",
                "Indeed",
                "Remote",
                "Entry Level",
                "Beginner",
                Arrays.asList("sql", "mysql", "database"),
                Arrays.asList("database assistant", "sql", "mysql", "data entry", "queries"),
                "Entry-level database assistant role for SQL and MySQL learners.",
                linkService.generateIndeedSearchLink("Junior Database Assistant SQL", "Remote")
        ));

        jobs.add(new Job(
                "SQA Intern",
                "Software Companies",
                "LinkedIn",
                "Pakistan",
                "Internship",
                "Beginner",
                Arrays.asList("testing", "sqa", "qa", "communication"),
                Arrays.asList("sqa", "qa", "testing", "test cases", "bug reports", "quality assurance"),
                "SQA internship for students interested in software testing and quality assurance.",
                linkService.generateLinkedInSearchLink("SQA Intern", "Pakistan")
        ));

        jobs.add(new Job(
                "QA Tester Intern",
                "Multiple Companies",
                "Google Jobs",
                "Pakistan",
                "Internship",
                "Beginner",
                Arrays.asList("testing", "qa", "problem solving"),
                Arrays.asList("qa tester", "manual testing", "test case", "bug", "software testing"),
                "Beginner QA testing role for students with testing basics.",
                linkService.generateGoogleJobsSearchLink("QA Tester Intern", "Pakistan")
        ));

        jobs.add(new Job(
                "Manual Testing Intern",
                "IT Companies",
                "Indeed",
                "Remote",
                "Internship",
                "Beginner",
                Arrays.asList("manual testing", "testing", "qa"),
                Arrays.asList("manual testing", "test cases", "bug tracking", "quality", "qa"),
                "Internship for candidates learning manual testing and software quality.",
                linkService.generateIndeedSearchLink("Manual Testing Intern", "Remote")
        ));

        jobs.add(new Job(
                "Python Programming Intern",
                "Multiple Companies",
                "Internshala",
                "Remote / India",
                "Internship",
                "Beginner",
                Arrays.asList("python", "problem solving", "git"),
                Arrays.asList("python", "programming", "scripts", "internship", "automation"),
                "Beginner-friendly Python internships for students with programming basics.",
                linkService.generateInternshalaSearchLink("python")
        ));

        jobs.add(new Job(
                "AI / Machine Learning Intern",
                "AI Startups",
                "Google Jobs",
                "Remote",
                "Internship",
                "Beginner",
                Arrays.asList("python", "machine learning", "ai", "nlp"),
                Arrays.asList("ai", "machine learning", "python", "dataset", "model", "nlp"),
                "Suitable for students who have started learning AI, ML, NLP, or Python.",
                linkService.generateGoogleJobsSearchLink("AI Machine Learning Intern", "Remote")
        ));

        jobs.add(new Job(
                "Data Analyst Intern",
                "Data Teams",
                "LinkedIn",
                "Remote / Pakistan",
                "Internship",
                "Beginner",
                Arrays.asList("sql", "python", "database"),
                Arrays.asList("data analyst", "sql", "python", "excel", "dashboard", "data"),
                "Data analyst internship for students with SQL, Python, and analytical skills.",
                linkService.generateLinkedInSearchLink("Data Analyst Intern", "Pakistan")
        ));

        jobs.add(new Job(
                "WordPress Developer Intern",
                "Small Businesses",
                "Indeed",
                "Remote",
                "Internship",
                "Beginner",
                Arrays.asList("html", "css", "javascript", "communication"),
                Arrays.asList("wordpress", "html", "css", "website", "frontend", "content"),
                "Good for students with basic web design and frontend knowledge.",
                linkService.generateIndeedSearchLink("WordPress Developer Intern", "Remote")
        ));

        jobs.add(new Job(
                "UI/UX Design Intern",
                "Design Teams",
                "LinkedIn",
                "Remote",
                "Internship",
                "Beginner",
                Arrays.asList("ui", "ux", "figma", "communication"),
                Arrays.asList("ui", "ux", "figma", "wireframe", "prototype", "design"),
                "UI/UX internship for students interested in product design and user interfaces.",
                linkService.generateLinkedInSearchLink("UI UX Design Intern", "Remote")
        ));

        jobs.add(new Job(
                "Technical Content Writer Intern",
                "Tech Blogs",
                "LinkedIn",
                "Remote",
                "Part-time",
                "Beginner",
                Arrays.asList("documentation", "communication", "technical writing"),
                Arrays.asList("technical writing", "documentation", "blog", "software", "content"),
                "Suitable for students who can write about programming and software engineering.",
                linkService.generateLinkedInSearchLink("Technical Content Writer Intern", "Remote")
        ));

        jobs.add(new Job(
                "IT Support Intern",
                "IT Services",
                "Indeed",
                "Pakistan",
                "Internship",
                "Beginner",
                Arrays.asList("communication", "problem solving", "documentation"),
                Arrays.asList("it support", "helpdesk", "troubleshooting", "support", "documentation"),
                "IT support internship for students with communication and troubleshooting interest.",
                linkService.generateIndeedSearchLink("IT Support Intern", "Pakistan")
        ));

        jobs.add(new Job(
                "Mobile App Developer Intern",
                "App Companies",
                "Google Jobs",
                "Remote",
                "Internship",
                "Beginner",
                Arrays.asList("java", "oop", "problem solving"),
                Arrays.asList("mobile app", "android", "java", "ui", "application", "internship"),
                "Mobile app internship for students with Java/OOP and app development interest.",
                linkService.generateGoogleJobsSearchLink("Mobile App Developer Intern", "Remote")
        ));

        return jobs;
    }
}
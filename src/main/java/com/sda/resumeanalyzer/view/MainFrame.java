package com.sda.resumeanalyzer.view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import com.sda.resumeanalyzer.model.AtsReport;
import com.sda.resumeanalyzer.model.MatchResult;
import com.sda.resumeanalyzer.model.Resume;
import com.sda.resumeanalyzer.observer.JobAlertSubject;
import com.sda.resumeanalyzer.observer.UserNotificationObserver;
import com.sda.resumeanalyzer.parser.ResumeParser;
import com.sda.resumeanalyzer.parser.ResumeParserFactory;
import com.sda.resumeanalyzer.service.AtsScoringService;
import com.sda.resumeanalyzer.service.JobMatchingService;
import com.sda.resumeanalyzer.service.ResumeAnalysisService;
import com.sda.resumeanalyzer.service.ResumeDomainService;
import com.sda.resumeanalyzer.util.BrowserUtil;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentPanel;

    private JTextArea resumeTextArea;
    private JEditorPane resumePreviewPane;
    private JEditorPane analysisPane;
    private JEditorPane atsResumePane;
    private JEditorPane reportPane;

    private JPanel jobCardsPanel;
    private JScrollPane jobCardsScrollPane;

    private JLabel dashboardScoreLabel;
    private JLabel dashboardSkillsLabel;
    private JLabel dashboardJobsLabel;
    private JLabel dashboardRoleLabel;
    private JLabel uploadStatusLabel;
    private JLabel detectedDomainLabel;
    private JLabel jobTrackerSummaryLabel;

    private ScoreDonutPanel atsDonutPanel;
    private ScoreBarsPanel scoreBarsPanel;
    private SkillsPanel hardSkillsPanel;
    private SkillsPanel softSkillsPanel;
    private KeywordUsagePanel keywordUsagePanel;

    private Resume currentResume;
    private AtsReport currentReport;
    private String currentDomain = "Not detected";
    private List<MatchResult> matchedResults = new ArrayList<>();

    private final ResumeAnalysisService resumeAnalysisService;
    private final AtsScoringService atsScoringService;
    private final JobMatchingService jobMatchingService;
    private final ResumeDomainService resumeDomainService;
    private final JobAlertSubject jobAlertSubject;

    private final Color tealDark = new Color(0, 82, 73);
    private final Color teal = new Color(0, 105, 92);
    private final Color tealSoft = new Color(139, 200, 190);
    private final Color tealVeryLight = new Color(232, 247, 245);

    private final Color cobalt = new Color(39, 39, 174);
    private final Color cobaltLight = new Color(84, 104, 235);

    private final Color mustard = new Color(245, 184, 0);
    private final Color mustardDark = new Color(218, 160, 0);

    private final Color backgroundStart = new Color(247, 252, 251);
    private final Color backgroundEnd = new Color(232, 238, 255);
    private final Color cardWhite = Color.WHITE;
    private final Color border = new Color(221, 232, 235);
    private final Color textDark = new Color(31, 41, 55);
    private final Color textMuted = new Color(107, 114, 128);
    private final Color lightGray = new Color(243, 244, 246);

    public MainFrame() {
        resumeAnalysisService = new ResumeAnalysisService();
        atsScoringService = new AtsScoringService();
        jobMatchingService = new JobMatchingService();
        resumeDomainService = new ResumeDomainService();

        jobAlertSubject = new JobAlertSubject();
        jobAlertSubject.addObserver(new UserNotificationObserver());

        setTitle("AI CareerFit Resume Analyzer");
        setSize(1320, 780);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createTopNavigation(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);
    }

    private JPanel createTopNavigation() {
        JPanel nav = new JPanel(null);
        nav.setPreferredSize(new Dimension(1320, 78));
        nav.setBackground(Color.WHITE);
        nav.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, border));

        JLabel logo = new JLabel("careerfit");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 34));
        logo.setForeground(Color.BLACK);
        logo.setBounds(30, 18, 190, 42);
        nav.add(logo);

        JLabel logoMark = new JLabel("●");
        logoMark.setForeground(teal);
        logoMark.setFont(new Font("Segoe UI", Font.BOLD, 32));
        logoMark.setBounds(160, 18, 40, 40);
        nav.add(logoMark);

        JButton home = createNavButton("Home");
        home.setBounds(330, 20, 80, 38);
        home.addActionListener(e -> showCard("dashboard"));
        nav.add(home);

        JButton upload = createNavButton("AI Resume Analyzer");
        upload.setBounds(420, 20, 175, 38);
        upload.addActionListener(e -> showCard("upload"));
        nav.add(upload);

        JButton jobs = createNavButton("Job Tracker");
        jobs.setBounds(605, 20, 120, 38);
        jobs.addActionListener(e -> showCard("jobs"));
        nav.add(jobs);

        JButton analysis = createNavButton("ATS Analyzer");
        analysis.setBounds(735, 20, 135, 38);
        analysis.addActionListener(e -> showCard("analysis"));
        nav.add(analysis);

        JButton atsResume = createNavButton("ATS-Friendly Resume");
        atsResume.setBounds(880, 20, 190, 38);
        atsResume.addActionListener(e -> {
            updateAtsFriendlyResume();
            showCard("atsResume");
        });
        nav.add(atsResume);

        JButton report = createNavButton("Report");
        report.setBounds(1080, 20, 95, 38);
        report.addActionListener(e -> showCard("report"));
        nav.add(report);

        return nav;
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Color.WHITE);
        button.setForeground(textDark);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setForeground(teal);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setForeground(textDark);
            }
        });

        return button;
    }

    private JPanel createMainContent() {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(createDashboardPage(), "dashboard");
        contentPanel.add(createUploadPage(), "upload");
        contentPanel.add(createJobsPage(), "jobs");
        contentPanel.add(createAnalysisPage(), "analysis");
        contentPanel.add(createAtsResumePage(), "atsResume");
        contentPanel.add(createReportPage(), "report");

        return contentPanel;
    }

    private JScrollPane createDashboardPage() {
        JPanel page = createScrollablePage(1160);

        JLabel section = new JLabel("Resume Intelligence Platform");
        section.setFont(new Font("Segoe UI", Font.BOLD, 24));
        section.setForeground(textDark);
        section.setBounds(35, 85, 420, 35);
        page.add(section);

        JLabel title = new JLabel("<html>Analyze your resume,<br>find matching jobs,<br>then calculate ATS score</html>");
        title.setFont(new Font("Segoe UI", Font.BOLD, 40));
        title.setForeground(textDark);
        title.setBounds(35, 128, 620, 160);
        page.add(title);

        JTextArea intro = createTransparentText();
        intro.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        intro.setText(
                "Upload your resume, detect your domain and skills, find relevant jobs, calculate ATS score against each job, "
                        + "and generate an ATS-friendly resume draft."
        );
        intro.setBounds(35, 305, 570, 115);
        page.add(intro);

        JButton cta = createYellowButton("Analyze Resume");
        cta.setBounds(35, 450, 230, 54);
        cta.setFont(new Font("Segoe UI", Font.BOLD, 17));
        cta.addActionListener(e -> showCard("upload"));
        page.add(cta);

        HeroMockupPanel hero = new HeroMockupPanel();
        hero.setBounds(640, 70, 620, 470);
        page.add(hero);

        JPanel stat1 = createStatCard("Resume Health", "0%", "General resume score", teal, 35, 610);
        JPanel stat2 = createStatCard("Detected Skills", "0", "Hard and soft skills", cobaltLight, 350, 610);
        JPanel stat3 = createStatCard("Matched Jobs", "0", "Job-specific results", mustard, 665, 610);
        JPanel stat4 = createStatCard("Detected Role", "Pending", "Resume domain", tealSoft, 980, 610);

        dashboardScoreLabel = findValueLabel(stat1);
        dashboardSkillsLabel = findValueLabel(stat2);
        dashboardJobsLabel = findValueLabel(stat3);
        dashboardRoleLabel = findValueLabel(stat4);

        page.add(stat1);
        page.add(stat2);
        page.add(stat3);
        page.add(stat4);

        WorkflowSmartArtPanel workflow = new WorkflowSmartArtPanel();
        workflow.setBounds(35, 790, 1225, 270);
        page.add(workflow);

        return wrapWithScroll(page);
    }

    private JScrollPane createUploadPage() {
        JPanel page = createScrollablePage(1020);

        createPageHero(
                page,
                "AI Resume Analyzer",
                "Upload your resume. The system extracts skills, detects your domain, and prepares job matches.",
                "Resume Upload",
                35,
                25
        );

        JPanel uploadCard = createFilledInfoPanel(35, 230, 1225, 205);

        JLabel cardTitle = createSectionTitle("Upload Resume File");
        cardTitle.setBounds(28, 25, 350, 30);
        uploadCard.add(cardTitle);

        JLabel desc = createMutedLabel("Supported formats: PDF and DOCX. Resume analysis and job matching will run automatically after selection.");
        desc.setBounds(28, 65, 950, 25);
        uploadCard.add(desc);

        uploadStatusLabel = new JLabel("Status: Waiting for resume upload");
        uploadStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        uploadStatusLabel.setForeground(textMuted);
        uploadStatusLabel.setBounds(28, 98, 620, 24);
        uploadCard.add(uploadStatusLabel);

        detectedDomainLabel = new JLabel("Detected Domain: Not detected");
        detectedDomainLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        detectedDomainLabel.setForeground(textMuted);
        detectedDomainLabel.setBounds(28, 122, 620, 24);
        uploadCard.add(detectedDomainLabel);

        JButton uploadButton = createPrimaryButton("Choose Resume File");
        uploadButton.setBounds(28, 153, 210, 42);
        uploadButton.addActionListener(e -> uploadResume());
        uploadCard.add(uploadButton);

        JButton continueButton = createYellowButton("Go to Job Tracker");
        continueButton.setBounds(258, 153, 190, 42);
        continueButton.addActionListener(e -> {
            if (currentResume == null) {
                JOptionPane.showMessageDialog(this, "Please upload a resume first.");
                return;
            }

            renderJobCards();
            showCard("jobs");
        });
        uploadCard.add(continueButton);

        page.add(uploadCard);

        resumeTextArea = new JTextArea();

        resumePreviewPane = createHtmlPane();
        resumePreviewPane.setText(buildEmptyResumePreviewHtml());

        JPanel previewFrame = createFilledInfoPanel(35, 470, 1225, 455);
        JLabel previewTitle = createSectionTitle("Extracted Resume Content");
        previewTitle.setBounds(24, 16, 400, 30);
        previewFrame.add(previewTitle);

        JLabel previewSubtitle = createMutedLabel("This section shows a clean preview of the extracted resume text.");
        previewSubtitle.setBounds(24, 47, 700, 25);
        previewFrame.add(previewSubtitle);

        JScrollPane resumeScroll = new JScrollPane(resumePreviewPane);
        resumeScroll.getVerticalScrollBar().setUnitIncrement(24);
        resumeScroll.setBounds(24, 82, 1175, 345);
        resumeScroll.setBorder(BorderFactory.createLineBorder(new Color(210, 222, 226)));
        previewFrame.add(resumeScroll);

        page.add(previewFrame);

        return wrapWithScroll(page);
    }

    private JScrollPane createJobsPage() {
        JPanel page = createScrollablePage(1240);

        createPageHero(
                page,
                "Job Tracker",
                "Relevant jobs are selected from your detected resume domain and skills before ATS scoring is shown.",
                "Job Discovery",
                35,
                25
        );

        JPanel actionCard = createFilledInfoPanel(35, 230, 1225, 115);

        JButton matchButton = createPrimaryButton("Refresh Job Matches");
        matchButton.setBounds(25, 34, 200, 46);
        matchButton.addActionListener(e -> matchJobs());
        actionCard.add(matchButton);

        JButton atsButton = createYellowButton("Continue to ATS Analyzer");
        atsButton.setBounds(245, 34, 230, 46);
        atsButton.addActionListener(e -> {
            if (currentResume == null) {
                JOptionPane.showMessageDialog(this, "Please upload a resume first.");
                showCard("upload");
                return;
            }

            showCard("analysis");
        });
        actionCard.add(atsButton);

        JButton resumeButton = createOutlineButton("ATS-Friendly Resume");
        resumeButton.setBounds(495, 34, 210, 46);
        resumeButton.addActionListener(e -> {
            updateAtsFriendlyResume();
            showCard("atsResume");
        });
        actionCard.add(resumeButton);

        jobTrackerSummaryLabel = createMutedLabel("Upload a resume to detect domain and find relevant jobs.");
        jobTrackerSummaryLabel.setBounds(735, 45, 470, 25);
        actionCard.add(jobTrackerSummaryLabel);

        page.add(actionCard);

        jobCardsPanel = new JPanel(null);
        jobCardsPanel.setOpaque(false);
        jobCardsPanel.setPreferredSize(new Dimension(1190, 720));

        jobCardsScrollPane = new JScrollPane(jobCardsPanel);
        jobCardsScrollPane.setBounds(35, 380, 1225, 685);
        jobCardsScrollPane.setBorder(null);
        jobCardsScrollPane.getViewport().setOpaque(false);
        jobCardsScrollPane.setOpaque(false);
        jobCardsScrollPane.getVerticalScrollBar().setUnitIncrement(24);
        jobCardsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jobCardsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        page.add(jobCardsScrollPane);

        JPanel insightCard = createFilledInfoPanel(35, 1085, 1225, 75);
        JLabel insight = new JLabel("Job-specific ATS scores are calculated by comparing your resume against each job's skills and keywords.");
        insight.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        insight.setForeground(textMuted);
        insight.setBounds(25, 22, 1100, 30);
        insightCard.add(insight);
        page.add(insightCard);

        return wrapWithScroll(page);
    }

    private JScrollPane createAnalysisPage() {
        JPanel page = createScrollablePage(1360);

        createPageHero(
                page,
                "ATS Analyzer",
                "ATS score is calculated separately for each matched job using skills, keywords, structure, contact details, and relevance.",
                "Job-Specific ATS",
                35,
                25
        );

        atsDonutPanel = new ScoreDonutPanel(0);
        atsDonutPanel.setBounds(35, 235, 330, 300);
        page.add(atsDonutPanel);

        scoreBarsPanel = new ScoreBarsPanel(null);
        scoreBarsPanel.setBounds(395, 235, 430, 300);
        page.add(scoreBarsPanel);

        keywordUsagePanel = new KeywordUsagePanel(null);
        keywordUsagePanel.setBounds(855, 235, 405, 300);
        page.add(keywordUsagePanel);

        hardSkillsPanel = new SkillsPanel("Hard Skills", new ArrayList<>());
        hardSkillsPanel.setBounds(35, 565, 595, 165);
        page.add(hardSkillsPanel);

        softSkillsPanel = new SkillsPanel("Soft Skills", new ArrayList<>());
        softSkillsPanel.setBounds(665, 565, 595, 165);
        page.add(softSkillsPanel);

        JPanel actionCard = createFilledInfoPanel(35, 760, 1225, 92);

        JButton runButton = createPrimaryButton("Refresh ATS Check");
        runButton.setBounds(25, 23, 185, 46);
        runButton.addActionListener(e -> analyzeResume());
        actionCard.add(runButton);

        JButton resumeButton = createYellowButton("Generate ATS Resume");
        resumeButton.setBounds(230, 23, 210, 46);
        resumeButton.addActionListener(e -> {
            updateAtsFriendlyResume();
            showCard("atsResume");
        });
        actionCard.add(resumeButton);

        JLabel tip = createMutedLabel("The detailed ATS report below uses the top matched jobs and their job-specific scores.");
        tip.setBounds(475, 33, 640, 25);
        actionCard.add(tip);

        page.add(actionCard);

        JPanel analysisFrame = createFilledInfoPanel(35, 885, 1225, 360);
        JLabel frameTitle = createSectionTitle("Detailed Job-Specific ATS Report");
        frameTitle.setBounds(24, 16, 500, 30);
        analysisFrame.add(frameTitle);

        analysisPane = createHtmlPane();
        analysisPane.setText(buildEmptyAnalysisHtml());

        JScrollPane details = new JScrollPane(analysisPane);
        details.getVerticalScrollBar().setUnitIncrement(24);
        details.setBounds(24, 58, 1175, 275);
        details.setBorder(BorderFactory.createLineBorder(new Color(210, 222, 226)));
        analysisFrame.add(details);

        page.add(analysisFrame);

        return wrapWithScroll(page);
    }

    private JScrollPane createAtsResumePage() {
        JPanel page = createScrollablePage(1030);

        createPageHero(
                page,
                "ATS-Friendly Resume",
                "Generate a cleaner, job-targeted resume draft based on your actual resume and top matched jobs.",
                "Resume Optimization",
                35,
                25
        );

        JPanel actionCard = createFilledInfoPanel(35, 230, 1225, 100);

        JButton generateButton = createPrimaryButton("Generate ATS-Friendly Resume");
        generateButton.setBounds(25, 27, 260, 46);
        generateButton.addActionListener(e -> updateAtsFriendlyResume());
        actionCard.add(generateButton);

        JButton reportButton = createYellowButton("Continue to Report");
        reportButton.setBounds(305, 27, 190, 46);
        reportButton.addActionListener(e -> {
            previewReport();
            showCard("report");
        });
        actionCard.add(reportButton);

        JLabel help = createMutedLabel("This draft gives honest resume improvements. Do not add fake skills or fake experience.");
        help.setBounds(530, 38, 650, 25);
        actionCard.add(help);

        page.add(actionCard);

        JPanel resumeFrame = createFilledInfoPanel(35, 365, 1225, 560);
        JLabel title = createSectionTitle("ATS-Friendly Resume Draft");
        title.setBounds(24, 16, 500, 30);
        resumeFrame.add(title);

        JLabel sub = createMutedLabel("A cleaner resume draft based on your detected domain and strongest job match.");
        sub.setBounds(24, 47, 700, 25);
        resumeFrame.add(sub);

        atsResumePane = createHtmlPane();
        atsResumePane.setText(buildEmptyAtsResumeHtml());

        JScrollPane resumeScroll = new JScrollPane(atsResumePane);
        resumeScroll.getVerticalScrollBar().setUnitIncrement(24);
        resumeScroll.setBounds(24, 82, 1175, 445);
        resumeScroll.setBorder(BorderFactory.createLineBorder(new Color(210, 222, 226)));
        resumeFrame.add(resumeScroll);

        page.add(resumeFrame);

        return wrapWithScroll(page);
    }

    private JScrollPane createReportPage() {
        JPanel page = createScrollablePage(1030);

        createPageHero(
                page,
                "Professional Report",
                "Preview and export a polished report with resume analysis, detected domain, job-specific ATS scores, and ATS-friendly resume guidance.",
                "Career Report",
                35,
                25
        );

        JPanel actionCard = createFilledInfoPanel(35, 230, 1225, 100);

        JButton previewButton = createPrimaryButton("Preview Report");
        previewButton.setBounds(25, 27, 180, 46);
        previewButton.addActionListener(e -> previewReport());
        actionCard.add(previewButton);

        JButton exportButton = createYellowButton("Export HTML");
        exportButton.setBounds(225, 27, 150, 46);
        exportButton.addActionListener(e -> exportReport());
        actionCard.add(exportButton);

        JButton restartButton = createOutlineButton("Start New Analysis");
        restartButton.setBounds(395, 27, 190, 46);
        restartButton.addActionListener(e -> {
            resetApplication();
            showCard("upload");
        });
        actionCard.add(restartButton);

        JLabel help = createMutedLabel("The report includes job-specific ATS scores and tailored resume recommendations.");
        help.setBounds(620, 38, 520, 25);
        actionCard.add(help);

        page.add(actionCard);

        JPanel reportFrame = createFilledInfoPanel(35, 365, 1225, 560);
        JLabel title = createSectionTitle("Professional Career Report Preview");
        title.setBounds(24, 16, 500, 30);
        reportFrame.add(title);

        reportPane = createHtmlPane();
        reportPane.setText(buildEmptyReportHtml());

        JScrollPane reportScroll = new JScrollPane(reportPane);
        reportScroll.getVerticalScrollBar().setUnitIncrement(24);
        reportScroll.setBounds(24, 58, 1175, 470);
        reportScroll.setBorder(BorderFactory.createLineBorder(new Color(210, 222, 226)));
        reportFrame.add(reportScroll);

        page.add(reportFrame);

        return wrapWithScroll(page);
    }

    private void uploadResume() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                ResumeParser parser = ResumeParserFactory.getParser(file.getName());
                String text = parser.parse(file);

                resumeTextArea.setText(text);

                currentResume = resumeAnalysisService.analyzeResume(text);
                currentReport = atsScoringService.generateReport(currentResume);
                currentDomain = resumeDomainService.detectDomain(currentResume);
                matchedResults = jobMatchingService.matchJobs(currentResume);

                resumePreviewPane.setText(buildResumePreviewHtml(text));
                resumePreviewPane.setCaretPosition(0);

                updateAnalysisUI();
                renderJobCards();
                updateAtsFriendlyResume();

                uploadStatusLabel.setText("Status: Resume analyzed and jobs matched successfully");
                uploadStatusLabel.setForeground(tealDark);

                detectedDomainLabel.setText("Detected Domain: " + currentDomain);
                detectedDomainLabel.setForeground(tealDark);

                if (jobTrackerSummaryLabel != null) {
                    jobTrackerSummaryLabel.setText("Detected domain: " + currentDomain + " | Jobs found: " + matchedResults.size());
                }

                JOptionPane.showMessageDialog(
                        this,
                        "Resume analyzed successfully.\nDetected Domain: " + currentDomain + "\nJobs have been matched against this resume."
                );

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error reading resume: " + ex.getMessage());
            }
        }
    }

    private void analyzeResume() {
        if (resumeTextArea == null || resumeTextArea.getText() == null || resumeTextArea.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please upload a resume first.");
            showCard("upload");
            return;
        }

        currentResume = resumeAnalysisService.analyzeResume(resumeTextArea.getText());
        currentReport = atsScoringService.generateReport(currentResume);
        currentDomain = resumeDomainService.detectDomain(currentResume);
        matchedResults = jobMatchingService.matchJobs(currentResume);

        updateAnalysisUI();
        renderJobCards();
        updateAtsFriendlyResume();

        JOptionPane.showMessageDialog(this, "Resume, jobs, and ATS analysis refreshed successfully.");
        showCard("analysis");
    }

    private void matchJobs() {
        if (currentResume == null) {
            JOptionPane.showMessageDialog(this, "Please upload a resume first.");
            showCard("upload");
            return;
        }

        matchedResults = jobMatchingService.matchJobs(currentResume);

        if (dashboardJobsLabel != null) {
            dashboardJobsLabel.setText(String.valueOf(matchedResults.size()));
        }

        if (jobTrackerSummaryLabel != null) {
            jobTrackerSummaryLabel.setText("Detected domain: " + currentDomain + " | Jobs found: " + matchedResults.size());
        }

        renderJobCards();
        updateAnalysisUI();
        updateAtsFriendlyResume();

        showCard("jobs");
    }

    private void renderJobCards() {
        if (jobCardsPanel == null) {
            return;
        }

        jobCardsPanel.removeAll();

        if (matchedResults == null || matchedResults.isEmpty()) {
            JLabel empty = new JLabel("No jobs found yet. Upload and analyze a resume first.");
            empty.setFont(new Font("Segoe UI", Font.BOLD, 16));
            empty.setForeground(textMuted);
            empty.setBounds(25, 25, 600, 30);
            jobCardsPanel.add(empty);
            jobCardsPanel.setPreferredSize(new Dimension(1185, 720));
        } else {
            int y = 0;

            for (MatchResult result : matchedResults) {
                JPanel card = createJobCard(result);
                card.setBounds(0, y, 1185, 145);
                jobCardsPanel.add(card);
                y += 165;
            }

            jobCardsPanel.setPreferredSize(new Dimension(1185, Math.max(720, y + 20)));
        }

        jobCardsPanel.revalidate();
        jobCardsPanel.repaint();

        if (jobCardsScrollPane != null) {
            jobCardsScrollPane.revalidate();
            jobCardsScrollPane.repaint();
        }
    }

    private void updateAnalysisUI() {
        if (currentResume == null || currentReport == null) {
            return;
        }

        int score = (int) Math.round(currentReport.getAtsScore());

        dashboardScoreLabel.setText(score + "%");
        dashboardSkillsLabel.setText(String.valueOf(currentResume.getSkills().size()));
        dashboardJobsLabel.setText(String.valueOf(matchedResults == null ? 0 : matchedResults.size()));
        dashboardRoleLabel.setText(shorten(currentDomain, 16));

        if (atsDonutPanel != null) {
            int topJobScore = getTopJobScoreOrGeneralScore(score);
            atsDonutPanel.setScore(topJobScore);
        }

        if (scoreBarsPanel != null) {
            scoreBarsPanel.setReport(currentReport);
        }

        if (keywordUsagePanel != null) {
            keywordUsagePanel.setReport(currentReport);
        }

        if (hardSkillsPanel != null) {
            hardSkillsPanel.setSkills(getHardSkills(currentResume.getSkills()));
        }

        if (softSkillsPanel != null) {
            softSkillsPanel.setSkills(getSoftSkills(currentResume.getSkills()));
        }

        if (analysisPane != null) {
            analysisPane.setText(buildAnalysisHtml());
            analysisPane.setCaretPosition(0);
        }
    }

    private int getTopJobScoreOrGeneralScore(int generalScore) {
        if (matchedResults != null && !matchedResults.isEmpty()) {
            return (int) Math.round(matchedResults.get(0).getMatchPercentage());
        }

        return generalScore;
    }

    private void updateAtsFriendlyResume() {
        if (atsResumePane == null) {
            return;
        }

        if (currentResume == null) {
            atsResumePane.setText(buildEmptyAtsResumeHtml());
            return;
        }

        atsResumePane.setText(buildAtsFriendlyResumeHtml());
        atsResumePane.setCaretPosition(0);
    }

    private void previewReport() {
        if (currentReport == null) {
            JOptionPane.showMessageDialog(this, "Please analyze a resume first.");
            showCard("upload");
            return;
        }

        reportPane.setText(buildReportHtml());
        reportPane.setCaretPosition(0);
        showCard("report");
    }

    private void exportReport() {
        if (currentReport == null) {
            JOptionPane.showMessageDialog(this, "Please analyze a resume first.");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("AI_CareerFit_Professional_Report.html"));

        int result = chooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            try (FileWriter writer = new FileWriter(chooser.getSelectedFile())) {
                writer.write(buildReportHtml());
                JOptionPane.showMessageDialog(this, "Professional HTML report exported successfully.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error exporting report: " + ex.getMessage());
            }
        }
    }

    private JPanel createJobCard(MatchResult result) {
        JPanel card = createFilledInfoPanel(0, 0, 1185, 145);

        JLabel platform = new JLabel(result.getJob().getPlatform());
        platform.setOpaque(true);
        platform.setBackground(tealVeryLight);
        platform.setForeground(tealDark);
        platform.setHorizontalAlignment(SwingConstants.CENTER);
        platform.setFont(new Font("Segoe UI", Font.BOLD, 12));
        platform.setBounds(25, 18, 110, 26);
        card.add(platform);

        JLabel title = new JLabel(result.getJob().getTitle());
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(textDark);
        title.setBounds(25, 52, 430, 28);
        card.add(title);

        JLabel company = new JLabel(result.getJob().getCompany() + " • " + result.getJob().getLocation() + " • " + result.getJob().getJobType());
        company.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        company.setForeground(textMuted);
        company.setBounds(25, 84, 560, 25);
        card.add(company);

        JLabel skillLine = new JLabel("Required: " + shorten(result.getJob().getRequiredSkills().toString(), 65));
        skillLine.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        skillLine.setForeground(textMuted);
        skillLine.setBounds(25, 112, 570, 20);
        card.add(skillLine);

        JLabel matchLabel = new JLabel(String.format("%.0f%%", result.getMatchPercentage()), SwingConstants.CENTER);
        matchLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        matchLabel.setForeground(tealDark);
        matchLabel.setBounds(610, 28, 90, 40);
        card.add(matchLabel);

        JLabel matchText = new JLabel("ATS Score", SwingConstants.CENTER);
        matchText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        matchText.setForeground(textMuted);
        matchText.setBounds(610, 68, 90, 22);
        card.add(matchText);

        JLabel verdict = new JLabel(result.getVerdict(), SwingConstants.CENTER);
        verdict.setOpaque(true);

        if (result.getMatchPercentage() >= 80) {
            verdict.setBackground(new Color(220, 252, 231));
            verdict.setForeground(new Color(22, 101, 52));
        } else if (result.getMatchPercentage() >= 60) {
            verdict.setBackground(new Color(254, 249, 195));
            verdict.setForeground(new Color(133, 77, 14));
        } else {
            verdict.setBackground(new Color(254, 226, 226));
            verdict.setForeground(new Color(153, 27, 27));
        }

        verdict.setFont(new Font("Segoe UI", Font.BOLD, 12));
        verdict.setBounds(735, 28, 210, 32);
        card.add(verdict);

        JLabel missingTitle = new JLabel("Missing:");
        missingTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        missingTitle.setForeground(textDark);
        missingTitle.setBounds(735, 72, 70, 20);
        card.add(missingTitle);

        JLabel missing = new JLabel(shorten(result.getMissingSkills().toString(), 48));
        missing.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        missing.setForeground(textMuted);
        missing.setBounds(805, 72, 245, 20);
        card.add(missing);

        JLabel keywords = new JLabel("Keywords: " + shorten(result.getMissingKeywords().toString(), 42));
        keywords.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        keywords.setForeground(textMuted);
        keywords.setBounds(735, 98, 330, 20);
        card.add(keywords);

        JButton openLink = createYellowButton("Open Link");
        openLink.setBounds(1070, 52, 105, 42);
        openLink.addActionListener(e -> BrowserUtil.openLink(result.getJob().getApplyLink()));
        card.add(openLink);

        return card;
    }

    private String buildEmptyResumePreviewHtml() {
        return htmlStart("Resume Preview")
                + htmlHero("RESUME PREVIEW", "Uploaded Resume Text", "After uploading your resume, extracted content will appear here.")
                + htmlCard("No Resume Uploaded", "<p class='muted'>Choose a PDF or DOCX resume file to extract and analyze resume text.</p>")
                + htmlEnd();
    }

    private String buildResumePreviewHtml(String rawText) {
        String safeText = escapeHtml(rawText).replace("\n", "<br>");

        return htmlStart("Resume Preview")
                + htmlHero("RESUME PREVIEW", "Extracted Resume Content", "The resume text below was extracted from your uploaded file and prepared for analysis.")
                + "<table width='100%' cellpadding='8' cellspacing='0'><tr>"
                + metricCell("Characters", String.valueOf(rawText.length()))
                + metricCell("Estimated Words", String.valueOf(rawText.trim().isEmpty() ? 0 : rawText.trim().split("\\s+").length))
                + metricCell("Detected Domain", currentDomain)
                + metricCell("Jobs Matched", String.valueOf(matchedResults == null ? 0 : matchedResults.size()))
                + "</tr></table>"
                + htmlCard("Resume Text Preview", "<div class='resumeBox'>" + safeText + "</div>")
                + htmlEnd();
    }

    private String buildEmptyAnalysisHtml() {
        return htmlStart("ATS Analysis")
                + htmlHero("ATS REPORT", "Job-Specific ATS Analysis", "Upload a resume and find jobs to generate job-specific ATS scores.")
                + htmlCard("No Analysis Available", "<p class='muted'>Upload your resume first.</p>")
                + htmlEnd();
    }

    private String buildAnalysisHtml() {
        StringBuilder jobScores = new StringBuilder();

        if (matchedResults != null && !matchedResults.isEmpty()) {
            int count = 1;

            for (MatchResult result : matchedResults) {
                if (count > 8) {
                    break;
                }

                String body = ""
                        + row("Verdict", result.getVerdict())
                        + row("Matched Skills", result.getMatchedSkills().toString())
                        + row("Missing Skills", result.getMissingSkills().toString())
                        + row("Missing Keywords", result.getMissingKeywords().toString());

                jobScores.append(htmlSmallJobCard(
                        count + ". " + result.getJob().getTitle() + " - " + String.format("%.0f%%", result.getMatchPercentage()),
                        body
                ));

                count++;
            }
        } else {
            jobScores.append("<p class='muted'>No job-specific scores yet. Open Job Tracker and refresh matches.</p>");
        }

        return htmlStart("ATS Analysis Report")
                + htmlHero("ATS REPORT", "Job-Specific ATS Analysis", "Your resume is compared separately against each matched job description.")
                + "<table width='100%' cellpadding='8' cellspacing='0'><tr>"
                + metricCell("Detected Domain", currentDomain)
                + metricCell("General Resume Health", String.format("%.2f%%", currentReport.getAtsScore()))
                + metricCell("Jobs Compared", String.valueOf(matchedResults == null ? 0 : matchedResults.size()))
                + metricCell("Top ATS Score", matchedResults == null || matchedResults.isEmpty() ? "0%" : String.format("%.0f%%", matchedResults.get(0).getMatchPercentage()))
                + "</tr></table>"
                + htmlCard("Candidate Profile",
                        row("Candidate Name", currentResume.getCandidateName())
                                + row("Email", currentResume.getEmail())
                                + row("Phone", currentResume.getPhone())
                                + row("Target Role", currentResume.getTargetRole())
                                + row("Detected Domain", currentDomain)
                                + row("Education", currentResume.getEducation()))
                + htmlCard("Detected Skills", buildSkillListHtml(currentResume.getSkills()))
                + htmlCard("Job-Specific ATS Scores", jobScores.toString())
                + htmlEnd();
    }

    private String buildEmptyAtsResumeHtml() {
        return htmlStart("ATS-Friendly Resume")
                + htmlHero("ATS RESUME", "ATS-Friendly Resume Draft", "Upload your resume to generate a job-targeted resume draft.")
                + htmlCard("No Resume Available", "<p class='muted'>Upload your resume first.</p>")
                + htmlEnd();
    }

    private String buildAtsFriendlyResumeHtml() {
        String topJobTitle = "Target Role";

        if (matchedResults != null && !matchedResults.isEmpty()) {
            topJobTitle = matchedResults.get(0).getJob().getTitle();
        } else if (currentResume.getTargetRole() != null) {
            topJobTitle = currentResume.getTargetRole();
        }

        StringBuilder skills = new StringBuilder();

        for (String skill : currentResume.getSkills()) {
            skills.append("<li>").append(escapeHtml(skill)).append("</li>");
        }

        StringBuilder suggestions = new StringBuilder();

        if (matchedResults != null && !matchedResults.isEmpty()) {
            MatchResult best = matchedResults.get(0);

            for (String suggestion : best.getTailoredSuggestions()) {
                suggestions.append("<li>").append(escapeHtml(suggestion)).append("</li>");
            }
        } else {
            suggestions.append("<li>Add role-specific keywords from the job description.</li>");
            suggestions.append("<li>Add academic or personal projects related to your target role.</li>");
            suggestions.append("<li>Add GitHub and LinkedIn links if available.</li>");
        }

        return htmlStart("ATS-Friendly Resume")
                + htmlHero("ATS RESUME", "ATS-Friendly Resume Draft", "This draft is based on your resume profile and top job match. Edit it honestly before using.")
                + htmlCard(currentResume.getCandidateName(),
                        row("Email", currentResume.getEmail())
                                + row("Phone", currentResume.getPhone())
                                + row("Target Role", topJobTitle)
                                + row("Detected Domain", currentDomain))
                + htmlCard("Professional Summary",
                        "<p>Motivated software engineering student with foundational experience in "
                                + escapeHtml(joinSkillsForSentence(currentResume.getSkills()))
                                + ". Interested in " + escapeHtml(topJobTitle)
                                + " opportunities and eager to contribute through academic projects, problem solving, and continuous learning.</p>")
                + htmlCard("Core Skills", "<ul>" + skills + "</ul>")
                + htmlCard("Recommended Resume Improvements for Top Job Match", "<ul>" + suggestions + "</ul>")
                + htmlCard("Important Note", "<p>Only include skills, tools, projects, and achievements that you genuinely have.</p>")
                + htmlEnd();
    }

    private String buildEmptyReportHtml() {
        return htmlStart("Career Report")
                + htmlHero("CAREER REPORT", "Professional Resume Report", "Preview your final resume analysis and job matching report here.")
                + htmlCard("No Report Available", "<p class='muted'>Analyze a resume first.</p>")
                + htmlEnd();
    }

    private String buildReportHtml() {
        StringBuilder jobSection = new StringBuilder();

        if (matchedResults != null && !matchedResults.isEmpty()) {
            int jobNo = 1;

            for (MatchResult result : matchedResults) {
                String body = ""
                        + row("Company", result.getJob().getCompany())
                        + row("Platform", result.getJob().getPlatform())
                        + row("Verdict", result.getVerdict())
                        + row("Matched Skills", result.getMatchedSkills().toString())
                        + row("Missing Skills", result.getMissingSkills().toString())
                        + row("Tailored Suggestions", result.getTailoredSuggestions().toString())
                        + row("Apply Link", result.getJob().getApplyLink());

                jobSection.append(htmlSmallJobCard(
                        jobNo + ". " + result.getJob().getTitle() + " - " + String.format("%.0f%%", result.getMatchPercentage()),
                        body
                ));

                jobNo++;
            }
        } else {
            jobSection.append("<p class='muted'>No job matches generated yet.</p>");
        }

        return htmlStart("Professional Career Report")
                + htmlHero("CAREER REPORT", "AI CareerFit Professional Resume Report", "This report covers resume analysis, domain detection, job-specific ATS scoring, and ATS-friendly resume guidance.")
                + "<table width='100%' cellpadding='8' cellspacing='0'><tr>"
                + metricCell("Detected Domain", currentDomain)
                + metricCell("Resume Health", String.format("%.2f%%", currentReport.getAtsScore()))
                + metricCell("Jobs Compared", String.valueOf(matchedResults == null ? 0 : matchedResults.size()))
                + metricCell("Top ATS Score", matchedResults == null || matchedResults.isEmpty() ? "0%" : String.format("%.0f%%", matchedResults.get(0).getMatchPercentage()))
                + "</tr></table>"
                + htmlCard("Candidate Details",
                        row("Candidate Name", currentResume.getCandidateName())
                                + row("Email", currentResume.getEmail())
                                + row("Phone", currentResume.getPhone())
                                + row("Target Role", currentResume.getTargetRole())
                                + row("Detected Domain", currentDomain)
                                + row("Education", currentResume.getEducation()))
                + htmlCard("Detected Skills", buildSkillListHtml(currentResume.getSkills()))
                + htmlCard("Recommended Job Matches and ATS Scores", jobSection.toString())
                + htmlCard("Final Recommendation", "<p>Use the ATS-Friendly Resume tab to improve the resume for your top job match. Apply first to roles with higher ATS scores.</p>")
                + htmlEnd();
    }

    private JPanel createScrollablePage(int height) {
        return new GradientPagePanel(height);
    }

    private JScrollPane wrapWithScroll(JPanel page) {
        JScrollPane scroll = new JScrollPane(page);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUnitIncrement(26);
        scroll.getVerticalScrollBar().setBlockIncrement(130);
        return scroll;
    }

    private void createPageHero(JPanel page, String title, String subtitle, String tag, int x, int y) {
        JPanel hero = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);

                GradientPaint gradient = new GradientPaint(0, 0, tealDark, getWidth(), getHeight(), cobalt);
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);

                g2.setColor(new Color(255, 255, 255, 35));
                g2.fillOval(getWidth() - 260, -90, 340, 340);

                g2.setColor(new Color(245, 184, 0, 70));
                g2.fillOval(getWidth() - 420, 95, 160, 160);
            }
        };

        hero.setOpaque(false);
        hero.setBounds(x, y, 1225, 170);

        JLabel tagLabel = new JLabel(tag);
        tagLabel.setOpaque(true);
        tagLabel.setBackground(new Color(255, 255, 255, 45));
        tagLabel.setForeground(Color.WHITE);
        tagLabel.setHorizontalAlignment(SwingConstants.CENTER);
        tagLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tagLabel.setBounds(30, 25, 180, 30);
        hero.add(tagLabel);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 34));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(30, 65, 850, 42);
        hero.add(titleLabel);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(230, 245, 243));
        subtitleLabel.setBounds(32, 110, 1000, 28);
        hero.add(subtitleLabel);

        page.add(hero);
    }

    private JPanel createWhiteCard(int x, int y, int width, int height) {
        JPanel panel = new JPanel(null);
        panel.setBounds(x, y, width, height);
        panel.setBackground(cardWhite);
        panel.setBorder(BorderFactory.createLineBorder(border));
        return panel;
    }

    private JPanel createFilledInfoPanel(int x, int y, int width, int height) {
        JPanel panel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);

                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);

                g2.setColor(new Color(232, 247, 245));
                g2.fillRoundRect(0, 0, 8, getHeight(), 12, 12);

                g2.setColor(border);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 22, 22);
            }
        };

        panel.setOpaque(false);
        panel.setBounds(x, y, width, height);
        return panel;
    }

    private JPanel createStatCard(String label, String value, String description, Color accent, int x, int y) {
        JPanel panel = createWhiteCard(x, y, 280, 130);

        JLabel labelText = new JLabel(label);
        labelText.setFont(new Font("Segoe UI", Font.BOLD, 14));
        labelText.setForeground(textMuted);
        labelText.setBounds(22, 18, 180, 25);
        panel.add(labelText);

        JLabel valueText = new JLabel(value);
        valueText.setName("valueLabel");
        valueText.setFont(new Font("Segoe UI", Font.BOLD, 31));
        valueText.setForeground(textDark);
        valueText.setBounds(22, 48, 230, 42);
        panel.add(valueText);

        JLabel desc = new JLabel(description);
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        desc.setForeground(textMuted);
        desc.setBounds(22, 94, 230, 22);
        panel.add(desc);

        JLabel accentBar = new JLabel();
        accentBar.setOpaque(true);
        accentBar.setBackground(accent);
        accentBar.setBounds(0, 0, 7, 130);
        panel.add(accentBar);

        return panel;
    }

    private JLabel findValueLabel(JPanel panel) {
        for (java.awt.Component component : panel.getComponents()) {
            if (component instanceof JLabel && "valueLabel".equals(component.getName())) {
                return (JLabel) component;
            }
        }

        return new JLabel();
    }

    private JLabel createSectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 21));
        label.setForeground(textDark);
        return label;
    }

    private JLabel createMutedLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(textMuted);
        return label;
    }

    private JTextArea createTransparentText() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setOpaque(false);
        area.setForeground(textDark);
        area.setBorder(null);
        return area;
    }

    private JEditorPane createHtmlPane() {
        JEditorPane pane = new JEditorPane();
        pane.setContentType("text/html");
        pane.setEditable(false);
        pane.setBackground(Color.WHITE);
        pane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        pane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return pane;
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(teal);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(tealDark);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(teal);
            }
        });

        return button;
    }

    private JButton createYellowButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(mustard);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(mustardDark);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(mustard);
            }
        });

        return button;
    }

    private JButton createOutlineButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Color.WHITE);
        button.setForeground(textDark);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(textDark));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void showCard(String name) {
        cardLayout.show(contentPanel, name);
    }

    private String htmlStart(String title) {
        return "<html><head><style>"
                + "body{font-family:Segoe UI,Arial,sans-serif;background:#ffffff;color:#1f2937;margin:0;padding:18px;}"
                + "h1{font-size:26px;color:#ffffff;margin:8px 0 8px 0;}"
                + "h2{font-size:20px;color:#111827;margin:0 0 14px 0;}"
                + "h3{font-size:17px;color:#111827;margin:0 0 8px 0;}"
                + ".muted{color:#6b7280;}"
                + ".resumeBox{font-family:Consolas,monospace;background:#f8fafc;border:1px solid #d9e5e8;padding:16px;line-height:1.55;color:#1f2937;}"
                + "ul{margin-top:6px;}"
                + "li{margin-bottom:6px;}"
                + "</style></head><body>";
    }

    private String htmlEnd() {
        return "</body></html>";
    }

    private String htmlHero(String tag, String title, String subtitle) {
        return ""
                + "<table width='100%' cellpadding='0' cellspacing='0' bgcolor='#005249'>"
                + "<tr><td style='padding:22px;'>"
                + "<table cellpadding='6' cellspacing='0' bgcolor='#f5b800'><tr><td><b>" + escapeHtml(tag) + "</b></td></tr></table>"
                + "<h1>" + escapeHtml(title) + "</h1>"
                + "<p style='color:#e8f5f3;font-size:14px;'>" + escapeHtml(subtitle) + "</p>"
                + "</td></tr></table><br>";
    }

    private String htmlCard(String title, String body) {
        return ""
                + "<table width='100%' cellpadding='0' cellspacing='0' style='border:1px solid #d9e5e8;'>"
                + "<tr><td bgcolor='#ffffff' style='padding:18px;'>"
                + "<h2>" + escapeHtml(title) + "</h2>"
                + body
                + "</td></tr></table><br>";
    }

    private String htmlSmallJobCard(String title, String body) {
        return ""
                + "<table width='100%' cellpadding='0' cellspacing='0' style='border:1px solid #d9e5e8;'>"
                + "<tr><td bgcolor='#f8fafc' style='padding:14px;'>"
                + "<h3>" + escapeHtml(title) + "</h3>"
                + body
                + "</td></tr></table><br>";
    }

    private String metricCell(String label, String value) {
        return ""
                + "<td width='25%'>"
                + "<table width='100%' cellpadding='0' cellspacing='0' style='border:1px solid #d9e5e8;'>"
                + "<tr><td bgcolor='#ffffff' style='padding:14px;border-left:6px solid #00695c;'>"
                + "<font color='#6b7280'><b>" + escapeHtml(label) + "</b></font><br>"
                + "<font size='5' color='#111827'><b>" + escapeHtml(value) + "</b></font>"
                + "</td></tr></table>"
                + "</td>";
    }

    private String row(String label, String value) {
        return ""
                + "<table width='100%' cellpadding='8' cellspacing='0'>"
                + "<tr>"
                + "<td width='24%'><b>" + escapeHtml(label) + "</b></td>"
                + "<td width='2%'><b>:</b></td>"
                + "<td width='74%'>" + escapeHtml(value) + "</td>"
                + "</tr>"
                + "</table>";
    }

    private String buildSkillListHtml(List<String> skills) {
        if (skills == null || skills.isEmpty()) {
            return "<p class='muted'>No major skills detected. Add a dedicated skills section.</p>";
        }

        StringBuilder html = new StringBuilder();
        html.append("<table width='100%' cellpadding='8' cellspacing='0'>");

        for (int i = 0; i < skills.size(); i += 2) {
            html.append("<tr>");

            html.append("<td width='50%' bgcolor='#f8fafc'><b><font color='#005249'>•</font></b> ")
                    .append(escapeHtml(skills.get(i)))
                    .append("</td>");

            if (i + 1 < skills.size()) {
                html.append("<td width='50%' bgcolor='#f8fafc'><b><font color='#005249'>•</font></b> ")
                        .append(escapeHtml(skills.get(i + 1)))
                        .append("</td>");
            } else {
                html.append("<td width='50%' bgcolor='#f8fafc'></td>");
            }

            html.append("</tr>");
        }

        html.append("</table>");
        return html.toString();
    }

    private void resetApplication() {
        currentResume = null;
        currentReport = null;
        currentDomain = "Not detected";
        matchedResults = new ArrayList<>();

        if (resumeTextArea != null) {
            resumeTextArea.setText("");
        }

        if (resumePreviewPane != null) {
            resumePreviewPane.setText(buildEmptyResumePreviewHtml());
        }

        if (analysisPane != null) {
            analysisPane.setText(buildEmptyAnalysisHtml());
        }

        if (atsResumePane != null) {
            atsResumePane.setText(buildEmptyAtsResumeHtml());
        }

        if (reportPane != null) {
            reportPane.setText(buildEmptyReportHtml());
        }

        if (jobCardsPanel != null) {
            jobCardsPanel.removeAll();
            jobCardsPanel.revalidate();
            jobCardsPanel.repaint();
        }

        if (uploadStatusLabel != null) {
            uploadStatusLabel.setText("Status: Waiting for resume upload");
            uploadStatusLabel.setForeground(textMuted);
        }

        if (detectedDomainLabel != null) {
            detectedDomainLabel.setText("Detected Domain: Not detected");
            detectedDomainLabel.setForeground(textMuted);
        }

        dashboardScoreLabel.setText("0%");
        dashboardSkillsLabel.setText("0");
        dashboardJobsLabel.setText("0");
        dashboardRoleLabel.setText("Pending");

        if (atsDonutPanel != null) {
            atsDonutPanel.setScore(0);
        }

        if (scoreBarsPanel != null) {
            scoreBarsPanel.setReport(null);
        }

        if (keywordUsagePanel != null) {
            keywordUsagePanel.setReport(null);
        }

        if (hardSkillsPanel != null) {
            hardSkillsPanel.setSkills(new ArrayList<>());
        }

        if (softSkillsPanel != null) {
            softSkillsPanel.setSkills(new ArrayList<>());
        }
    }

    private String shorten(String text, int maxLength) {
        if (text == null) {
            return "Pending";
        }

        if (text.length() <= maxLength) {
            return text;
        }

        return text.substring(0, maxLength) + "...";
    }

    private String escapeHtml(String text) {
        if (text == null) {
            return "";
        }

        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private String joinSkillsForSentence(List<String> skills) {
        if (skills == null || skills.isEmpty()) {
            return "software development, problem solving, and technical learning";
        }

        int limit = Math.min(5, skills.size());
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < limit; i++) {
            builder.append(skills.get(i));

            if (i < limit - 1) {
                builder.append(", ");
            }
        }

        return builder.toString();
    }

    private List<String> getHardSkills(List<String> skills) {
        List<String> hardSkills = new ArrayList<>();

        for (String skill : skills) {
            String s = skill.toLowerCase();

            if (s.contains("java") || s.contains("python") || s.contains("sql") || s.contains("html")
                    || s.contains("css") || s.contains("javascript") || s.contains("git")
                    || s.contains("testing") || s.contains("spring") || s.contains("api")
                    || s.contains("database") || s.contains("machine learning") || s.contains("nlp")
                    || s.contains("oop") || s.contains("json") || s.contains("c++") || s.contains("c#")
                    || s.contains("data structures") || s.contains("algorithms")) {
                hardSkills.add(skill);
            }
        }

        return hardSkills;
    }

    private List<String> getSoftSkills(List<String> skills) {
        List<String> softSkills = new ArrayList<>();

        for (String skill : skills) {
            String s = skill.toLowerCase();

            if (s.contains("communication") || s.contains("teamwork")
                    || s.contains("problem solving") || s.contains("leadership")
                    || s.contains("documentation")) {
                softSkills.add(skill);
            }
        }

        return softSkills;
    }

    private class GradientPagePanel extends JPanel {

        public GradientPagePanel(int height) {
            setLayout(null);
            setPreferredSize(new Dimension(1300, height));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);

            GradientPaint gradient = new GradientPaint(0, 0, backgroundStart, getWidth(), getHeight(), backgroundEnd);
            g2.setPaint(gradient);
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.setColor(new Color(0, 105, 92, 18));
            g2.fillOval(-120, 120, 300, 300);

            g2.setColor(new Color(39, 39, 174, 18));
            g2.fillOval(getWidth() - 220, 260, 360, 360);

            super.paintComponent(g);
        }
    }

    private class HeroMockupPanel extends JPanel {

        public HeroMockupPanel() {
            setOpaque(false);
            setPreferredSize(new Dimension(620, 470));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color navy = new Color(30, 37, 82);
            Color purple = new Color(109, 64, 238);
            Color deepPurple = new Color(76, 29, 149);
            Color midPurple = new Color(139, 92, 246);
            Color bluePurple = new Color(79, 70, 229);
            Color lavender = new Color(245, 243, 255);
            Color paleLavender = new Color(250, 248, 255);
            Color borderPurple = new Color(221, 214, 254);
            Color softBorder = new Color(226, 232, 240);
            Color muted = new Color(100, 116, 139);
            Color gold = new Color(234, 179, 8);

            /*
             * Background block.
             */
            GradientPaint bg = new GradientPaint(
                    210, 0, new Color(58, 52, 180),
                    620, 470, new Color(67, 97, 238)
            );
            g2.setPaint(bg);
            g2.fillRect(190, 0, 430, 470);

            g2.setColor(new Color(255, 255, 255, 28));
            g2.fillOval(430, 25, 220, 220);

            g2.setColor(new Color(167, 139, 250, 52));
            g2.fillOval(380, 315, 190, 190);

            /*
             * Main white card.
             */
            int cardX = 35;
            int cardY = 35;
            int cardW = 545;
            int cardH = 330;

            g2.setColor(new Color(15, 23, 42, 22));
            g2.fillRoundRect(cardX + 12, cardY + 14, cardW, cardH, 28, 28);

            g2.setColor(Color.WHITE);
            g2.fillRoundRect(cardX, cardY, cardW, cardH, 28, 28);

            g2.setPaint(new GradientPaint(
                    cardX, cardY, new Color(237, 233, 254),
                    cardX + cardW, cardY, new Color(224, 231, 255)
            ));
            g2.fillRoundRect(cardX, cardY, cardW, 16, 28, 28);

            g2.setColor(Color.WHITE);
            g2.fillRect(cardX, cardY + 14, cardW, 15);

            /*
             * Header icon.
             */
            g2.setPaint(new GradientPaint(cardX + 32, cardY + 42, purple, cardX + 68, cardY + 78, deepPurple));
            g2.fillRoundRect(cardX + 32, cardY + 42, 38, 38, 11, 11);

            g2.setColor(Color.WHITE);
            g2.fillOval(cardX + 47, cardY + 57, 10, 10);

            /*
             * Title and description.
             * Kept within left column so it cannot touch the score card.
             */
            int textX = cardX + 95;
            int textY = cardY + 64;

            g2.setColor(navy);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 24));
            g2.drawString("Resume → Jobs → ATS", textX, textY);

            g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
            g2.drawString("Job-Specific Resume Scoring", textX, textY + 38);

            g2.setColor(muted);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            g2.drawString("Compare resume keywords with job descriptions", textX, textY + 65);
            g2.drawString("and generate targeted ATS improvements.", textX, textY + 86);

            /*
             * Main score card.
             */
            drawMainScoreCard(
                    g2,
                    cardX + 405,
                    cardY + 52,
                    115,
                    112,
                    navy,
                    purple,
                    lavender,
                    borderPurple,
                    muted
            );

            /*
             * Clean bottom section.
             */
            drawMetricsPanel(
                    g2,
                    cardX + 35,
                    cardY + 198,
                    295,
                    110,
                    navy,
                    purple,
                    bluePurple,
                    midPurple,
                    muted,
                    softBorder
            );

            drawInfoCard(g2, cardX + 360, cardY + 198, 155, 32, "ROLE", "Frontend Intern", lavender, purple, navy);
            drawInfoCard(g2, cardX + 360, cardY + 238, 155, 32, "SKILLS", "7 / 10 matched", new Color(239, 246, 255), bluePurple, navy);
            drawInfoCard(g2, cardX + 360, cardY + 278, 155, 32, "MISSING", "React, API, Git", new Color(255, 251, 235), gold, navy);

            /*
             * Candidate review card.
             */
            int reviewX = 95;
            int reviewY = 388;
            int reviewW = 405;
            int reviewH = 70;

            g2.setColor(new Color(15, 23, 42, 20));
            g2.fillRoundRect(reviewX + 10, reviewY + 10, reviewW, reviewH, 22, 22);

            g2.setColor(new Color(250, 250, 255));
            g2.fillRoundRect(reviewX, reviewY, reviewW, reviewH, 22, 22);

            g2.setColor(softBorder);
            g2.setStroke(new BasicStroke(1.3f));
            g2.drawRoundRect(reviewX, reviewY, reviewW, reviewH, 22, 22);

            g2.setPaint(new GradientPaint(reviewX + 25, reviewY + 18, purple, reviewX + 62, reviewY + 55, deepPurple));
            g2.fillOval(reviewX + 25, reviewY + 17, 40, 40);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
            g2.drawString("FA", reviewX + 38, reviewY + 42);

            g2.setColor(navy);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
            g2.drawString("Candidate Experience", reviewX + 84, reviewY + 27);

            g2.setColor(muted);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            g2.drawString("ATS insights highlighted missing keywords", reviewX + 84, reviewY + 49);
            g2.drawString("and improved the resume for stronger matches.", reviewX + 84, reviewY + 65);
        }

        private void drawMainScoreCard(Graphics2D g2, int x, int y, int w, int h,
                                       Color navy, Color purple, Color bg, Color border, Color muted) {
            g2.setColor(new Color(15, 23, 42, 12));
            g2.fillRoundRect(x + 4, y + 6, w, h, 18, 18);

            g2.setColor(bg);
            g2.fillRoundRect(x, y, w, h, 18, 18);

            g2.setColor(border);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(x, y, w, h, 18, 18);

            g2.setColor(purple);
            g2.fillRoundRect(x + 16, y + 16, 48, 24, 9, 9);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
            g2.drawString("TOP", x + 29, y + 32);

            g2.setColor(navy);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 30));
            g2.drawString("82%", x + 16, y + 75);

            g2.setColor(muted);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            g2.drawString("Job ATS Match", x + 16, y + 96);
        }

        private void drawMetricsPanel(Graphics2D g2, int x, int y, int w, int h,
                                      Color navy, Color purple, Color bluePurple, Color midPurple,
                                      Color muted, Color border) {
            g2.setColor(new Color(15, 23, 42, 10));
            g2.fillRoundRect(x + 4, y + 5, w, h, 18, 18);

            g2.setColor(new Color(253, 252, 255));
            g2.fillRoundRect(x, y, w, h, 18, 18);

            g2.setColor(border);
            g2.setStroke(new BasicStroke(1.4f));
            g2.drawRoundRect(x, y, w, h, 18, 18);

            g2.setColor(navy);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
            g2.drawString("ATS Match Breakdown", x + 16, y + 25);

            drawMetricLine(g2, x + 16, y + 48, 130, "Keywords", 78, purple, muted);
            drawMetricLine(g2, x + 16, y + 72, 130, "Skills", 72, bluePurple, muted);
            drawMetricLine(g2, x + 16, y + 96, 130, "Structure", 66, midPurple, muted);
        }

        private void drawMetricLine(Graphics2D g2, int x, int y, int barW,
                                    String label, int value, Color fill, Color muted) {
            g2.setColor(new Color(51, 65, 85));
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            g2.drawString(label, x, y - 3);

            int barX = x + 82;
            int barY = y - 11;

            g2.setColor(new Color(229, 231, 235));
            g2.fillRoundRect(barX, barY, barW, 8, 8, 8);

            int fillW = (int) (barW * (value / 100.0));
            g2.setColor(fill);
            g2.fillRoundRect(barX, barY, fillW, 8, 8, 8);

            g2.setColor(muted);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
            g2.drawString(value + "%", barX + barW + 8, y - 3);
        }

        private void drawInfoCard(Graphics2D g2, int x, int y, int w, int h,
                                  String label, String value, Color bg, Color accent, Color text) {
            g2.setColor(bg);
            g2.fillRoundRect(x, y, w, h, 13, 13);

            g2.setColor(new Color(218, 224, 235));
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(x, y, w, h, 13, 13);

            g2.setColor(accent);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 9));
            g2.drawString(label, x + 12, y + 12);

            g2.setColor(text);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
            g2.drawString(value, x + 12, y + 26);
        }
    }
    private class WorkflowSmartArtPanel extends JPanel {

        public WorkflowSmartArtPanel() {
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(border));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(textDark);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 24));
            g2.drawString("Smart Resume-to-Job Workflow", 30, 42);

            g2.setColor(textMuted);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            g2.drawString("A professional flow that turns resume data into ATS insights and job recommendations.", 30, 68);

            int baseY = 152;

            drawStage(g2, 60, baseY, "01", "Analyze Resume", "Extract skills, role, domain", tealDark, tealVeryLight);
            drawConnectorArrow(g2, 225, baseY, 285, baseY);

            drawStage(g2, 295, baseY, "02", "Find Jobs", "Match jobs by domain", mustardDark, new Color(255, 247, 220));
            drawConnectorArrow(g2, 460, baseY, 520, baseY);

            drawStage(g2, 530, baseY, "03", "ATS Score", "Compare per job", cobalt, new Color(235, 238, 255));
            drawConnectorArrow(g2, 695, baseY, 755, baseY);

            drawStage(g2, 765, baseY, "04", "Optimize Resume", "Create ATS-friendly draft", tealDark, tealVeryLight);
            drawConnectorArrow(g2, 930, baseY, 990, baseY);

            drawStage(g2, 1000, baseY, "05", "Export Report", "Final career summary", mustardDark, new Color(255, 247, 220));
        }

        private void drawStage(Graphics2D g2, int x, int y, String number, String title, String subtitle, Color accent, Color fill) {
            int cardW = 165;
            int cardH = 112;

            g2.setColor(new Color(0, 0, 0, 18));
            g2.fillRoundRect(x + 4, y - 50 + 5, cardW, cardH, 26, 26);

            g2.setColor(fill);
            g2.fillRoundRect(x, y - 50, cardW, cardH, 26, 26);

            g2.setColor(accent);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(x, y - 50, cardW, cardH, 26, 26);

            g2.setColor(accent);
            g2.fillOval(x + 18, y - 34, 44, 44);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
            g2.drawString(number, x + 31, y - 7);

            g2.setColor(textDark);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
            g2.drawString(title, x + 18, y + 30);

            g2.setColor(textMuted);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            g2.drawString(subtitle, x + 18, y + 52);

            g2.setColor(accent);
            g2.fillRoundRect(x + 18, y + 70, 60, 5, 8, 8);
        }

        private void drawConnectorArrow(Graphics2D g2, int startX, int startY, int endX, int endY) {
            g2.setColor(new Color(148, 163, 184));
            g2.setStroke(new BasicStroke(2.2f));

            int controlX = (startX + endX) / 2;
            int controlY = startY + 55;

            java.awt.geom.QuadCurve2D curve = new java.awt.geom.QuadCurve2D.Float();
            curve.setCurve(startX, startY, controlX, controlY, endX, endY);
            g2.draw(curve);

            double angle = Math.atan2(endY - controlY, endX - controlX);
            int arrowSize = 10;

            int x1 = (int) (endX - arrowSize * Math.cos(angle - Math.PI / 6));
            int y1 = (int) (endY - arrowSize * Math.sin(angle - Math.PI / 6));

            int x2 = (int) (endX - arrowSize * Math.cos(angle + Math.PI / 6));
            int y2 = (int) (endY - arrowSize * Math.sin(angle + Math.PI / 6));

            g2.drawLine(endX, endY, x1, y1);
            g2.drawLine(endX, endY, x2, y2);
        }
    }
    
    private class ScoreDonutPanel extends JPanel {

        private int score;

        public ScoreDonutPanel(int score) {
            this.score = score;
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(border));
        }

        public void setScore(int score) {
            this.score = Math.max(0, Math.min(100, score));
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);

            g2.setColor(textDark);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 22));
            g2.drawString("Top ATS Score", 28, 42);

            int x = 85;
            int y = 72;
            int size = 145;

            g2.setStroke(new BasicStroke(16, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(new Color(235, 236, 240));
            g2.drawArc(x, y, size, size, 0, 360);

            g2.setColor(teal);
            g2.drawArc(x, y, size, size, 90, -(int) (360 * score / 100.0));

            g2.setColor(textDark);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 36));
            g2.drawString(score + "%", x + 38, y + 88);

            g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            g2.setColor(textMuted);
            g2.drawString("Best job match", x + 31, y + 112);

            g2.setColor(score >= 75 ? tealDark : score >= 55 ? mustardDark : new Color(185, 28, 28));
            g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
            g2.drawString(getScoreLabel(score), 95, 260);
        }

        private String getScoreLabel(int score) {
            if (score >= 80) {
                return "Strong apply";
            }

            if (score >= 60) {
                return "Good match";
            }

            if (score >= 40) {
                return "Needs tailoring";
            }

            return "Low match";
        }
    }

    private class ScoreBarsPanel extends JPanel {

        private AtsReport report;

        public ScoreBarsPanel(AtsReport report) {
            this.report = report;
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(border));
        }

        public void setReport(AtsReport report) {
            this.report = report;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);

            g2.setColor(textDark);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 22));
            g2.drawString("Resume Health", 28, 42);

            double structure = report == null ? 0 : report.getFormattingScore();
            double skills = report == null ? 0 : report.getSkillsScore();
            double keywords = report == null ? 0 : report.getKeywordScore();
            double education = report == null ? 0 : report.getEducationScore();
            double projects = report == null ? 0 : report.getProjectScore();

            drawBar(g2, "Resume Structure", structure, 28, 82, teal);
            drawBar(g2, "Skills Coverage", skills, 28, 122, cobaltLight);
            drawBar(g2, "Keyword Usage", keywords, 28, 162, mustard);
            drawBar(g2, "Education", education, 28, 202, tealSoft);
            drawBar(g2, "Projects", projects, 28, 242, tealDark);
        }

        private void drawBar(Graphics2D g2, String label, double value, int x, int y, Color color) {
            g2.setColor(textDark);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
            g2.drawString(label, x, y);

            g2.setColor(lightGray);
            g2.fillRoundRect(x + 165, y - 12, 190, 10, 8, 8);

            g2.setColor(color);
            int fill = (int) (190 * Math.min(100, value) / 100.0);
            g2.fillRoundRect(x + 165, y - 12, fill, 10, 8, 8);

            g2.setColor(textMuted);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            g2.drawString(String.format("%.0f%%", value), x + 365, y);
        }
    }

    private class KeywordUsagePanel extends JPanel {

        private AtsReport report;

        public KeywordUsagePanel(AtsReport report) {
            this.report = report;
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(border));
        }

        public void setReport(AtsReport report) {
            this.report = report;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);

            g2.setColor(textDark);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 22));
            g2.drawString("Resume Insights", 28, 42);

            double keyword = report == null ? 0 : report.getKeywordScore();
            double contact = report == null ? 0 : report.getContactScore();
            double format = report == null ? 0 : report.getFormattingScore();

            drawInsight(g2, "Keyword Usage", keyword, 80, mustard);
            drawInsight(g2, "Contact Details", contact, 130, teal);
            drawInsight(g2, "Readable Format", format, 180, cobaltLight);

            g2.setColor(textMuted);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            g2.drawString("General resume health is separate from", 28, 242);
            g2.drawString("job-specific ATS scores.", 28, 262);
        }

        private void drawInsight(Graphics2D g2, String label, double value, int y, Color color) {
            g2.setColor(textDark);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
            g2.drawString(label, 28, y);

            g2.setColor(lightGray);
            g2.fillRoundRect(185, y - 14, 145, 12, 8, 8);

            g2.setColor(color);
            g2.fillRoundRect(185, y - 14, (int) (145 * value / 100.0), 12, 8, 8);

            g2.setColor(textMuted);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            g2.drawString(String.format("%.0f%%", value), 345, y);
        }
    }

    private class SkillsPanel extends JPanel {

        private final String title;
        private List<String> skills;

        public SkillsPanel(String title, List<String> skills) {
            this.title = title;
            this.skills = skills;
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(border));
        }

        public void setSkills(List<String> skills) {
            this.skills = skills;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);

            g2.setColor(textDark);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 22));
            g2.drawString(title, 28, 42);

            if (skills == null || skills.isEmpty()) {
                g2.setColor(textMuted);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 15));
                g2.drawString("No skills detected yet.", 28, 82);
                g2.drawString("Upload and analyze a resume.", 28, 106);
                return;
            }

            int x = 28;
            int y = 68;

            for (String skill : skills) {
                int width = Math.max(80, skill.length() * 9 + 30);

                if (x + width > getWidth() - 30) {
                    x = 28;
                    y += 38;
                }

                g2.setColor(title.toLowerCase().contains("hard") ? tealVeryLight : new Color(255, 247, 220));
                g2.fillRoundRect(x, y, width, 28, 16, 16);

                g2.setColor(title.toLowerCase().contains("hard") ? tealDark : mustardDark);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
                g2.drawString(skill, x + 15, y + 19);

                x += width + 10;
            }
        }
    }
}

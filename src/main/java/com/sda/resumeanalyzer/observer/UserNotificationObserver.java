package com.sda.resumeanalyzer.observer;

import javax.swing.JOptionPane;

import com.sda.resumeanalyzer.model.MatchResult;

public class UserNotificationObserver implements JobObserver {

    @Override
    public void update(MatchResult result) {
        JOptionPane.showMessageDialog(null,
                "High Match Job Found!\n\n" +
                        "Job: " + result.getJob().getTitle() + "\n" +
                        "Platform: " + result.getJob().getPlatform() + "\n" +
                        "Match: " + String.format("%.2f", result.getMatchPercentage()) + "%\n" +
                        "Verdict: " + result.getVerdict(),
                "Job Match Alert",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
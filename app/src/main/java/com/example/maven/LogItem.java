package com.example.maven;

public class LogItem {
    private String subject;
    private String activity;
    private int scorePercentage;
    private int activityPercentage;

    public LogItem(String subject, String activity, int scorePercentage, int activityPercentage) {
        this.subject = subject;
        this.activity = activity;
        this.scorePercentage = scorePercentage;
        this.activityPercentage = activityPercentage;
    }



    public String getSubject() {
        return subject;
    }

    public String getActivity() {
        return activity;
    }

    public int getScorePercentage() {
        return scorePercentage;
    }

    public int getActivityPercentage() {
        return activityPercentage;
    }
}


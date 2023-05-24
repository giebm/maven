package com.example.maven;

public class Log {
    private String subject;
    private String activity;
    private int score;
    private int activityPercentage;

    public Log(String subject, String activity, int score, int activityPercentage) {
        this.subject = subject;
        this.activity = activity;
        this.score = score;
        this.activityPercentage = activityPercentage;
    }

    public String getSubject() {
        return subject;
    }

    public String getActivity() {
        return activity;
    }

    public int getScore() {
        return score;
    }

    public int getActivityPercentage() {
        return activityPercentage;
    }
}

package com.example.maven;

public class Reminder {
    private String text;
    private String dateTime;

    public Reminder(String text, String dateTime) {
        this.text = text;
        this.dateTime = dateTime;
    }

    public String getText() {
        return text;
    }

    public String getDateTime() {
        return dateTime;
    }
}

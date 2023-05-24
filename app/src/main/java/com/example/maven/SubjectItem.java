package com.example.maven;

import java.util.ArrayList;
import java.util.List;

public class SubjectItem {
    private String subject;
    private List<LogItem> logList;

    public SubjectItem(String subject) {
        this.subject = subject;
        logList = new ArrayList<>();
    }

    public String getSubject() {
        return subject;
    }

    public List<LogItem> getLogList() {
        return logList;
    }

    public void addLog(LogItem log) {
        logList.add(log);
    }
}


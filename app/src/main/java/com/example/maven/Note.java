package com.example.maven;

public class Note {
    private String id;
    private String subject;
    private String content;
    private String photoUrl;

    public Note() {
        // Default constructor required for Firestore
    }

    public Note(String subject, String content, String photoUrl) {
        this.subject = subject;
        this.content = content;
        this.photoUrl = photoUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    // Override toString() method if you want to display the note's information in the ArrayAdapter or elsewhere
    @Override
    public String toString() {
        return subject;
    }
}

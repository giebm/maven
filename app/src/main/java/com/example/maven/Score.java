package com.example.maven;

public class Score {
    private int countCorrect;
    private int countWrong;
    private double average;

    public Score() {
        // Default constructor required for Firestore
    }

    public Score(int countCorrect, int countWrong, double average) {
        this.countCorrect = countCorrect;
        this.countWrong = countWrong;
        this.average = average;
    }

    public int getCountCorrect() {
        return countCorrect;
    }

    public int getCountWrong() {
        return countWrong;
    }

    public double getAverage() {
        return average;
    }
}

package com.searchengine.model;

public class SearchPhrase {

    private String fileName;
    private double score;
    private String fragment;
    private int queryCounter;
    private String lastModified;

    public SearchPhrase() {
    }

    public SearchPhrase(String fileName, double score, String fragment, int queryCounter, String lastModified) {
        this.fileName = fileName;
        this.score = score;
        this.fragment = fragment;
        this.queryCounter = queryCounter;
        this.lastModified = lastModified;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getFragment() {
        return fragment;
    }

    public void setFragment(String fragment) {
        this.fragment = fragment;
    }

    public int getQueryCounter() {
        return queryCounter;
    }

    public void setQueryCounter(int queryCounter) {
        this.queryCounter = queryCounter;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public String toString() {
        return "SearchPhrase{" +
                "fileName='" + fileName + '\'' +
                ", score=" + score +
                ", fragment='" + fragment + '\'' +
                ", queryCounter=" + queryCounter +
                ", lastModified='" + lastModified + '\'' +
                '}';
    }
}

package com.company.collabcode.model;

public class Notification {
    private String text;
    private String url;

    public Notification() {
    }

    public Notification(String text, String url) {
        this.text = text;
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

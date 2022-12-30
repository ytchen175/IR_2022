package com.example.webdemo.DTO;

public class DocumentDTO {
    private String id;
    private int score;
    private String title;
    private String h1;
    private String body;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getH1() {
        return h1;
    }

    public void setH1(String h1) {
        this.h1 = h1;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}

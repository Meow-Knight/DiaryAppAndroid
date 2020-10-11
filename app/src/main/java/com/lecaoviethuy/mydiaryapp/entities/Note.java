package com.lecaoviethuy.mydiaryapp.entities;

import java.util.Date;

public class Note {
    private int id;
    private long timestamp;
    private String title;
    private String content;
    private int color;

    public Note(){}

    public Note(int id, long timestamp, String title, String content, int color) {
        this.id = id;
        this.timestamp = timestamp;
        this.title = title;
        this.content = content;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Date getDate(){
        return new Date(timestamp);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", color=" + color +
                '}';
    }
}

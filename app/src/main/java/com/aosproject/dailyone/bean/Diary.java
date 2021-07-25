package com.aosproject.dailyone.bean;

import java.sql.Date;

public class Diary {

    private int id;
    private String content;
    private int emoji;
    private String date;

    public Diary(int id, String content, int emoji, String date) {
        this.id = id;
        this.content = content;
        this.emoji = emoji;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getEmoji() {
        return emoji;
    }

    public void setEmoji(int emoji) {
        this.emoji = emoji;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

package org.aerogear.gsoc.kafkapoc.model;

import java.io.Serializable;

public class Tweet implements Serializable {
    private String id;
    private String text;
    private String language;

    public Tweet(String id, String text, String language) {
        this.id = id;
        this.text = text;
        this.language = language;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }


    @Override
    public String toString() {
        return "Tweet{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}
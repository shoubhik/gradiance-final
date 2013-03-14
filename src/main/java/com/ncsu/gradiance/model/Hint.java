package com.ncsu.gradiance.model;

/**
 * User: shoubhik Date: 4/3/13 Time: 12:12 PM
 */
public class Hint {
    int id;
    String text;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isValid(){
        return text != null && !text.trim().equals("");
    }

}

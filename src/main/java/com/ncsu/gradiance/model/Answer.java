package com.ncsu.gradiance.model;

public class Answer {
    private String text;
    private int id;
    private int correct;
    private Hint hint;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public Hint getHint() {
        return hint;
    }

    public void setHint(Hint hint) {
        this.hint = hint;
    }

    public boolean isCorrect(){
        return correct == 1;
    }

    public boolean isValid(){
        return text != null && !text.trim().equals("") && hint.isValid();
    }

    public boolean equals(Object o){
        if(o == null || !(o instanceof Answer))
            return false;
        Answer that = (Answer)o;
        return this.id == that.id;
    }


}

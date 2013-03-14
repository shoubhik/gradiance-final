package com.ncsu.gradiance.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class Homework {

    private Course course;
    private Timestamp startDate;
    private Timestamp endDate;
    private int numAttempts;
    private ScoreSelectionScheme scoreSelectionScheme;
    private int correctPts;
    private int incorrectPts;
    private int numQuestions;
    private String name;
    private int id;
    private List<Question> questions;
    private Attempt currentAttempt;

    public int getAggregateScore() {
        return aggregateScore;
    }

    public void setAggregateScore(int aggregateScore) {
        this.aggregateScore = aggregateScore;
    }

    private int aggregateScore;

    public List<Attempt> getAllAttempts() {
        return allAttempts;
    }

    public void setAllAttempts(List<Attempt> allAttempts) {
        this.allAttempts = allAttempts;
    }

    public Attempt getCurrentAttempt() {
        return currentAttempt;
    }

    public void setCurrentAttempt(Attempt currentAttempt) {
        this.currentAttempt = currentAttempt;
    }

    List<Attempt> allAttempts;


    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumQuestions() {
        return numQuestions;
    }

    public void setNumQuestions(int numQuestions) {
        this.numQuestions = numQuestions;
    }

    public int getIncorrectPts() {
        return incorrectPts;
    }

    public void setIncorrectPts(int incorrectPts) {
        this.incorrectPts = incorrectPts;
    }

    public int getCorrectPts() {
        return correctPts;
    }

    public void setCorrectPts(int correctPts) {
        this.correctPts = correctPts;
    }

    public ScoreSelectionScheme getScoreSelectionScheme() {
        return scoreSelectionScheme;
    }

    public void setScoreSelectionScheme(
            ScoreSelectionScheme scoreSelectionScheme) {
        this.scoreSelectionScheme = scoreSelectionScheme;
    }

    public int getNumAttempts() {
        return numAttempts;
    }

    public void setNumAttempts(int numAttempts) {
        this.numAttempts = numAttempts;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString(){
        return this.name;
    }

    public String getStartDateString(){
        return new SimpleDateFormat("yyyy-MM-dd:kk:mm:ss").format(
                new Date(startDate.getTime()));
    }

    public String getEndDAteString(){
        return new SimpleDateFormat("yyyy-MM-dd:kk:mm:ss").format(
                new Date(endDate.getTime()));
    }

    public Attempt setAttemptById(int attemptId){
        if(getAllAttempts() == null)
            throw new IllegalStateException();
        for(Attempt attempt : getAllAttempts())
            if(attempt.getId() == attemptId){
                setCurrentAttempt(attempt);
                return attempt;
            }
        return null;
    }


}

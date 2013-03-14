package com.ncsu.gradiance.model;

import com.ncsu.gradiance.dao.HomeworkDao;

import java.sql.Timestamp;
import java.util.List;

public class Attempt {
    private User user;
    private int attemptNum;
    private java.sql.Timestamp time;
    private List<Question> questions;
    private int score;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    public int getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(int homeworkId) {
        this.homeworkId = homeworkId;
    }

    private int homeworkId;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }



    public int getAttemptNum() {
        return attemptNum;
    }

    public void setAttemptNum(int attemptNum) {
        this.attemptNum = attemptNum;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void add(HomeworkDao homeworkDao){
        homeworkDao.insertHomeworkAttempt(this);
    }

    public void submit(HomeworkDao homeworkDao){
        homeworkDao.submitAttempt(this);

    }


}

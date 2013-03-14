package com.ncsu.gradiance.model;

import javax.validation.constraints.NotNull;
import java.util.List;

public class Topic {
    @NotNull
    private String courseId;
    @NotNull
    private String topicId;
    @NotNull
    private String topicName;

    private List<Question> questions;

    private Question question;


    public Topic() {
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String toString(){
        return this.topicName;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public void setSelectedQuestionById(int qid) {
        if(getQuestions() == null)
            throw new IllegalStateException();
        for(Question question1: getQuestions())
            if(question1.getId() == qid){
                setQuestion(question1);
                break;
            }
    }
}

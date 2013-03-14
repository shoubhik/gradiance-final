package com.ncsu.gradiance.model;


import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static com.ncsu.gradiance.dao.Constants.*;

/**
 * User: shoubhik Date: 3/3/13 Time: 2:47 PM
 */
public class Course {
    @NotNull
    private String courseId;
    @NotNull
    private String courseName;
    @NotNull
    private Date fromDate;
    @NotNull
    private Date toDate;

    private String tokenId;

    private String profId;

    private Topic selectedTopic;

    private Homework homework;

    private List<Homework> homeworks;

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    List<Question> questions;

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    List<Topic> topics;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String toString(){
        return this.courseId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getProfId() {
        return profId;
    }

    public void setProfId(String profId) {
        this.profId = profId;
    }

    public boolean isCourseExpired(){
        return getTodaysSqlDate().after(toDate);
    }

    public Topic getSelectedTopic() {
        return selectedTopic;
    }

    public void setSelectedTopic(Topic selectedTopic) {
        this.selectedTopic = selectedTopic;
    }

    public void setTopicSelectedByUser(String topicName){
        if(getTopics() == null)
            throw new IllegalStateException();
        for(Topic topic : getTopics())
            if(topic.getTopicName().equals(topicName)){
                setSelectedTopic(topic);
                break;
            }
    }

    public List<Question> getQuestionsSelectedForHomework(int qIds[]){
        if(getQuestions() == null)
            throw new IllegalStateException();
        List<Question> selectedQuestions1 = new ArrayList<>();
        for(Question question : getQuestions())
            for(int qid : qIds)
                if(question.getId() == qid)
                    selectedQuestions1.add(question);
        return selectedQuestions1;

    }

    public Homework getHomework() {
        return homework;
    }

    public void setHomework(Homework homework) {
        this.homework = homework;
    }

    public List<Homework> getHomeworks() {
        return homeworks;
    }

    public void setHomeworks(List<Homework> homeworks) {
        this.homeworks = homeworks;
    }

    public Homework getSelectedHomework(int id){
        if(getHomeworks() == null)
            throw new IllegalStateException();
        for(Homework homework : getHomeworks())
            if(homework.getId() == id){
                setHomework(homework);
                return homework;
            }
        throw new IllegalArgumentException();
    }
}

package com.ncsu.gradiance.dao;

import com.ncsu.gradiance.model.Attempt;
import com.ncsu.gradiance.model.Homework;
import com.ncsu.gradiance.model.Question;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class Scorer {
    private Attempt attempt;
    private HttpServletRequest request;
    private Homework homework;
    public Scorer(Attempt attempt, HttpServletRequest request, Homework homework){
        this.attempt = attempt;
        this.request= request;
        this.homework = homework;
    }

    public void calculate(){
        int correctPts = homework.getCorrectPts();
        int incorrectPts = -1 * homework.getIncorrectPts();
        int totalScore = 0;
        for(Question question : attempt.getQuestions()){
            String ansId = request.getParameter(question.getId()+"");
            if(StringUtils.isBlank(ansId))
                totalScore += incorrectPts;
            else if(question.isResponseCorrect(Integer.parseInt(ansId)))
                totalScore += correctPts;
            else totalScore += incorrectPts;
            if(!StringUtils.isBlank(ansId)){
                question.setResponseByUser(Integer.parseInt(ansId));
            }
            String explain = request.getParameter(question.getId()+"-Exp");
            if(!StringUtils.isBlank(explain))
                question.setExplaination(explain);

        }
        attempt.setScore(totalScore);
    }


}

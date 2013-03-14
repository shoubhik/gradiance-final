package com.ncsu.gradiance.controller;

import com.ncsu.gradiance.model.Answer;
import com.ncsu.gradiance.model.Hint;
import com.ncsu.gradiance.model.Question;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * User: shoubhik Date: 12/3/13 Time: 7:57 PM
 */
public class ValidateQuestion {
    private HttpServletRequest request;
    private Question question;
    private Errors errors;
    public  ValidateQuestion(HttpServletRequest request, Question question,
                             Errors errors){
        this.request = request;
        this.question= question;
        this.errors = errors;
    }

    public Question getQuestion(){
        return this.question;
    }

    public boolean isValidAddAnswer(){
        try {
            List<Answer> answers = extractAnswersAndHints(request, errors);
            if(errors.hasErrors()){
                return false;
            }
            else question.setAnswers(answers);
        } catch (Exception e) {
            errors.rejectValue("", "", e.getMessage());
            return false;
        }
        return true;
    }

    public boolean isValidNewQuestion(){
        try {
            String qText = request.getParameter("question.text");
            if (StringUtils.isBlank(qText)) {
                errors.rejectValue("", "", "question cannot be blank");
                return false;
            }
            question.setText(qText);
            int diffLevel = Integer.parseInt(request.getParameter("difficultyLevel"));
            if(!(diffLevel >= 1 && diffLevel <=5)){
                errors.rejectValue("", "", "difficulty level should between 1 and 5");
                return false;
            }
            question.setDifficultyLevel(diffLevel);
            int correctPts = Integer.parseInt(request.getParameter("pointCorrect"));
            question.setPointCorrect(correctPts);
            int incorrectPts = Integer.parseInt(request.getParameter("pointIncorrect"));
            question.setPointIncorrect(incorrectPts);
            String qHint = request.getParameter("question.hint");
            if(!StringUtils.isBlank(qHint)){
                Hint hint = new Hint();
                hint.setText(qHint);
                question.setHasHint(true);
                question.setHint(hint);
            }
            else question.setHasHint(false);
            List<Answer> answers = extractAnswersAndHints(request, errors);
            if(errors.hasErrors()){
                return false;
            }
            else question.setAnswers(answers);


        } catch (Exception e) {
            errors.rejectValue("", "", e.getMessage());
            return false;
        }
        return true;
    }

    private List<Answer> extractAnswersAndHints(HttpServletRequest request,
                                        Errors errors){
        List<Answer> answers = new ArrayList<>();
        for(int i = 1; i <= question.getNumCorrectAnswers(); i++){
            String ansText = request.getParameter("correct_ans"+i);
            String hintText = request.getParameter("correct_hint"+i);
            if(StringUtils.isEmpty(ansText) || StringUtils.isEmpty(hintText))
                errors.rejectValue("", "", "answers and hints cannot be left empty");
            answers.add(createAnswer(ansText, hintText, 1));
        }
        for(int i = 1; i <= question.getNumIncorrectAnswers(); i++){
            String ansText = request.getParameter("incorrect_ans"+i);
            String hintText = request.getParameter("incorrect_hint"+i);
            if(StringUtils.isEmpty(ansText) || StringUtils.isEmpty(hintText))
                errors.rejectValue("", "", "answers and hints cannot be left empty");
            answers.add(createAnswer(ansText, hintText, 0));
        }
        return answers;
    }

    private Answer createAnswer(String ansText, String hintText, int correct){
        Hint hint = new Hint();
        hint.setText(hintText.trim());
        Answer answer = new Answer();
        answer.setText(ansText.trim());
        answer.setCorrect(correct);
        answer.setHint(hint);
        return answer;
    }



}

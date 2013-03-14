package com.ncsu.gradiance.controller;

import com.ncsu.gradiance.dao.HomeworkDao;
import com.ncsu.gradiance.model.Course;
import com.ncsu.gradiance.model.Homework;
import com.ncsu.gradiance.model.Question;
import com.ncsu.gradiance.model.ScoreSelectionScheme;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: shoubhik Date: 13/3/13 Time: 11:50 AM
 */
public class ValidateHomework {

    private Homework homework;
    private HttpServletRequest request;
    private Errors errors;
    private HomeworkDao homeworkDao;
    private Course course;
    public ValidateHomework(Homework homework, HttpServletRequest request,
                            Errors errors, HomeworkDao homeworkDao, Course course)  {
        this.homework = homework;
        this.request = request;
        this.errors = errors;
        this.homeworkDao = homeworkDao;
        this.course = course;
    }

    public Homework getHomework(){
       return this.homework;
    }

    public void updateEditHomework(){
        try{
            Integer numAttempts = Integer.parseInt(
                    request.getParameter("homework.numAttempts"));
            if(numAttempts < 0){
                errors.rejectValue("", "", "attempts cannot be l;ess than 0");
                return;
            }

            String scheme = request.getParameter(
                    "courseSelected.homework.scoreSelectionScheme");
            String startStr = request.getParameter("homework.startDate");
            String endStr = request.getParameter("homework.endDate");
            if(isEmpty(new String[]{scheme,startStr, endStr})){
                errors.rejectValue("", "", "all field are mandatory");
                return;
            }
            ScoreSelectionScheme scoreSelectionScheme = this.homeworkDao.
                    getSchemeByName(scheme);
            Timestamp startDate = getTimeStamp( startStr);
            Timestamp endDate = getTimeStamp(endStr);
            int correctPts = Integer.parseInt(request.getParameter("homework.correctPts"));
            int incorrectPts = Integer.parseInt(request.getParameter("homework.incorrectPts"));
            int numQues = Integer.parseInt(
                    request.getParameter("homework.numQuestions"));
            String quesRemoved[] = request.getParameterValues("delete");
            if(quesRemoved != null && quesRemoved.length > 0){
                List<Integer> rmQues = new ArrayList<>();
                for(String str : quesRemoved)
                    rmQues.add(Integer.parseInt(str));
                for(ListIterator<Question> questionListIterator =
                            homework.getQuestions().listIterator(); questionListIterator.hasNext();){
                    int id = questionListIterator.next().getId();
                    if(rmQues.contains(id))
                        questionListIterator.remove();
                }
                // what bad code this is but i dont have time
                this.homeworkDao.deleteQuestionsFromHomework(homework.getId(),
                                                  rmQues);
            }
            String quesAdded[] = request.getParameterValues("add");
            int numQueAdded = 0;
            if(quesAdded != null && quesAdded.length > 0){
                numQueAdded= quesAdded.length;
                List<Integer> addQues = new ArrayList<>();
                for(String str : quesAdded)
                    addQues.add(Integer.parseInt(str));
                for(int addQueId: addQues)
                    this.homeworkDao.insertHomeworkQuestion(addQueId, homework.getId());

            }

            if(numQues > homework.getQuestions().size() + numQueAdded){
                errors.rejectValue("", "", "num of questions should be less than or equal to number of questions selected");
                return;
            }
            homework.setNumAttempts(numAttempts);
            homework.setStartDate(startDate);
            homework.setEndDate(endDate);
            homework.setScoreSelectionScheme(scoreSelectionScheme);
            homework.setCorrectPts(correctPts);
            homework.setIncorrectPts(incorrectPts);
            homework.setNumQuestions(numQues);
            this.homeworkDao.updateHomeworkForEdit(scoreSelectionScheme.getSchemeId(),
                 homework.getStartDate(), homework.getEndDate(), numAttempts,
                 correctPts, incorrectPts,numQues, homework.getId());

        }catch (Exception e){
            errors.rejectValue("", "", e.getMessage());
        }

    }

    public void validateNewHomeWork(){
        try{
            String name = request.getParameter("homework.name");
            Integer numAttempts = Integer.parseInt(
                    request.getParameter("homework.numAttempts"));
            if(numAttempts < 0){
                errors.rejectValue("", "", "attempts cannot be l;ess than 0");
                return;
            }

            String scheme = request.getParameter(
                    "courseSelected.homework.scoreSelectionScheme");
            String startStr = request.getParameter("homework.startDate");
            String endStr = request.getParameter("homework.endDate");
            if(isEmpty(new String[]{name,scheme,startStr, endStr})){
                errors.rejectValue("", "", "all field are mandatory");
                return;
            }
            ScoreSelectionScheme scoreSelectionScheme = this.homeworkDao.
                    getSchemeByName(scheme);
            Timestamp startDate = getTimeStamp( startStr);
            Timestamp endDate = getTimeStamp(endStr);
            int correctPts = Integer.parseInt(request.getParameter("homework.correctPts"));
            int incorrectPts = Integer.parseInt(request.getParameter("homework.incorrectPts"));
            int numQues = Integer.parseInt(request.getParameter("homework.numQuestions"));
            String questions[] = request.getParameterValues("question");
            if(questions == null || questions.length == 0) {
                errors.rejectValue("", "", "at least one question should be selected");
                return;
            }
            if(numQues > questions.length){
                errors.rejectValue("", "", "num of questions should be less than or equal to number of questions selected");
                return;
            }
            homework.setName(name);
            homework.setNumAttempts(numAttempts);
            homework.setStartDate(startDate);
            homework.setEndDate(endDate);
            homework.setScoreSelectionScheme(scoreSelectionScheme);
            homework.setCorrectPts(correctPts);
            homework.setIncorrectPts(incorrectPts);
            homework.setNumQuestions(numQues);
            homework.setQuestions(course.getQuestionsSelectedForHomework(
                    getQuestionIds(questions)));

        }catch (Exception e){
            errors.rejectValue("", "", e.getMessage());
        }
    }

    private Timestamp getTimeStamp(String str) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd:kk:mm:ss");
        return new Timestamp(df.parse(str).getTime());
    }



    private int[] getQuestionIds(String...ids){
        int qids[] = new int[ids.length];
        for(int i = 0; i<ids.length;i++)
            qids[i] = Integer.parseInt(ids[i]);
        return qids;
    }

    private boolean isEmpty(String...strs){
        for(String str : strs)
            if( StringUtils.isEmpty(str))
                return true;
        return false;
    }
}

package com.ncsu.gradiance.dao;

import com.ncsu.gradiance.model.*;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ChooseRandomQuestionAndAnswers {

    private Errors errors;
    private HomeworkDao homeworkDao;
    private User student;
    private Homework homework;
    private static int NUM_CORRECT = 1;
    private static int NUM_INCORRECT = 3;
    private static int TOTAL_ANSWERS= NUM_CORRECT + NUM_INCORRECT;

    public ChooseRandomQuestionAndAnswers(HomeworkDao homeworkDao, User student,
                                          Homework homework, Errors errors){
        this.homeworkDao = homeworkDao;
        this.errors = errors;
        this.student = student;
        this.homework = homework;
    }

    public Attempt choose() {
        Attempt attempt = null;
        if(this.homeworkDao.isHomeworkPending(student.getUserName(),
                                              homework.getId())){
            attempt = this.homeworkDao.getPendingAttempt(student.getUserName(),
                                                         homework.getId());
            attempt.setUser(student);
        } else {
            int randomSeed =
                    this.homeworkDao.getSeedForStudent(student.getUserName());
            attempt = new Attempt();
            attempt.setUser(student);
            attempt.setHomeworkId(homework.getId());
            attempt.setAttemptNum(
                    this.homeworkDao.getHomeworkAttemptNumber(student.
                            getUserName(), homework.getId()));
            attempt.setQuestions(getRandomQuestions(randomSeed));
            attempt.add(homeworkDao);
        }
        homework.setCurrentAttempt(attempt);
        return attempt;
    }

    private List<Question> getRandomQuestions(int seed){
        List<Question> questions = this.homeworkDao.getAllQuestionsOfHomework(
                homework.getId());
        List<Question> randomQuestions = new ArrayList<>();
        // throw in a random function so that each homework has
        // different questions assigned
        int randFn = Math.abs((int)Math.random() + homework.getId()) % 5;
        Random random = new Random(seed%randFn);
        while(randomQuestions.size() != homework.getNumQuestions()){
            int rand = Math.abs(random.nextInt()%questions.size());
            Question question = questions.get(rand);
            if(!randomQuestions.contains(question)){
                question.setAnswers(getRandomAnswers(question, random));
                randomQuestions.add(question);
            }
        }
        return randomQuestions;

    }

    private List<Answer> getRandomAnswers(Question q, Random random){
        List<Answer> allAnswers = this.homeworkDao.getAnswers(q.getId());
        List<Answer> selectedAnswers = new ArrayList<>();
        while(selectedAnswers.size() != NUM_CORRECT){
            int rand = Math.abs(random.nextInt() % allAnswers.size());
            Answer answer = allAnswers.get(rand);
            if(!selectedAnswers.contains(answer) && answer.isCorrect())
                selectedAnswers.add(answer);
        }
        while(selectedAnswers.size() != TOTAL_ANSWERS){
            int rand = Math.abs(random.nextInt() % allAnswers.size());
            Answer answer = allAnswers.get(rand);
            if(!selectedAnswers.contains(answer) && !answer.isCorrect())
                selectedAnswers.add(answer);

        }
        // now put the right answer in a random position
        int right = Math.abs(random.nextInt() % TOTAL_ANSWERS);
        Answer temp = selectedAnswers.get(right);
        selectedAnswers.set(right, selectedAnswers.get(0));
        selectedAnswers.set(0, temp);
        return selectedAnswers;
    }
}

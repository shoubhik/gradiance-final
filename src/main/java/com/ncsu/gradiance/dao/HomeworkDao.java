package com.ncsu.gradiance.dao;

import com.ncsu.gradiance.model.*;
import org.springframework.aop.target.LazyInitTargetSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.validation.Errors;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: shoubhik Date: 13/3/13 Time: 1:44 AM
 */
public class HomeworkDao {

    private JdbcTemplate jdbcTemplate;
    private CourseDao courseDao;
    private QuestionDao questionDao;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<ScoreSelectionScheme> getScoreSelectionSchemes(){
        return this.jdbcTemplate.query("select * from score_selection_scheme",
                                       new Object[]{}, new ScoreSelectionSchemeMapper());
    }

    public ScoreSelectionScheme getSchemeByName(String schemeName){
        return this.jdbcTemplate.queryForObject(
                "select * from score_selection_scheme where name = ?",
                new Object[]{schemeName}, new ScoreSelectionSchemeMapper());
    }

    public ScoreSelectionScheme getSchemeById(Integer id){
        return this.jdbcTemplate.queryForObject(
                "select * from score_selection_scheme where id = ?",
                new Object[]{id}, new ScoreSelectionSchemeMapper());
    }

    public void deleteQuestionsFromHomework(Integer hwId, List<Integer> qids){
        for(int qid : qids)
            deleteQuestionFromHomework(hwId, qid);
    }

    public void deleteQuestionFromHomework(Integer hwId, Integer qId){
        this.jdbcTemplate.update("delete from homework_questions where hw_id= ? and q_id= ?",
                                 new Object[]{hwId, qId});
    }



    public void insertHomeworkAttempt(Attempt attempt){
        Integer attemptId = getAttemptId();
        attempt.setId(attemptId);
        for(Question question : attempt.getQuestions()){
            Integer attemptAnsId = getAttemptAnsId();
            for(Answer answer : question.getAnswers()){
                this.jdbcTemplate.update("insert into attempt_ans(attempt_ans_id, ans_id) values(?,?)",
                                     new Object[]{attemptAnsId, new Integer(answer.getId())});
            }
            this.jdbcTemplate.update("insert into attempt(attempt_id, q_id, attempt_ans_id, response_id, response_exp) values(?,?,?,?,?)",
                  new Object[]{attemptId, new Integer(question.getId()), attemptAnsId,
                          null, null});
        }
        this.jdbcTemplate.update("insert into hw_student(hw_id, student_id, attempt_id, attempt_date, score, attempt_num) values(?,?,?,?,?, ?)",
            new Object[]{new Integer(attempt.getHomeworkId()),
            attempt.getUser().getUserName(), attemptId, null,
            null, new Integer(attempt.getAttemptNum())});
    }

    public void submitAttempt(Attempt attempt){
        for(Question question : attempt.getQuestions())
            this.jdbcTemplate.update("update attempt set response_id = ?,response_exp = ? where attempt_id=? and q_id=?",
            new Object[]{new Integer(question.getResponse().getId()),
            question.getExplaination(), new Integer(attempt.getId()), new Integer(question.getId())});
        Timestamp now = new Timestamp(new Date().getTime());
        this.jdbcTemplate.update("update hw_student set attempt_date = ?,score =? where attempt_id=? ",
                                 new Object[]{now, new Integer(attempt.getScore()),
                                         new Integer(attempt.getId())});
    }



    public void insertHomework(Homework homework, Errors errors){

        try{
        Integer homeworkId = getNextHomeworkId();
        String courseId = homework.getCourse().getCourseId();
        Integer scoreScheme = homework.getScoreSelectionScheme().getSchemeId();
        String name = homework.getName();
        java.sql.Timestamp startDate = homework.getStartDate();
        java.sql.Timestamp endDate = homework.getEndDate();
        Integer numAttempts = homework.getNumAttempts();
        Integer correctPts = homework.getCorrectPts();
        Integer incorrectPts = homework.getIncorrectPts();
        Integer numQuestions = homework.getNumQuestions();
        this.jdbcTemplate.update("insert into homeworks(hw_id, course_id, score_selection_scheme, name, start_date, end_date, num_attempts, correct_ans_pts, incorrect_ans_pts, numQuestions) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                                 new Object[]{homeworkId, courseId, scoreScheme,
                                         name, startDate, endDate, numAttempts, correctPts, incorrectPts, numQuestions});
        for(Question question : homework.getQuestions())
            insertHomeworkQuestion(question.getId(), homeworkId);
        }catch(Exception e){
            errors.rejectValue("", "", e.getMessage());
        }
    }

    public List<Homework> getAllHomeworksCreatedForCourse(String courseId){
        List<Homework> homeworks =  this.jdbcTemplate.query("select * from homeworks where course_id = ? order by start_date",
                                       new Object[]{courseId},
                                       new HomeworkMapper(this, this.courseDao));
        for(Homework homework : homeworks){
            List<Question> questions = getAllQuestionsOfHomework(homework.getId());
            for(Question question : questions){
                List<Answer> answers = this.questionDao.getAnswersForQuestion(question.getId());
                question.setAnswers(answers);
            }
            homework.setQuestions(questions);
        }
        return homeworks;
    }

    public List<Homework> getHomeWorksForCourseThatHaveNotStarted(String courseId){
        java.sql.Timestamp now = new Timestamp(new Date().getTime());
        return this.jdbcTemplate.query("select * from homeworks where course_id = ? and start_date > ?",
                                       new Object[]{courseId, now},
                                       new HomeworkMapper(this, this.courseDao));
    }

    public List<Homework> attemptedByUser(String studentId){
        List<Integer> hwIds =  this.jdbcTemplate.query("select unique(hw_id) from hw_student where student_id=?",
                              new Object[]{studentId},new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs,
                                  int rowNum)
                    throws SQLException {
                return new Integer(rs.getInt("hw_id"));
            }
        });
        List<Homework> homeworks = new ArrayList<>();
        for(int hwId : hwIds){
            Homework homework = getHomeworkById(hwId);
            homework.setAggregateScore(getHomeworkAggregateScore(studentId,
                 homework.getId(),homework.getScoreSelectionScheme().getSchemeId()));
            homeworks.add(homework );
        }
        return homeworks;

    }

    private int getHomeworkAggregateScore(String studentId, int hwId, int scoreScheme){
        switch(scoreScheme){
            case 1:
                return getScoreOfFirstAttempt(studentId, hwId);
            case 2:
                return getScoreOfLastAttempt(studentId, hwId);
            case 3:
                return getMaxScore(studentId, hwId);
            case 4:
                return getAvgScore(studentId, hwId);
        }
        throw new IllegalArgumentException();
    }

    private int getAvgScore(String studentId, int hwId){
        return this.jdbcTemplate.queryForInt("select avg(score) from hw_student  where hw_id = ? and student_id= ?",
                    new Object[]{new Integer(hwId), studentId});
    }

    private int getMaxScore(String studentId, int hwId){
        return this.jdbcTemplate.queryForInt("select max(score) from hw_student  where hw_id = ? and student_id= ?",
                                             new Object[]{new Integer(hwId), studentId});
    }

    private int getScoreOfFirstAttempt(String studentId, int hwId){
        return this.jdbcTemplate.queryForInt("select  * from(select score from hw_student where hw_id = ? and student_id=? order by attempt_date) where ROWNUM = 1",
                                             new Object[]{new Integer(hwId), studentId});
    }

    private int getScoreOfLastAttempt(String studentId, int hwId){
        return this.jdbcTemplate.queryForInt("select score from ( select score, rownum as rn from ( select score from hw_student where hw_id = ? and student_id= ? order by attempt_date)) where rn= (select count(*) from hw_student where hw_id = ? and student_id= ?)",
                                             new Object[]{new Integer(hwId), studentId, new Integer(hwId), studentId});

    }

    public Homework getHomeworkById(Integer hwId){

        return this.jdbcTemplate.queryForObject("select * from homeworks where hw_id = ?",
                                                new Object[]{hwId},
                                                new HomeworkMapper(this, this.courseDao) );


    }

    public List<Homework> getHomeworksStudentCanAttempt(String studentId, String courseId){
        java.sql.Timestamp now = new Timestamp(new Date().getTime());
        return this.jdbcTemplate.query("select * from homeworks where course_id = ?  and start_date <= ? and end_date >= ?",
                                       new Object[]{courseId, now, now},
                                       new HomeworkMapper(this, this.courseDao));
    }

    public List<Homework> submittedHomeworks(String studentId){
        List<Integer> hwIds =  this.jdbcTemplate.query("select unique(hw_id) from hw_student where student_id=?",
                                                       new Object[]{studentId},new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs,
                                  int rowNum)
                    throws SQLException {
                return new Integer(rs.getInt("hw_id"));
            }
        });
        List<Homework> homeworks = new ArrayList<>();
        for(int hwId : hwIds){
            Homework homework = getHomeworkById(hwId);
            List<Attempt> attempts = getAttempts(studentId, homework.getId());
            homework.setAllAttempts(attempts);
            homeworks.add(homework);

        }
        return homeworks;
    }

    public void insertHomeworkQuestion(Integer qId, Integer hwId){
        this.jdbcTemplate.update("insert into homework_questions(hw_id, q_id) values(?, ?)",
                                 new Object[]{hwId, qId});

    }

    public void updateHomeworkForEdit(Integer scheme, Timestamp start, Timestamp
            end, Integer attempts, Integer correctPts, Integer incorrectPts, Integer numQUes, Integer hwId){
        this.jdbcTemplate.update("update homeworks set score_selection_scheme = ?, " +
             "start_date =?, end_date=?, num_attempts=?, correct_ans_pts=?, " +
             "incorrect_ans_pts=?,numQuestions=? where hw_id=?",
              new Object[]{scheme, start, end, attempts, correctPts, incorrectPts, numQUes, hwId});

    }

    public List<Question> getAllQuestionsOfHomework(int hwId){
        return this.questionDao.getQuestionsInAHomework(hwId);
    }

    public List<Answer> getAnswers(int qid){
        return this.questionDao.getAnswersForQuestion(qid);
    }

    public List<Question> getAllQuestionsNotAddedInHomework(int hwId){
        return this.questionDao.getQuestionsNotInAHomework(hwId);
    }

    public int getSeedForStudent(String studentId){
        return this.jdbcTemplate.queryForInt("select seed from students where student_id = ?"
            , new Object[]{studentId});

    }

    public int getHomeworkAttemptNumber(String studentId, Integer hwId){
        return this.jdbcTemplate.queryForInt("select count(*) from hw_student where student_id = ? and hw_id = ?"
                , new Object[]{studentId, hwId}) + 1;
    }

    public boolean isHomeworkPending(String studentId, Integer hwId){
        return  this.jdbcTemplate.queryForInt("select count(*) from hw_student where student_id = ? and hw_id = ? and attempt_date is null"
                , new Object[]{studentId, hwId}) == 1;
    }

    public Attempt getPendingAttempt(String studentId, Integer hwId){
        return this.jdbcTemplate.queryForObject(
                "select * from hw_student where hw_id = ? and student_id = ? and attempt_date is null",
                new Object[]{hwId, studentId}, new AttemptMapper(this, this.questionDao));

    }

    public List<Attempt> getAttempts(String studentId, Integer hwId){
        return this.jdbcTemplate.query(
                "select * from hw_student where hw_id = ? and student_id = ? and attempt_date is not null order by attempt_date",
                new Object[]{hwId, studentId}, new AttemptMapper(this, this.questionDao));

    }

    public List<Integer> getQuestionIdsForTheAttempt(Integer attemptId){
        return this.jdbcTemplate.query("select q_id from attempt where attempt_id = ?",
                                       new Object[]{attemptId},
                                       new RowMapper<Integer>() {
                                           @Override
                                           public Integer mapRow(ResultSet rs,
                                                                 int rowNum)
                                                   throws SQLException {
                                               return new Integer(rs.getInt("q_id"));
                                           }
                                       });

    }

    public List<Answer> getAnswersForAQuestionOfAttempt(Integer attemptId, Integer qId){
        Integer attemptAnsId = this.jdbcTemplate.queryForInt("select attempt_ans_id from attempt where attempt_id = ? and q_id = ?",
                                                             new Object[]{attemptId, qId});
        List<Integer> ansIds = this.jdbcTemplate.query("select ans_id from attempt_ans where attempt_ans_id = ?",
                                                       new Object[]{attemptAnsId},
                                                       new RowMapper<Integer>() {
                                                           @Override
                                                           public Integer mapRow(ResultSet rs,
                                                                                 int rowNum)
                                                                   throws SQLException {
                                                               return new Integer(rs.getInt("ans_id"));
                                                           }
                                                       });
        List<Answer> answers = new ArrayList<>();
        for(int ansId : ansIds)
            answers.add(this.questionDao.getAnswerById(ansId));
        return answers;
    }

    private Answer getResponse(Integer attemptId, Integer questionId){
        int responseId = this.jdbcTemplate.queryForInt("select response_id from attempt where attempt_id = ? and q_id =?",
                         new Object[]{attemptId, questionId});
        if(responseId != 0)
            return this.questionDao.getAnswerById(responseId);
        return null;
    }

    private int getAttemptAnsId(){
        if(this.jdbcTemplate.queryForInt("select count(*) from attempt_ans") == 0)
            return 1;
        return this.jdbcTemplate.queryForInt("select max(attempt_ans_id) from attempt_ans")  + 1;
    }

    private int getAttemptId(){
        if(this.jdbcTemplate.queryForInt("select count(*) from attempt") == 0)
            return 1;
        return this.jdbcTemplate.queryForInt("select max(attempt_id) from attempt")  + 1;
    }

    private int getNextHomeworkId(){
        if(this.jdbcTemplate.queryForInt("select count(*) from homeworks") == 0)
            return 1;
        return this.jdbcTemplate.queryForInt("select max(hw_id) from homeworks")  + 1;
    }

    public CourseDao getCourseDao() {
        return courseDao;
    }

    public void setCourseDao(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    public QuestionDao getQuestionDao() {
        return questionDao;
    }

    public void setQuestionDao(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    private static final class ScoreSelectionSchemeMapper implements
                                                          RowMapper<ScoreSelectionScheme> {
        public ScoreSelectionScheme mapRow(ResultSet rs, int rowNum) throws SQLException {
            ScoreSelectionScheme scoreSelectionScheme = new ScoreSelectionScheme();
            scoreSelectionScheme.setSchemeId(rs.getInt("id"));
            scoreSelectionScheme.setName(rs.getString("name"));
            return scoreSelectionScheme;
        }
    }


    private static final class HomeworkMapper implements RowMapper<Homework> {

        private HomeworkDao homeworkDao;
        private CourseDao courseDao;
        public HomeworkMapper(HomeworkDao homeworkDao, CourseDao courseDao){
            this.homeworkDao = homeworkDao;
            this.courseDao = courseDao;
        }

        public Homework mapRow(ResultSet rs, int rowNum) throws SQLException {
            Homework homework = new Homework();
            homework.setId(rs.getInt("hw_id"));
            homework.setCourse(this.courseDao.getCourseById(rs.getString("course_id")));
            homework.setScoreSelectionScheme(this.homeworkDao.getSchemeById(rs.getInt("score_selection_scheme")));
            homework.setName(rs.getString("name"));
            homework.setStartDate(rs.getTimestamp("start_date"));
            homework.setEndDate(rs.getTimestamp("end_date"));
            homework.setNumAttempts(rs.getInt("num_attempts"));
            homework.setCorrectPts(rs.getInt("correct_ans_pts"));
            homework.setIncorrectPts(rs.getInt("incorrect_ans_pts"));
            homework.setNumQuestions(rs.getInt("numQuestions"));
            return homework;
        }
    }

    private static final class AttemptMapper implements RowMapper<Attempt> {

        private HomeworkDao homeworkDao;
        private QuestionDao questionDao;
        public AttemptMapper(HomeworkDao homeworkDao, QuestionDao questionDao){
            this.homeworkDao = homeworkDao;
            this.questionDao = questionDao;
        }

        public Attempt mapRow(ResultSet rs, int rowNum) throws SQLException {
            Attempt attempt = new Attempt();
            attempt.setId(rs.getInt("attempt_id"));
            attempt.setAttemptNum(rs.getInt("attempt_num"));
            attempt.setScore(rs.getInt("score"));
            attempt.setHomeworkId(rs.getInt("hw_id"));
            attempt.setTime(rs.getTimestamp("attempt_date"));
            List<Integer> qIds = this.homeworkDao.getQuestionIdsForTheAttempt(attempt.getId());
            List<Question> questions = new ArrayList<>();
            for(int qId : qIds){
                Question question = this.questionDao.getQuestionById(qId);
                List<Answer> answers = this.homeworkDao.
                        getAnswersForAQuestionOfAttempt(attempt.getId(),
                                                        question.getId());
                question.setAnswers(answers);
                Answer response = this.homeworkDao.getResponse(attempt.getId(), question.getId());
                question.setResponse(response);
                questions.add(question);
            }
            attempt.setQuestions(questions);
            return attempt;
        }
    }



}

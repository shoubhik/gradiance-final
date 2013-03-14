package com.ncsu.gradiance.dao;

import com.ncsu.gradiance.model.Answer;
import com.ncsu.gradiance.model.Hint;
import com.ncsu.gradiance.model.Question;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.validation.Errors;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class QuestionDao {

    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private int getNextHintId(){
        return this.jdbcTemplate.queryForInt("select count(*) from hints")  + 1;
    }


    private int getNextQuestionId(){
        return this.jdbcTemplate.queryForInt("select count(*) from questions")  + 1;
    }

    private int getNextAnswerId(){
        return this.jdbcTemplate.queryForInt("select count(*) from question_answers")  + 1;
    }

    private int insertHint(Hint hint){
        int hintId = getNextHintId();
        this.jdbcTemplate.update("insert into hints(hint_id, text) values(?, ?)",
                                 new Object[]{new Integer(hintId), hint.getText()});
        return hintId;
    }

    public Hint getHintById(final Integer hintId){
        return this.jdbcTemplate.queryForObject(
                "select * from hints where hint_id = ?",
                new Object[]{hintId},
                new RowMapper<Hint>() {
                    public Hint mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Hint hint = new Hint();
                        hint.setId(rs.getInt("hint_id"));
                        hint.setText(rs.getString("text"));
                        return hint;
                    }
                });
    }

    public List<Question> getQuestionsInAHomework(int hwId){
        return this.jdbcTemplate.query("select * from questions where q_id in (Select q_id from homework_questions where hw_id = ?)",
                                       new Object[]{hwId}, new QuestionMapper(this));
    }

    public List<Question> getQuestionsNotInAHomework(int hwId){
        return this.jdbcTemplate.query("select * from questions where q_id not in (Select q_id from homework_questions where hw_id = ?)",
                                       new Object[]{hwId}, new QuestionMapper(this));
    }

    public int insertQuestion(Question question, Errors errors) {
        int qId = getNextQuestionId();
        try {

            Object hintId =
                    question.isHasHint() ? new Integer(insertHint(question.
                            getHint())) : "null";
            Integer q_Id = new Integer(qId);
            String topicId = question.getTopicId();
            String text = question.getText();
            Integer diffLevel = new Integer(question.getDifficultyLevel());
            Integer ptsCorrect = new Integer(question.getPointCorrect());
            Integer ptsIncorrect = new Integer(question.getPointIncorrect());
            this.jdbcTemplate
                    .update("insert into questions(q_id, topic_id, text, " +
                                    "difficulty_level, points_incorrect, " +
                                    "points_correct, hint_id) values(?,?,?,?," +
                                    "?,?,?)",
                            new Object[]{q_Id, topicId, text, diffLevel,
                                    ptsCorrect, ptsIncorrect, hintId});
            for (Answer answer : question.getAnswers())
                insertAnswer(answer, qId);
        } catch (Exception e) {
            errors.rejectValue("", "", e.getMessage());
        }
        return qId;
    }

    public List<Question> getQuestionsForTopic(String topicId){
        return this.jdbcTemplate.query("select * from questions where topic_id = ?",
                                       new Object[]{topicId}, new QuestionMapper(this));

    }

    public List<Question> getAllQuestionsOfCourse(String courseId){
        return this.jdbcTemplate.query("select * from questions where topic_id in (select topic_id from course_topics where course_id= ?)",
                                       new Object[]{courseId}, new QuestionMapper(this));

    }

    public List<Answer> getAnswersForQuestion(Integer qid){
        return this.jdbcTemplate.query("select * from question_answers where q_id = ?",
                                       new Object[]{qid}, new AnswerMapper(this));
    }

    public int insertAnswer(Answer answer, Integer qId){
        Integer answerId = new Integer(getNextAnswerId());
        Integer hintId = new Integer(insertHint(answer.getHint()));
        Integer correct =  answer.getCorrect();
        this.jdbcTemplate.update("insert into question_answers(ans_id, text, hint_id, q_id, correct) values(?,?,?,?,?)",
                                 new Object[]{answerId, answer.getText(), hintId, qId, correct });
        return answerId;

    }

    public Question getQuestionById(Integer qid){
        return this.jdbcTemplate.queryForObject(
                "select * from questions where q_id = ? ",
                new Object[]{qid}, new QuestionMapper(this));

    }

    public Answer getAnswerById(Integer ansId){
        return this.jdbcTemplate.queryForObject(
                "select * from question_answers where ans_id = ? ",
                new Object[]{ansId}, new AnswerMapper(this));
    }

    private static final class QuestionMapper implements RowMapper<Question> {

        private QuestionDao questionDao;
        public QuestionMapper(QuestionDao questionDao){
            this.questionDao = questionDao;
        }

        public Question mapRow(ResultSet rs, int rowNum) throws SQLException {
            Question question = new Question();
            question.setId(rs.getInt("q_id"));
            question.setTopicId(rs.getString("topic_id"));
            question.setText(rs.getString("text"));
            question.setDifficultyLevel(rs.getInt("difficulty_level"));
            question.setPointIncorrect(rs.getInt("points_incorrect"));
            question.setPointCorrect(rs.getInt("points_correct"));
            int hintId = rs.getInt("hint_id");
            if(!rs.wasNull()) {
                question.setHint(this.questionDao.getHintById(hintId));

            }
            return question;
        }
    }

    private static final class AnswerMapper implements RowMapper<Answer> {

        private QuestionDao questionDao;
        public AnswerMapper(QuestionDao questionDao){
            this.questionDao = questionDao;
        }

        public Answer mapRow(ResultSet rs, int rowNum) throws SQLException {
            Answer answer = new Answer();
            answer.setId(rs.getInt("ans_id"));
            answer.setText(rs.getString("text"));
            answer.setCorrect(rs.getInt("correct"));
            int hintId = rs.getInt("hint_id");
            if(!rs.wasNull()) {
                answer.setHint(this.questionDao.getHintById(hintId));

            }
            return answer;
        }
    }
}

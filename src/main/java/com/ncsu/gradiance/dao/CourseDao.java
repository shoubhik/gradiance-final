package com.ncsu.gradiance.dao;

import com.ncsu.gradiance.model.Course;
import com.ncsu.gradiance.model.Topic;
import com.ncsu.gradiance.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.validation.Errors;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.ncsu.gradiance.dao.Constants.*;

import static com.ncsu.gradiance.dao.Constants.PROF_ROLE;

/**
 * User: shoubhik Date: 11/3/13 Time: 11:51 PM
 */
public class CourseDao {

    private JdbcTemplate jdbcTemplate;


    private UserDao userDao;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Course> getCoursesAddedByStudent(String uid){
        if(isStudentEnrolled(uid))
             return this.jdbcTemplate.query(
                     "select * from courses c where c.course_id in (select " +
                             "course_id from enrolled where student_id= ?)",
                     new Object[]{uid}, new CourseMapper());
        return new ArrayList<>();
    }

    public List<Course> getCoursesAddedByTA(String uid){
            return this.jdbcTemplate.query(
                    "select * from courses c where c.course_id in (select " +
                            "course_id from course_ta where student_id= ?)",
                    new Object[]{uid}, new CourseMapper());
    }

    public List<Course> getCoursesAddedByProf(String uid){
        if(isPofHasClass(uid))
            return this.jdbcTemplate.query(
                    "select * from courses  where prof_id = ?",
                    new Object[]{uid}, new CourseMapper());
        return new ArrayList<>();
    }

    public Course addCourseToUser(User user, String courseId, Errors errors){
        try{
            if(user.isProf())
                addCourseToProf(courseId, user.getUserName());
            else if(user.isStudent()) {
                String tokenId = courseId;
                courseId = getCourseIdByToken(tokenId);
                if(userDao.isTA(user.getUserName(), courseId)){
                    errors.rejectValue("", "", "you are already registered as a TA for the course");
                    return null;
                }
                addCourseToStudent(tokenId, user.getUserName());
            }
            Course course = getCourseById(courseId);
            user.getAddedCourses().add(course);
            return course;
        }catch (Exception e){
            errors.rejectValue("", "", e.getMessage());
        }
        return null;
    }

    public void addCourseToStudent(String token, String studentId){
        this.jdbcTemplate.update("insert into enrolled(course_id, student_id) select courses.course_id, ? from courses where courses.token= ? ",
                                 new Object[]{studentId, token});
    }

    public void addCourseToProf(String courseId, String profId){
        this.jdbcTemplate.update("update courses set prof_id = ? where course_Id = ?",
                                 new Object[]{profId, courseId});
    }

    public Course getCourseById(String courseId){
        return this.jdbcTemplate.queryForObject(
                "select * from courses  where course_id = ?",
                new Object[]{courseId}, new CourseMapper());

    }

    public String getCourseIdByToken(String token){
       return (String) this.jdbcTemplate
                .queryForObject("select course_id from courses where token = ?", new Object[]{token}, String.class);
    }

    public List<Course> coursesProfCanAdd(){
        return this.jdbcTemplate.query(
                "select * from courses  where prof_id is null and endDate > ?",
                new Object[]{getTodaysSqlDate()}, new CourseMapper());
    }

    public boolean isStudentEnrolled(String uid){
        int count =  this.jdbcTemplate.queryForInt("select count(*) from enrolled where student_id = ? ",
                                                   new Object[]{uid});
        return count > 0;
    }

    public boolean isPofHasClass(String uid){
        int count =  this.jdbcTemplate.queryForInt("select count(*) from courses where prof_id = ? ",
                                                   new Object[]{uid});
        return count > 0;
    }

    public List<Topic> getTopicsForCourse(String courseId){
        return this.jdbcTemplate.query(
                "select * from course_topics  where course_id = ?",
                new Object[]{courseId}, new RowMapper<Topic>() {
            @Override
            public Topic mapRow(ResultSet rs, int rowNum) throws SQLException {
                Topic topic = new Topic();
                topic.setCourseId(rs.getString("course_id"));
                topic.setTopicId(rs.getString("topic_id"));
                topic.setTopicName(rs.getString("name"));
                return topic;
            }
        });

    }

    private static final class CourseMapper implements RowMapper<Course> {

        public Course mapRow(ResultSet rs, int rowNum) throws SQLException {
            Course course = new Course();
            course.setCourseId(rs.getString("course_id"));
            course.setCourseName(rs.getString("name"));
            course.setFromDate(rs.getDate("startDate"));
            course.setToDate(rs.getDate("endDate"));
            course.setTokenId(rs.getString("token"));
            course.setProfId(rs.getString("prof_id"));
            return course;
        }
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

}

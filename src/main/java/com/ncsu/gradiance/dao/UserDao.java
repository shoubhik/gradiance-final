package com.ncsu.gradiance.dao;

import com.ncsu.gradiance.model.Course;
import com.ncsu.gradiance.model.MappedPageFactory;
import com.ncsu.gradiance.model.Student;
import com.ncsu.gradiance.model.User;
import org.springframework.aop.target.LazyInitTargetSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.validation.Errors;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static com.ncsu.gradiance.dao.Constants.*;

public class UserDao {
    private JdbcTemplate jdbcTemplate;
    private MappedPageFactory mappedPageFactory;
    private CourseDao courseDao;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }



    public User getUser(String userName, String password, Errors errors){
        if(getUserCount(userName, password) != 1 ){
            errors.rejectValue("", "user.invalid", "invalid user");
            return null;
        }
        User user = null;
        if(isProf(userName))
            user = getProfessor(userName);
        else if(isStudent(userName))
            user = getStudent(userName);
        return user;
    }

    private User getProfessor(final String username){
        return this.jdbcTemplate.queryForObject(
                "select * from prof where prof_id= ?",
                new Object[]{username},
                new RowMapper<User>() {
                    public User mapRow(ResultSet rs, int rowNum)
                            throws SQLException {
                        User user = new User();
                        user.setRoleName(PROF_ROLE);
                        user.setUserName(rs.getString("prof_id"));
                        user.setUserPages(mappedPageFactory.getProfPages());
                        user.setAddedCourses(courseDao.getCoursesAddedByProf(username));
                        return user;
                    }
                });
    }



    private User getStudent(final String username){
        return this.jdbcTemplate.queryForObject(
                "select * from students where student_id = ?",
                new Object[]{username},
                new RowMapper<User>() {
                    public User mapRow(ResultSet rs, int rowNum)
                            throws SQLException {
                        Student student = new Student();
                        student.setRoleName(STUDENT_ROLE);
                        student.setUserName(rs.getString("student_id"));
                        student.setSeed(rs.getInt("seed"));
                        student.setUserPages(mappedPageFactory.getStudentPages());
                        student.setAddedCourses(courseDao.getCoursesAddedByStudent(username));
                        // handle as TA
                        List<Course> taCourses = courseDao.getCoursesAddedByTA(username);
                        student.getAddedCourses().addAll(taCourses);
                        return student;
                    }
                });
    }

    public boolean isStudentEnrolledInCourse(String studentId, String courseId){
        int count =  this.jdbcTemplate.queryForInt("select count(*) from enrolled where student_id = ? and course_id = ?",
                                                   new Object[]{studentId, courseId});
        return count == 1;
    }

    private boolean isProf(String uid){
        int count =  this.jdbcTemplate.queryForInt("select count(*) from prof where prof_id = ? ",
                                             new Object[]{uid});
        return count == 1;
    }

    private boolean isStudent(String uid){
        int count =  this.jdbcTemplate.queryForInt("select count(*) from students where student_id = ? ",
                                                   new Object[]{uid});
        return count == 1;
    }

    public boolean isTA(String uid, String courseId){
        int count =  this.jdbcTemplate.queryForInt("select count(*) from course_ta where student_id = ? and course_id = ?",
                                                   new Object[]{uid, courseId});
        return count == 1;
    }

    private int getUserCount(String username, String password){
        return this.jdbcTemplate.queryForInt("select count(*) from users where u_id = ? and pwd = ?",
                                             new Object[]{username, password});
    }

    public void setMappedPageFactory(MappedPageFactory mappedPageFactory) {
        this.mappedPageFactory = mappedPageFactory;
    }

    public CourseDao getCourseDao() {
        return courseDao;
    }

    public void setCourseDao(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    public List<Map<String, Object>> makeGeneralQuery(String sql){
        return this.jdbcTemplate.queryForList(sql);
    }


}

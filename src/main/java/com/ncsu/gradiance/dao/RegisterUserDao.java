package com.ncsu.gradiance.dao;

import com.ncsu.gradiance.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.validation.Errors;

import javax.sql.DataSource;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.ncsu.gradiance.dao.Constants.*;

/**
 * User: shoubhik Date: 11/3/13 Time: 10:12 PM
 */
public class RegisterUserDao {

    private JdbcTemplate jdbcTemplate;
    private UserDao userDao;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public User register(String uid, String username, String password, String role, Errors errors){
        try{
        registerUser(uid, password);
        if(role.equals(PROF_ROLE))
            registerProf(uid, username);
        else if(role.equals(STUDENT_ROLE))
            registerStudent(uid, username);
            return this.userDao.getUser(uid, password, errors);
        }catch(Exception e){

            errors.rejectValue("", "", e.getMessage());
        }
        return null;
    }

    private void registerUser(String uid, String password){
         this.jdbcTemplate.update("insert into users(u_id, pwd) values(?, ?)",
                new Object[]{uid, password});
    }
    private void registerProf(String uid, String username ){
        this.jdbcTemplate.update("insert into prof(prof_id, name) values(?, ?)",
                                 new Object[]{uid, username});
    }

    private void registerStudent(String uid, String username ){
        this.jdbcTemplate.update("insert into students(student_id, name, seed) values(?, ?, ?)",
                                 new Object[]{uid, username,
                                         new Long(Math.round(Math.random() * 100000))});
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}

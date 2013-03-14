package com.ncsu.gradiance.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.ncsu.gradiance.dao.Constants.*;

public class User {
    protected String userName;
    protected String roleName;

    public Course getCourseSelected() {
        return courseSelected;
    }

    public void setCourseSelected(Course courseSelected) {
        this.courseSelected = courseSelected;
    }

    protected Course courseSelected;

    public List<Course> getAddedCourses() {
        return addedCourses;
    }

    public void setAddedCourses(List<Course> addedCourses) {
        this.addedCourses = addedCourses;
    }

    protected List<Course> addedCourses;

    public Map<String, String> getUserPages() {
        return userPages;
    }

    public void setUserPages(Map<String, String> userPages) {
        this.userPages = userPages;
    }

    protected Map<String, String> userPages;

    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public boolean isProf(){
        return this.roleName.equals(PROF_ROLE);
    }

    public boolean isStudent(){
        return this.roleName.equals(STUDENT_ROLE);
    }

    public List<Course> getCoursesStillActive(){
        if(getAddedCourses() == null)
            throw new IllegalStateException();
        List<Course> activeCourses = new ArrayList<>();
        for(Course course : getAddedCourses())
            if(!course.isCourseExpired())
                activeCourses.add(course);
        return activeCourses;

    }

    public void setCourseAsSelect(String courseId){
        if(getAddedCourses() == null)
            throw new IllegalStateException();
        for(Course course : getAddedCourses())
            if(course.getCourseId().equals(courseId)){
                setCourseSelected(course);
                break;
            }
    }
}

package com.ncsu.gradiance.controller;

import com.ncsu.gradiance.dao.CourseDao;
import com.ncsu.gradiance.dao.UserDao;
import com.ncsu.gradiance.model.Course;
import com.ncsu.gradiance.model.MappedPageFactory;
import com.ncsu.gradiance.model.Topic;
import com.ncsu.gradiance.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.ncsu.gradiance.dao.Constants.*;

@Controller
@RequestMapping("/selectCourse")
@SessionAttributes("user")
public class SelectCourseController {

    private CourseDao courseDao;
    private UserDao userDao;
    private MappedPageFactory mappedPageFactory;
    @Autowired
    public SelectCourseController(CourseDao courseDao, UserDao userDao,
                                  MappedPageFactory mappedPageFactory){
        this.courseDao = courseDao;
        this.userDao = userDao;
        this.mappedPageFactory = mappedPageFactory;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showForm(@ModelAttribute("user") User user, Model model){
        return user.getUserPages().get("selectCourse");
    }

    @RequestMapping(method = RequestMethod.POST)
    public String showSelectedCourseOptions(@ModelAttribute("user") User user,
                                            HttpServletRequest request){
        String courseId = request.getParameter("courseSelected");
        user.setCourseAsSelect(courseId);
        if(user.getRoleName().equals(STUDENT_ROLE) && userDao.isTA(
                user.getUserName(), courseId)){
            user.setRoleName(TA);
            user.setUserPages(mappedPageFactory.getTAPages());
        }
        else if(user.getRoleName().equals(TA) && !userDao.isTA(
                user.getUserName(), courseId) &&
                userDao.isStudentEnrolledInCourse(user.getUserName(), courseId)) {
            user.setRoleName(STUDENT_ROLE);
            user.setUserPages(mappedPageFactory.getStudentPages());

        }
        List<Topic> topics = this.courseDao.getTopicsForCourse(user.
                getCourseSelected().getCourseId());
        user.getCourseSelected().setTopics(topics);
        return user.getUserPages().get("afterCourseSelected");

    }

    @InitBinder
    public void bindCourseWithId(WebDataBinder binder){
        binder.setDisallowedFields("courseSelected");

    }

    @ModelAttribute("course")
    public List<String> getRoles(@ModelAttribute("user") User user,
                                 BindingResult result){
        List<String> courses = new ArrayList<>();
        for(Course course :user.getCoursesStillActive())
            courses.add(course.toString());
        return courses;
    }

}

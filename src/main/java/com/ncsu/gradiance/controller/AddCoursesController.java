package com.ncsu.gradiance.controller;

import com.ncsu.gradiance.dao.CourseDao;
import com.ncsu.gradiance.model.Course;
import com.ncsu.gradiance.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/addCourse")
@SessionAttributes("user")
public class AddCoursesController {

    private CourseDao courseDao;
    @Autowired
    public AddCoursesController(CourseDao courseDao){
        this.courseDao = courseDao;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showForm(@ModelAttribute("user") User user, Model model){
        return user.getUserPages().get("addCourse");
    }


    @RequestMapping(method = RequestMethod.POST)
    public String addCourse(@ModelAttribute("user" ) User user,
                            BindingResult result, Model model, HttpServletRequest request){
        // user info not proogated to UI so cannot rebuild.
        // pick up from session. its a hack but not worth the time
        String courseId;
        if(user.isProf())
            courseId = request.getParameter("courseSelected");
        else
            courseId = request.getParameter("token");
        Course course = this.courseDao.addCourseToUser(user, courseId, result);
        model.addAttribute("course", course);
        if(result.hasErrors())
            return user.getUserPages().get("addCourse");
        return user.getUserPages().get("courseAdded");
    }

    @ModelAttribute("course")
    public List<String> getRoles(@ModelAttribute("user") User user,
                                 BindingResult result){
        if(user.isStudent())
            return Collections.singletonList("");
        List<String> courses = new ArrayList<>();
        List<Course> canAdd = this.courseDao.coursesProfCanAdd();
         for(Course course : canAdd)
             courses.add(course.toString());
        return courses;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("courseSelected");
    }
}

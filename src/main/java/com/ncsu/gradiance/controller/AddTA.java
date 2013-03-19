package com.ncsu.gradiance.controller;

import com.ncsu.gradiance.dao.RegisterUserDao;
import com.ncsu.gradiance.dao.UserDao;
import com.ncsu.gradiance.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/addTA")
@SessionAttributes("user")
public class AddTA {
    private UserDao userDao;
    private RegisterUserDao registerDao;

    @Autowired
    public AddTA(UserDao userDao, RegisterUserDao registerDao){
        this.userDao = userDao;
        this.registerDao = registerDao;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showForm(@ModelAttribute("user") User user){
        if(!user.isProf())
            throw new AuthorizationException();
        return "addTA";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String submitForm(@ModelAttribute("user") User user, BindingResult
            result,HttpServletRequest request, Model model){
        String studentId = request.getParameter("ta");
        String error = null;
        if(StringUtils.isEmpty(studentId))
            error  = "student id cannot be empty";
        else if(!userDao.isStudent(studentId))
            error = "student does not exist";
        else if(userDao.isStudentEnrolledInCourse(studentId, user.
                getCourseSelected().getCourseId()))
            error = "student is already registered for the course";
        else if(userDao.isTA(studentId, user.getCourseSelected().getCourseId()))
            error = "student is already registered as TA for the course";
        if(error != null){
            result.rejectValue("", "", error);
            return "addTA";
        }
        this.registerDao.registerTA(studentId, user.getCourseSelected().
                getCourseId(), result);
        model.addAttribute("course", user.getCourseSelected().getCourseId());
        model.addAttribute("ta", studentId);
        return result.hasErrors() ? "addTA" : "taAdded";
    }
}

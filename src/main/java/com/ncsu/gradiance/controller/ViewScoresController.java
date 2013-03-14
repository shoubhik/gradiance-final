package com.ncsu.gradiance.controller;

import com.ncsu.gradiance.dao.HomeworkDao;
import com.ncsu.gradiance.model.Homework;
import com.ncsu.gradiance.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

@Controller
@RequestMapping("/viewScores")
@SessionAttributes({"user", "selectedCourse"})
public class ViewScoresController {

    private HomeworkDao homeworkDao;
    @Autowired
    public ViewScoresController(HomeworkDao homeworkDao){
        this.homeworkDao = homeworkDao;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showScores(@ModelAttribute("user") User user,
                              BindingResult result,
                             Model model){
        List<Homework> homeworks = this.homeworkDao.attemptedByUser(user.getUserName());
        user.getCourseSelected().setHomeworks(homeworks);
        model.addAttribute("homeworks", homeworks);
        return "viewScores";

    }
}

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

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/viewAllHomeworks")
@SessionAttributes("user")
public class ViewAllHomeworkController {

    private  HomeworkDao homeworkDao;
    @Autowired
    public ViewAllHomeworkController(HomeworkDao homeworkDao){
        this.homeworkDao = homeworkDao;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String chooseHomework(@ModelAttribute("user") User user,
                                 BindingResult result,
                                 Model model){
        List<Homework> homeworks = this.homeworkDao.getAllHomeworksCreatedForCourse(
                user.getCourseSelected().getCourseId());
        user.getCourseSelected().setHomeworks(homeworks);
        return "selectHomeworkForTa";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String showSubmissionSummary(@ModelAttribute("user") User user,
                                        BindingResult result,
                                        Model model, HttpServletRequest request){
        int hwId = Integer.parseInt(request.getParameter("homework"));
        Homework homework = user.getCourseSelected().getSelectedHomework(hwId);
        model.addAttribute("homework", homework);
        return "viewHomeworkSummary";
    }



}

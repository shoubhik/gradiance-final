package com.ncsu.gradiance.controller;

import com.ncsu.gradiance.dao.HomeworkDao;
import com.ncsu.gradiance.model.Homework;
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

@Controller
@RequestMapping("/viewPastSubmissions")
@SessionAttributes("user")
public class ViewPastSubmissionController {


    private HomeworkDao homeworkDao;
    @Autowired
    public ViewPastSubmissionController(HomeworkDao homeworkDao){
        this.homeworkDao = homeworkDao;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String chooseHomework(@ModelAttribute("user") User user,
                                  BindingResult result,
                                 Model model){
        List<Homework> homeworks = this.homeworkDao.submittedHomeworks(user.
                getUserName());
        user.getCourseSelected().setHomeworks(homeworks);
        return "selectAttemptedHomework";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String showSubmissionSummary(@ModelAttribute("user") User user,
                                        BindingResult result,
                                        Model model, HttpServletRequest request){
        String ids[] = request.getParameter("attempt").split("-");
        int hwId = Integer.parseInt(ids[0]);
        int attemptId = Integer.parseInt(ids[1]);
        Homework homework = user.getCourseSelected().getSelectedHomework(hwId);
        homework.setAttemptById(attemptId);
        model.addAttribute("summary", homework.getCurrentAttempt());
        return "viewHomeworkSubmissionSummary";
    }

}

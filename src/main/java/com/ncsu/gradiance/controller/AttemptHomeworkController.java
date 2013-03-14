package com.ncsu.gradiance.controller;

import com.ncsu.gradiance.dao.ChooseRandomQuestionAndAnswers;
import com.ncsu.gradiance.dao.HomeworkDao;
import com.ncsu.gradiance.dao.Scorer;
import com.ncsu.gradiance.model.Attempt;
import com.ncsu.gradiance.model.Homework;
import com.ncsu.gradiance.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/attemptHomework")
@SessionAttributes("user")
public class AttemptHomeworkController {


    private HomeworkDao homeworkDao;
    @Autowired
    public AttemptHomeworkController(HomeworkDao homeworkDao){
        this.homeworkDao = homeworkDao;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showHomeworks(@ModelAttribute("user") User user,
                                 BindingResult result,
                                Model model){
        List<Homework> homeworks = this.homeworkDao.getHomeworksStudentCanAttempt(
                user.getUserName(), user.getCourseSelected().getCourseId());
        user.getCourseSelected().setHomeworks(homeworks);
        return "selectHomeworkToAttempt";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String showForm(@ModelAttribute("user") User user,
                           BindingResult result,
                           @RequestParam("_page") int currentPage,
                           Model model, HttpServletRequest request){
        Map<Integer, String> pageForms = new HashMap<>();
        pageForms.put(1,"selectHomeworkToAttempt");
        pageForms.put(2,"submitHomeworkAttempt");
        if(request.getParameter("_cancel") != null){
            return pageForms.get(0);
        }
        else if(request.getParameter("_finish") != null){
            // handle finish
            Attempt attempt = user.getCourseSelected().getHomework().getCurrentAttempt();
            Scorer scorer = new Scorer(attempt, request, user.getCourseSelected().getHomework());
            scorer.calculate();
            attempt.submit(this.homeworkDao);
            model.addAttribute("summary", attempt);
            return "viewHomeworkSubmissionSummary";
        }
        else{
            int targetPage = WebUtils
                    .getTargetPage(request, "_target", currentPage);
            int hwId = Integer.parseInt(request.getParameter("homework"));
            Homework homework = user.getCourseSelected().getSelectedHomework(hwId);
            ChooseRandomQuestionAndAnswers chooseRandomQuestionAndAnswers =
                    new ChooseRandomQuestionAndAnswers(this.homeworkDao, user,
                                                       homework, result);
            chooseRandomQuestionAndAnswers.choose();
            return pageForms.get(targetPage);
        }

    }


}

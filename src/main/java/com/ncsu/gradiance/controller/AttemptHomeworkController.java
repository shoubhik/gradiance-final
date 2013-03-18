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
import java.sql.Timestamp;
import java.util.*;

@Controller
@RequestMapping("/attemptHomework")
@SessionAttributes("user")
public class AttemptHomeworkController extends BaseController{


    private HomeworkDao homeworkDao;
    @Autowired
    public AttemptHomeworkController(HomeworkDao homeworkDao){
        this.homeworkDao = homeworkDao;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showHomeworks(@ModelAttribute("user") User user,
                                 BindingResult result,
                                Model model){
        if(!user.isStudent())
            throw new AuthorizationException();
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
            return "redirect:selectCourse";
        }
        else if(request.getParameter("_finish") != null){
            // handle finish
            Homework homework =  user.getCourseSelected().getHomework();
            java.sql.Timestamp now = new Timestamp(new Date().getTime());
            if(now.after(homework.getEndDate())){
                result.rejectValue("", "", "Homework has expired");
                return pageForms.get(currentPage);
            }
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

package com.ncsu.gradiance.controller;

import com.ncsu.gradiance.dao.HomeworkDao;
import com.ncsu.gradiance.dao.QuestionDao;
import com.ncsu.gradiance.model.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/addHomework")
@SessionAttributes("user")
public class AddHomeWorkController extends BaseController{

    private HomeworkDao homeworkDao;
    private QuestionDao questionDao;
    @Autowired
    public AddHomeWorkController(HomeworkDao homeworkDao,
                                 QuestionDao questionDao){
        this.homeworkDao = homeworkDao;
        this.questionDao = questionDao;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showWizard(@ModelAttribute("user") User user, Model model){
        if(!user.isProf())
            throw new AuthorizationException();
        Homework homework = new Homework();
        homework.setCourse(user.getCourseSelected());
        user.getCourseSelected().setHomework(homework);
        model.addAttribute("newhomework", true);
        List<Question> questions = this.questionDao.getAllQuestionsOfCourse(
                user.getCourseSelected().getCourseId());
        user.getCourseSelected().setQuestions(questions);
        model.addAttribute("questions", questions);
        return "submitHomework";
    }

    @ModelAttribute("schemes")
    public List<String> getScoringSchemes(@ModelAttribute("user") User user,
                                          BindingResult result){
        List<String> schemes = new ArrayList<>();
        for(ScoreSelectionScheme scoreSelectionScheme : this.homeworkDao.
                getScoreSelectionSchemes())
            schemes.add(scoreSelectionScheme.toString());
        return schemes;
    }


    @RequestMapping(method = RequestMethod.POST)
    public String submitForm(@ModelAttribute("user") User user,
                             BindingResult result,
                             @RequestParam("_page") int currentPage,
                             HttpServletRequest request, Model model){
        if(request.getParameter("_cancel") != null){

            return "redirect:selectCourse";
        }
        else {
            // handle finish
            Course course = user.getCourseSelected();
            ValidateHomework validateHomework = new ValidateHomework(course.
                    getHomework(), request, result,
                                 this.homeworkDao, course);
            validateHomework.validateNewHomeWork();
            this.homeworkDao.insertHomework(course.getHomework(), result);
            if(result.hasErrors()) {
                model.addAttribute("questions", user.getCourseSelected().getQuestions());
                return  "submitHomework";
            }
            return "homeworkCreated";
        }

    }



    @InitBinder
    public void bindCourseWithId(WebDataBinder binder){
        binder.setDisallowedFields("courseSelected.homework.scoreSelectionScheme");
    }
}

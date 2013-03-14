package com.ncsu.gradiance.controller;

import com.ncsu.gradiance.dao.HomeworkDao;
import com.ncsu.gradiance.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/editHomework")
@SessionAttributes("user")
public class EditHomeworkController {


    private HomeworkDao homeworkDao;
    @Autowired
    public EditHomeworkController(HomeworkDao homeworkDao){
        this.homeworkDao = homeworkDao;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showWizard(@ModelAttribute("user") User user,
                             BindingResult result, Model model){
        model.addAttribute("editHomework", true);
        Course course = user.getCourseSelected();
        List<Homework> homeworks = this.homeworkDao.
                getHomeWorksForCourseThatHaveNotStarted(course.getCourseId());
        course.setHomeworks(homeworks);
        return "selectHomework";
    }


    @ModelAttribute("schemes")
    public List<String> getScoringSchemes(@ModelAttribute("user") User user){
        List<String> schemes = new ArrayList<>();
        for(ScoreSelectionScheme scoreSelectionScheme : this.homeworkDao.
                getScoreSelectionSchemes())
            schemes.add(scoreSelectionScheme.toString());
        return schemes;
    }


    @InitBinder
    public void bindCourseWithId(WebDataBinder binder){
        binder.setDisallowedFields("courseSelected.homework.scoreSelectionScheme");
    }

    @RequestMapping(method = RequestMethod.POST)
    public String submitForm(@ModelAttribute("user") User user,
                             BindingResult result,
                             @RequestParam("_page") int currentPage,
                             HttpServletRequest request, Model model){
        model.addAttribute("newhomework", false);
        Map<Integer, String> pageForms = new HashMap<>();
        pageForms.put(1,"selectHomework");
        pageForms.put(2,"ediHomework");
        if(request.getParameter("_cancel") != null){
            return "redirect:home";
        }
        else if(request.getParameter("_finish") != null){
            // handle finish
            Course course = user.getCourseSelected();
            ValidateHomework validateHomework = new ValidateHomework(course.
                    getHomework(), request, result,this.homeworkDao, course);
            validateHomework.updateEditHomework();
            if(result.hasErrors()){
                int selectedHomeworkId = user.getCourseSelected().getHomework().getId();
                List<Question> notAdded  = this.homeworkDao.
                        getAllQuestionsNotAddedInHomework(selectedHomeworkId);
                model.addAttribute("addedQuestions", user.getCourseSelected().
                        getHomework().getQuestions());
                model.addAttribute("notAddedQuestions", notAdded);
                return  pageForms.get(currentPage);
            }
            return "homeworkCreated";
        }
        else{
            int targetPage = WebUtils
                    .getTargetPage(request, "_target", currentPage);
            if(targetPage < currentPage){
                return pageForms.get(targetPage);
            }
            int selectedHomeworkId = Integer.parseInt(
                    request.getParameter("homework"));
            Homework homework = user.getCourseSelected().getSelectedHomework(selectedHomeworkId);
            List<Question> addedQuestions = this.homeworkDao.
                    getAllQuestionsOfHomework(homework.getId());
            homework.setQuestions(addedQuestions);
            List<Question> notAdded  = this.homeworkDao.
                    getAllQuestionsNotAddedInHomework(selectedHomeworkId);
            model.addAttribute("addedQuestions", addedQuestions);
            model.addAttribute("notAddedQuestions", notAdded);
            if(result.hasErrors())
                return  pageForms.get(currentPage);
            return pageForms.get(targetPage);
        }
    }
}

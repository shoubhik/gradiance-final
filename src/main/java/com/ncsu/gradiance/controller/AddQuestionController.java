package com.ncsu.gradiance.controller;

import com.ncsu.gradiance.dao.QuestionDao;
import com.ncsu.gradiance.model.Question;
import com.ncsu.gradiance.model.Topic;
import com.ncsu.gradiance.model.User;
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
@RequestMapping("/addQuestion")
@SessionAttributes("user")
public class AddQuestionController {

    private QuestionDao questionDao;
    @Autowired
    public AddQuestionController(QuestionDao questionDao){
        this.questionDao = questionDao;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showWizard(@ModelAttribute("user") User user){
        return "selectTopic";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String submitForm(@ModelAttribute("user") User user,
                             BindingResult result,
                             @RequestParam("_page") int currentPage,
                             HttpServletRequest request, Model model){
        model.addAttribute("newquestion", true);
        Map<Integer, String> pageForms = new HashMap<>();
        pageForms.put(0,"selectTopic");
        pageForms.put(1,"numAnswers");
        pageForms.put(2,"submitQuestions");
        if(request.getParameter("_cancel") != null){
            return pageForms.get(0);
        }
        else if(request.getParameter("_finish") != null){
            // handle finish
            ValidateQuestion validateQuestion = new ValidateQuestion(request, user.getCourseSelected().
                    getSelectedTopic().getQuestion(), result);
            validateQuestion.isValidNewQuestion();
            this.questionDao.insertQuestion(user.getCourseSelected().
                    getSelectedTopic().getQuestion(), result);
            if(result.hasErrors())
                return  pageForms.get(currentPage);
            return "questionAddedSummary";
        }
        else{
            int targetPage = WebUtils
                    .getTargetPage(request, "_target", currentPage);
            if(targetPage < currentPage){
                return pageForms.get(targetPage);
            }
            // before going to next page validate input
            handlePages(currentPage, user, request, result);
            if(result.hasErrors())
                return  pageForms.get(currentPage);
            return pageForms.get(targetPage);
        }
    }

    private void handlePages(int currentPage, User user, HttpServletRequest request,
                             Errors errors){
        switch (currentPage){
            case 0:
                String topicName = request.getParameter("courseSelected.topics");
                user.getCourseSelected().setTopicSelectedByUser(topicName);
                break;
            case 1:
                try{
                    int numCorrect = Integer.parseInt(request.getParameter("numCorrect"));
                    int numIncorrect = Integer.parseInt(request.getParameter("numIncorrect"));
                    if(numCorrect < 1 || numIncorrect < 3)
                        errors.rejectValue("", "", "there should be at least 1 correct and 3 incorrect");
                    if(!errors.hasErrors()){
                        Question question = new Question();
                        question.setNumCorrectAnswers(numCorrect);
                        question.setNumIncorrectAnswers(numIncorrect);
                        Topic topic = user.getCourseSelected().getSelectedTopic();
                        question.setTopicId(topic.getTopicId());
                        topic.setQuestion(question);
                    }
                }catch(NumberFormatException e){
                    errors.rejectValue("", "", e.getMessage());
                }

            break;
        }
    }

    @ModelAttribute("topic")
    public List<String> getTopics(@ModelAttribute("user") User user,
                                  BindingResult result){
        List<String> topics = new ArrayList<>();
        for(Topic topic : user.getCourseSelected().getTopics())
            topics.add(topic.toString());
        return topics;
    }

    @InitBinder
    public void bindCourseWithId(WebDataBinder binder){
        binder.setDisallowedFields("courseSelected.topics");
    }
}

package com.ncsu.gradiance.controller;

import com.ncsu.gradiance.dao.QuestionDao;
import com.ncsu.gradiance.model.Answer;
import com.ncsu.gradiance.model.Question;
import com.ncsu.gradiance.model.Topic;
import com.ncsu.gradiance.model.User;
import oracle.net.ano.AnoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/addAnswer")
@SessionAttributes("user")
public class AddAnswerController extends BaseController{

    private QuestionDao questionDao;
    @Autowired
    public AddAnswerController(QuestionDao questionDao){
        this.questionDao = questionDao;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showWizard(@ModelAttribute("user") User user){
        if(!user.isProf())
            throw new AuthorizationException();
        return "selectTopic";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String submitForm(@ModelAttribute("user") User user,
                             BindingResult result,
                             @RequestParam("_page") int currentPage,
                             HttpServletRequest request, Model model){
        model.addAttribute("newquestion", false);
        Map<Integer, String> pageForms = new HashMap<>();
        pageForms.put(0,"selectTopic");
        pageForms.put(1,"selectQuestion");
        pageForms.put(2,"numAnswers");
        pageForms.put(3,"submitQuestions");
        if(request.getParameter("_cancel") != null){
            return pageForms.get(0);
        }
        else if(request.getParameter("_finish") != null){
            // handle finish
            ValidateQuestion validateQuestion = new ValidateQuestion(request, user.getCourseSelected().
                    getSelectedTopic().getQuestion(), result);
            validateQuestion.isValidAddAnswer();
            if(!result.hasErrors()){
                Question question = user.getCourseSelected().getSelectedTopic().
                        getQuestion();
                for(Answer answer : question.getAnswers())
                    this.questionDao.insertAnswer(answer, question.getId());
            }
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
                Topic selectedTopic = user.getCourseSelected().getSelectedTopic();
                List<Question> questions = this.questionDao.getQuestionsForTopic(selectedTopic.getTopicId());
                selectedTopic.setQuestions(questions);
                break;
            case 1:
                String question = request.getParameter("courseSelected.selectedTopic.question");
                int qid = Integer.parseInt(question.split("-")[0]);
                selectedTopic = user.getCourseSelected().getSelectedTopic();
                selectedTopic.setSelectedQuestionById(qid);
                break;
            case 2:
                try{
                    int numCorrect = Integer.parseInt(request.getParameter("numCorrect"));
                    int numIncorrect = Integer.parseInt(request.getParameter("numIncorrect"));
                    if(numCorrect+numIncorrect < 1){
                        errors.rejectValue("", "", "there should at least be one question");
                        return;
                    }
                    Question question1 = user.getCourseSelected().getSelectedTopic().getQuestion();
                    question1.setNumCorrectAnswers(numCorrect);
                    question1.setNumIncorrectAnswers(numIncorrect);

                }catch (Exception e){
                    errors.rejectValue("", "", e.getMessage());
                }
                break;
        }
    }

    @InitBinder
    public void bindCourseWithId(WebDataBinder binder){
        binder.setDisallowedFields("courseSelected.topics",
                                   "courseSelected.selectedTopic.question");
    }

    @ModelAttribute("topic")
    public List<String> getTopics(@ModelAttribute("user") User user,
                                  BindingResult result){
        if(user.getCourseSelected() == null)
            return Collections.singletonList("");
        List<String> topics = new ArrayList<>();
        for(Topic topic : user.getCourseSelected().getTopics())
            topics.add(topic.toString());
        return topics;
    }

    @ModelAttribute("question")
    public List<String> getQuestions(@ModelAttribute("user") User user,
                                     BindingResult result){
        List<String> questions = new ArrayList<>();
        if(user.getCourseSelected() == null || user.getCourseSelected().
                getSelectedTopic() == null)
            return Collections.singletonList("");
        if(user.getCourseSelected().getSelectedTopic().getQuestions() == null){
            Topic selectedTopic = user.getCourseSelected().getSelectedTopic();
            List<Question> qs = this.questionDao.getQuestionsForTopic(selectedTopic.getTopicId());
            selectedTopic.setQuestions(qs);
        }
        for(Question question : user.getCourseSelected().getSelectedTopic().getQuestions())
            questions.add(question.toString());
        return questions;
    }


}

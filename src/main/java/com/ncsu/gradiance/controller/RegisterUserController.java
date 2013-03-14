package com.ncsu.gradiance.controller;

import com.ncsu.gradiance.dao.RegisterUserDao;
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
import java.util.Arrays;
import java.util.List;

import static com.ncsu.gradiance.dao.Constants.*;


@Controller
@RequestMapping("/register")
@SessionAttributes("user")
public class RegisterUserController {

    private RegisterUserDao registerUserDao;

    @Autowired
    public RegisterUserController(RegisterUserDao registerUserDao){
        this.registerUserDao = registerUserDao;
    }



    @RequestMapping(method = RequestMethod.GET)
    public String showForm(@ModelAttribute("user") User user, Model model){
        if(user == null) model.addAttribute("user", new User());
        return "registerUser";

    }

    @RequestMapping(method = RequestMethod.POST)
    public String register(@ModelAttribute("user") User dummyUser,
                           BindingResult result, HttpServletRequest request, Model model){
        String uid = request.getParameter("uid");
        String username = request.getParameter("uname");
        String password = request.getParameter("password");
        String role = request.getParameter("roleName");
        User user = this.registerUserDao.register(uid, username, password, role,result);
        if(!result.hasErrors()){
            model.addAttribute("user", user);
            return "redirect:home" ;

        }
        else return "registerUser";
    }

    @ModelAttribute("roles")
    public List<String> getRoles(){
        return Arrays.asList(new String[]{STUDENT_ROLE, PROF_ROLE});
    }
}

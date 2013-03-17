package com.ncsu.gradiance.controller;

import com.ncsu.gradiance.dao.UserDao;
import com.ncsu.gradiance.model.User;
import com.ncsu.gradiance.model.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

@Controller
@RequestMapping("/welcome")
@SessionAttributes("user")
public class LoginController {

    private UserDao userDao;

    @Autowired
    public LoginController(UserDao userDao){
        this.userDao = userDao;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String welcome( Model model) {
        model.addAttribute("user", new User());
        return "welcome";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String validate(@ModelAttribute("user") User dummyUser,
                           BindingResult result, HttpServletRequest request,
                           Model model)
            throws SQLException {
        String username = request.getParameter("uname");
        String password = request.getParameter("password");
        User user = this.userDao.getUser(username, password, result);
        if(result.hasErrors()) return "welcome";
        model.addAttribute("user", user);
        return "redirect:home" ;
    }
}

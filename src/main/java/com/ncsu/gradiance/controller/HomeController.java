package com.ncsu.gradiance.controller;

import com.ncsu.gradiance.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/home")
@SessionAttributes("user")
public class HomeController {

    @RequestMapping(method = RequestMethod.GET)
    public String showHomePage(@ModelAttribute("user") User user, Model model){
        model.addAttribute("role", user.getRoleName());
        return user.getUserPages().get("home");
    }
}

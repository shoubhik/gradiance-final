package com.ncsu.gradiance.controller;

import com.ncsu.gradiance.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequestMapping("/logout")
@SessionAttributes("user")
public class LogoutController {

    @RequestMapping(method = RequestMethod.GET)
    public String welcome( @ModelAttribute("user") User user,ModelMap model,
                           SessionStatus status) {
        model.clear();
        status.setComplete();
        return "redirect:welcome";
    }
}

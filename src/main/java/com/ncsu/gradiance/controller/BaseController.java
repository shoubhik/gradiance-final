package com.ncsu.gradiance.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * Class for implementing common functionality. this class need not be abstract.
 * but this is the the best way of sharing the exception handling.
 * i feel bad about this class. A future me will come and strangle myself.
 */
public abstract class BaseController  {

    @ExceptionHandler(AuthorizationException.class)
    public ModelAndView handleAuthorizationException(AuthorizationException ae){
        ModelAndView modelAndView = new ModelAndView("notAuthorized");
        return modelAndView;
    }
}

package com.myaudiolibrary.web.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(EntityNotFoundException.class)
    public ModelAndView handleEntityNotFoundException(EntityNotFoundException e){
        ModelAndView modelAndView = new ModelAndView("erreur", HttpStatus.NOT_FOUND);
        modelAndView.addObject("error", e.getMessage());
        modelAndView.addObject("status", HttpStatus.NOT_FOUND);
        return modelAndView;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleEntityNotFoundException(IllegalArgumentException e){
        ModelAndView modelAndView = new ModelAndView("erreur", HttpStatus.BAD_REQUEST);
        modelAndView.addObject("error", e.getMessage());
        modelAndView.addObject("status", HttpStatus.BAD_REQUEST);
        return modelAndView;
    }

}

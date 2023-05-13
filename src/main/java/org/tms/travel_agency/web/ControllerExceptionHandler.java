package org.tms.travel_agency.web;


import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.tms.travel_agency.exception.TravelAgencyException;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(TravelAgencyException.class)
    public ModelAndView process(TravelAgencyException exception) {
        ModelAndView modelAndView = new ModelAndView("exception");
        modelAndView.addObject("exception", exception.getMessage());
        return modelAndView;
    }
}
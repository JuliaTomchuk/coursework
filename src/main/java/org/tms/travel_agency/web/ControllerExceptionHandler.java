package org.tms.travel_agency.web;


import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.tms.travel_agency.exception.DuplicateDestinationException;
import org.tms.travel_agency.exception.DuplicateHotelException;
import org.tms.travel_agency.exception.DuplicateRegionException;
import org.tms.travel_agency.exception.DuplicateRoomException;
import org.tms.travel_agency.exception.DuplicateUserException;
import org.tms.travel_agency.exception.NoSuchCartException;
import org.tms.travel_agency.exception.NoSuchDestinationException;
import org.tms.travel_agency.exception.NoSuchHotelException;
import org.tms.travel_agency.exception.NoSuchRegionException;
import org.tms.travel_agency.exception.NoSuchRoomException;
import org.tms.travel_agency.exception.NoSuchUserException;
import org.tms.travel_agency.exception.NotAllowedException;
import org.tms.travel_agency.exception.TravelDateException;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(NoSuchDestinationException.class)
    public ModelAndView process(NoSuchDestinationException exception){
        ModelAndView modelAndView = new ModelAndView("exception");
        modelAndView.addObject("exception",exception.getMessage());
        return modelAndView;
    }
    @ExceptionHandler(DuplicateDestinationException.class)
    public ModelAndView process(DuplicateDestinationException exception){
        ModelAndView modelAndView = new ModelAndView("exception");
        modelAndView.addObject("exception", exception.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(NoSuchRegionException.class)
    public ModelAndView process(NoSuchRegionException exception){
        ModelAndView modelAndView = new ModelAndView("exception");
        modelAndView.addObject("exception", exception.getMessage());
        return modelAndView;
    }
    @ExceptionHandler(DuplicateRegionException.class)
    public ModelAndView process(DuplicateRegionException exception){
        ModelAndView modelAndView = new ModelAndView("exception");
        modelAndView.addObject("exception", exception.getMessage());
        return modelAndView;
    }
    @ExceptionHandler(DuplicateUserException.class)
    public ModelAndView process(DuplicateUserException exception) {
        ModelAndView modelAndView = new ModelAndView("exception");
        modelAndView.addObject("exception", exception.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(NoSuchUserException.class)
    public ModelAndView process(NoSuchUserException exception) {
        ModelAndView modelAndView = new ModelAndView("/exception");
        modelAndView.addObject("exception", exception.getMessage());
        return modelAndView;
    }
    @ExceptionHandler (NoSuchHotelException.class)
    public ModelAndView process(NoSuchHotelException exception) {
        ModelAndView modelAndView = new ModelAndView("/exception");
        modelAndView.addObject("exception", exception.getMessage());
        return modelAndView;
    }
    @ExceptionHandler (DuplicateHotelException.class)
    public ModelAndView process(DuplicateHotelException exception) {
        ModelAndView modelAndView = new ModelAndView("/exception");
        modelAndView.addObject("exception", exception.getMessage());
        return modelAndView;
    }
    @ExceptionHandler (NoSuchRoomException.class)
    public ModelAndView process(NoSuchRoomException exception) {
        ModelAndView modelAndView = new ModelAndView("/exception");
        modelAndView.addObject("exception", exception.getMessage());
        return modelAndView;
    }
    @ExceptionHandler (DuplicateRoomException.class)
    public ModelAndView process(DuplicateRoomException exception) {
        ModelAndView modelAndView = new ModelAndView("/exception");
        modelAndView.addObject("exception", exception.getMessage());
        return modelAndView;
    }

    @ExceptionHandler (TravelDateException.class)
    public ModelAndView process(TravelDateException exception) {
        ModelAndView modelAndView = new ModelAndView("/exception");
        modelAndView.addObject("exception", exception.getMessage());
        return modelAndView;
    }
    @ExceptionHandler (NoSuchCartException.class)
    public ModelAndView process(NoSuchCartException exception) {
        ModelAndView modelAndView = new ModelAndView("/exception");
        modelAndView.addObject("exception", exception.getMessage());
        return modelAndView;
    }
    @ExceptionHandler (NotAllowedException.class)
    public ModelAndView process(NotAllowedException exception) {
        ModelAndView modelAndView = new ModelAndView("/exception");
        modelAndView.addObject("exception", exception.getMessage());
        return modelAndView;
    }


}

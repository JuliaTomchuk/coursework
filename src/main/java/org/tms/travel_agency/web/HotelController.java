package org.tms.travel_agency.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.tms.travel_agency.dto.hotel.HotelInputDto;

@Controller
@RequestMapping("/hotel")
public class HotelController {

    @GetMapping("/hotelCreator")
    public ModelAndView getHotelCreatorPage(){
        ModelAndView modelAndView = new ModelAndView("/hotelCreator");
        HotelInputDto newHotel = new HotelInputDto();
        modelAndView.addObject("newHotel",newHotel);
       return modelAndView;
    }
    @PostMapping("/create")
    public ModelAndView saveHotel(HotelInputDto hotel){
        ModelAndView modelAndView = new ModelAndView("/hotelCreator");
        return modelAndView;
    }
}

package org.tms.travel_agency.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.tms.travel_agency.dto.room.RoomDetailsDto;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {

    @GetMapping("/addRoom")
    public ModelAndView getAddRoomPage(@RequestParam UUID id){
        RoomDetailsDto newRoom = new RoomDetailsDto();
        ModelAndView modelAndView = new ModelAndView("addRoom");
        modelAndView.addObject("idHotel",id);
        modelAndView.addObject("newRoom",newRoom);
        return modelAndView;
    }
}

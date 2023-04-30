package org.tms.travel_agency.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.tms.travel_agency.dto.room.RoomDetailsDto;
import org.tms.travel_agency.services.RoomService;
import org.tms.travel_agency.validator.OnCreate;
import org.tms.travel_agency.validator.OnUpdate;


import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {
    private final RoomService roomService;

    @GetMapping("/add")
    public ModelAndView getAddRoomPage(@RequestParam UUID id){
        RoomDetailsDto newRoom = new RoomDetailsDto();
        newRoom.setIdHotel(id);
        ModelAndView modelAndView = new ModelAndView("addRoom");
        modelAndView.addObject("newRoom",newRoom);
        return modelAndView;
    }
    @PostMapping("/add")
    public ModelAndView saveRoom(@Validated(OnCreate.class)@ModelAttribute("newRoom") RoomDetailsDto dto, BindingResult result){
        ModelAndView modelAndView = new ModelAndView();
        if(result.hasErrors()){
            return  new ModelAndView("/addRoom");
        }
        roomService.save(dto);
          return new ModelAndView("redirect:/hotels/hotelManager");
    }
    @GetMapping("/delete")
    public RedirectView delete(@RequestParam UUID id){
        roomService.delete(id);
        RedirectView redirectView = new RedirectView("/hotels/hotelManager");
        return redirectView;
    }
    @PostMapping("/update")
    public String update(@ModelAttribute("currentRoom")@Validated(OnUpdate.class) RoomDetailsDto roomDetailsDto,BindingResult result, RedirectAttributes attributes){
        if(result.hasErrors()){
            return "updateRoom";
        }
        roomService.update(roomDetailsDto);
        attributes.addAttribute("id",roomDetailsDto.getIdHotel());
        return "redirect:/hotels/details/";
    }
    @GetMapping("/update")
    public ModelAndView getUpdatePage(@RequestParam UUID id){
        RoomDetailsDto byId = roomService.getById(id);
        ModelAndView modelAndView = new ModelAndView("updateRoom");
        modelAndView.addObject("currentRoom",byId);
        return modelAndView;
    }

}

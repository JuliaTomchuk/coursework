package org.tms.travel_agency.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
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
import org.tms.travel_agency.dto.hotel.HotelLightDto;
import org.tms.travel_agency.dto.room.RoomDetailsDto;
import org.tms.travel_agency.dto.room.RoomLightDto;
import org.tms.travel_agency.services.HotelService;
import org.tms.travel_agency.services.RegionService;
import org.tms.travel_agency.services.RoomService;
import org.tms.travel_agency.validator.OnCreate;
import org.tms.travel_agency.validator.OnSearch;
import org.tms.travel_agency.validator.OnUpdate;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {
    private final RoomService roomService;
    private final HotelService hotelService;

    @GetMapping("book")
    public String book(UUID uuid){
        roomService.book(uuid);
        return "/cart";
    }
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
        attributes.addAttribute("id",roomDetailsDto.getId());
        return "redirect:/rooms/details/";
    }
    @GetMapping("/update")
    public ModelAndView getUpdatePage(@RequestParam UUID id){
        RoomDetailsDto byId = roomService.getById(id);
        ModelAndView modelAndView = new ModelAndView("updateRoom");
        modelAndView.addObject("currentRoom",byId);
        return modelAndView;
    }
    @GetMapping("/details")
    public ModelAndView getDetailsPage(@RequestParam UUID id){
        RoomDetailsDto currentRoom = roomService.getById(id);
        ModelAndView modelAndView = new ModelAndView("roomDetailsForAdmin");
        modelAndView.addObject("currentRoom",currentRoom);
        return modelAndView;
    }
    @GetMapping
    public ModelAndView getSearchPage(@RequestParam(name = "regionName") String regionName, @RequestParam(name="destinationName") String destinationName){
        ModelAndView modelAndView=new ModelAndView("roomSearch");
        RoomDetailsDto searchRoom = new RoomDetailsDto();
        searchRoom.setDestination(destinationName);
        searchRoom.setRegion(regionName);
        List<HotelLightDto> hotels  = hotelService.getByRegionName(regionName);
        modelAndView.addObject("searchRoom",searchRoom);
        modelAndView.addObject("hotels", hotels);

        return modelAndView;
    }
    @PostMapping
    public ModelAndView search(@Validated(OnSearch.class)  @ModelAttribute(name="searchRoom") RoomDetailsDto dto, BindingResult result){
        ModelAndView modelAndView = new ModelAndView("roomSearch");
        List<HotelLightDto> hotels  = hotelService.getByRegionName(dto.getRegion());
        modelAndView.addObject("hotels",hotels);
        if(result.hasErrors()){
            return modelAndView;
        }
        List<RoomDetailsDto> rooms = roomService.getRoomsListForBooking(dto);
        modelAndView.addObject("rooms",rooms);
        modelAndView.addObject("roomForm", new RoomDetailsDto());

        return modelAndView;
    }

    @PostMapping("/detailsForUser")
    public ModelAndView getDetailsPage( RoomDetailsDto room){
        ModelAndView modelAndView = new ModelAndView("roomDetailsForUser");
        modelAndView.addObject("currentRoom",room);
        return modelAndView;
    }
    @PostMapping("/prebook")
    public ModelAndView prebook(RoomDetailsDto dto){
        ModelAndView modelAndView = new ModelAndView("roomSearch");
        roomService.prebook(dto);
        RoomDetailsDto searchRoom = new RoomDetailsDto();
        searchRoom.setRegion(dto.getRegion());
        searchRoom.setDestination(dto.getDestination());
        modelAndView.addObject("hotels", hotelService.getByRegionName(dto.getRegion()));
        modelAndView.addObject("searchRoom", searchRoom);
        return modelAndView;
    }



}

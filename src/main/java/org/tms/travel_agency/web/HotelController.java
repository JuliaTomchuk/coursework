package org.tms.travel_agency.web;

import lombok.AllArgsConstructor;
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
import org.tms.travel_agency.domain.BoardBasisTypes;
import org.tms.travel_agency.dto.hotel.HotelDetailsDto;
import org.tms.travel_agency.dto.hotel.HotelLightDto;
import org.tms.travel_agency.dto.region.RegionLightDto;
import org.tms.travel_agency.dto.review.ReviewLightDto;
import org.tms.travel_agency.dto.room.RoomDetailsDto;
import org.tms.travel_agency.dto.room.RoomLightDto;
import org.tms.travel_agency.services.HotelService;
import org.tms.travel_agency.services.RegionService;
import org.tms.travel_agency.services.ReviewService;
import org.tms.travel_agency.services.RoomService;
import org.tms.travel_agency.validator.OnCreate;
import org.tms.travel_agency.validator.OnUpdate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/hotels")
@AllArgsConstructor
public class HotelController {
    private final RegionService regionService;
    private final HotelService hotelService;
    private final RoomService roomService;
    private final ReviewService reviewService;

    @GetMapping("/create")
    public ModelAndView getHotelCreatorPage(){
        ModelAndView modelAndView = new ModelAndView("/hotelCreator");
        HotelDetailsDto newHotel = new HotelDetailsDto();
        List<RegionLightDto> allRegions = regionService.getAll();
        modelAndView.addObject("newHotel",newHotel);
        modelAndView.addObject("regions",allRegions);
       return modelAndView;
    }
    @PostMapping("/create")
    public String saveHotel(@Validated(OnCreate.class) @ModelAttribute("newHotel") HotelDetailsDto newHotel, BindingResult result){
        if(result.hasErrors()){
            return "hotelCreator";

        }
        hotelService.save(newHotel);
        return "redirect:/hotels/hotelManager";
         }

     @GetMapping("/hotelManager")
    public ModelAndView getHotelManagerPage(){
        ModelAndView modelAndView = new ModelAndView("hotelManager");
         List<HotelLightDto> hotels = hotelService.getAll();
         modelAndView.addObject("hotels",hotels);
         return modelAndView;
     }
     @GetMapping ("/delete")
    public String delete(@RequestParam UUID id){
        hotelService.delete(id);
        return "redirect:/hotels/hotelManager";
     }

     @GetMapping("/update")
    public ModelAndView getUpdatePage(@RequestParam UUID id){
        ModelAndView modelAndView = new ModelAndView("updateHotel");
         HotelDetailsDto hotel = hotelService.getById(id);
         modelAndView.addObject("hotel",hotel);
         return modelAndView;
     }
     @PostMapping("/update")
      public String update(@Validated(OnUpdate.class) @ModelAttribute("hotel") HotelDetailsDto dto, BindingResult result){
             if(result.hasErrors()){
            return "updateHotel";
        }
        hotelService.update(dto);
        return "redirect:/hotels/hotelManager";
     }
     @GetMapping("/details")
    public ModelAndView getHotelDetails(@RequestParam UUID id){
         HotelDetailsDto byId = hotelService.getById(id);
         RoomDetailsDto roomDetailsDto = new RoomDetailsDto();
         roomDetailsDto.setIdHotel(id);
         List<RoomLightDto> rooms = roomService.search(roomDetailsDto);
         ModelAndView modelAndView = new ModelAndView("hotelDetailsForAdmin");
         modelAndView.addObject("hotel", byId);
         modelAndView.addObject("rooms", rooms);
         return modelAndView;
     }
     @GetMapping("/addBoardBasis")
    public ModelAndView getAddBoardBasisType(@RequestParam UUID id){
        ModelAndView modelAndView = new ModelAndView("addBoardBasis");
        modelAndView.addObject("idHotel",id);
        return modelAndView;
     }
     @PostMapping("/addBoardBasis")
    public RedirectView addBoardBasisType(@RequestParam(name="idHotel") UUID id, @RequestParam(name = "boardBasisType") BoardBasisTypes type, RedirectAttributes attributes){
        hotelService.addBoardBasis(type,id);
        RedirectView redirectView = new RedirectView("/hotels/details");
        attributes.addAttribute("id",id);
        return  redirectView;
     }
     @GetMapping("/deleteBoardBasis")
     public RedirectView deleteBoardBasisType(@RequestParam(name="idHotel") UUID id, @RequestParam(name = "boardBasisType") BoardBasisTypes type, RedirectAttributes attributes){
         hotelService.deleteBoardBasis(type,id);
         RedirectView redirectView = new RedirectView("/hotels/details");
         attributes.addAttribute("id",id);
         return  redirectView;
     }
     @GetMapping("/detailsForUser")
    public ModelAndView getDetailsForUser(@RequestParam(name="id") UUID id){
         HotelDetailsDto hotel = hotelService.getById(id);
         ModelAndView modelAndView = new ModelAndView("hotelDetailsForUser");
         List<ReviewLightDto> reviewLightDtos = reviewService.getByHotel(id);
         modelAndView.addObject("hotel",hotel);
         modelAndView.addObject("reviews", reviewLightDtos);
         return modelAndView;
     }


}

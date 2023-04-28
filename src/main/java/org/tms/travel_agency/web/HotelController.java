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
import org.tms.travel_agency.dto.hotel.HotelDetailsDto;
import org.tms.travel_agency.dto.hotel.HotelLightDto;
import org.tms.travel_agency.dto.region.RegionLightDto;
import org.tms.travel_agency.services.HotelService;
import org.tms.travel_agency.services.RegionService;
import org.tms.travel_agency.validator.OnCreate;
import org.tms.travel_agency.validator.OnUpdate;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/hotels")
@AllArgsConstructor
public class HotelController {
    private final RegionService regionService;
    private final HotelService hotelService;

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
    public ModelAndView getHotelDetails(@RequestParam UUID uuid){
         HotelDetailsDto byId = hotelService.getById(uuid);
         ModelAndView modelAndView = new ModelAndView("hotelDetailsForAdmin");
         modelAndView.addObject("hotel", byId);
         return modelAndView;
     }

}

package org.tms.travel_agency.web;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.tms.travel_agency.dto.destination.DestinationLightDto;
import org.tms.travel_agency.dto.hotel.HotelLightDto;
import org.tms.travel_agency.dto.region.RegionDetailsDto;
import org.tms.travel_agency.dto.region.RegionLightDto;
import org.tms.travel_agency.services.DestinationService;
import org.tms.travel_agency.services.HotelService;
import org.tms.travel_agency.services.RegionService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/regionManager")
@AllArgsConstructor
public class RegionController {
    private final RegionService service;
    private final DestinationService destinationService;
    private final HotelService hotelService;

    @GetMapping()
    public ModelAndView getRegionManagerPage(){
        ModelAndView modelAndView = new ModelAndView("regionManager");
        RegionDetailsDto dto = new RegionDetailsDto();
        List<DestinationLightDto> destinations = destinationService.getAll();
        List<RegionLightDto> regions = service.getAll();
        modelAndView.addObject("newRegion", dto);
        modelAndView.addObject("destinations", destinations);
        modelAndView.addObject("regions", regions);
        return modelAndView;
    }
    @PostMapping()
    public String save(@Valid @ModelAttribute("newRegion") RegionDetailsDto dto, BindingResult result){
        if(result.hasErrors()){
            return "regionManager";
        }
         service.save(dto);
         return "redirect:/regionManager";
    }
    @PostMapping("/update")
    public String update(@Valid  @ModelAttribute("newRegion") RegionDetailsDto dto, BindingResult result){
        if(result.hasErrors()){
            return "regionManager";
        }
        service.update(dto);
        return"redirect:/regionManager";

    }
    @GetMapping("/delete")
    public String delete(@RequestParam UUID id){
        service.delete(id);
        return"redirect:/regionManager";
    }
    @GetMapping("/details")
    public ModelAndView getDetails(@RequestParam(name="id") UUID id){
        RegionDetailsDto region = service.getById(id);
        List<HotelLightDto> hotels = hotelService.getByRegionName(region.getName());
        ModelAndView modelAndView=new ModelAndView("regionDetails");
        modelAndView.addObject("region", region);
        modelAndView.addObject("hotels", hotels);
        return modelAndView;
    }




}

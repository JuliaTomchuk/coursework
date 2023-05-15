package org.tms.travel_agency.web;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.tms.travel_agency.domain.Destination;
import org.tms.travel_agency.dto.destination.DestinationDetailsDto;
import org.tms.travel_agency.dto.destination.DestinationLightDto;
import org.tms.travel_agency.dto.region.RegionLightDto;
import org.tms.travel_agency.exception.DuplicateDestinationException;
import org.tms.travel_agency.exception.NoSuchDestinationException;
import org.tms.travel_agency.services.DestinationService;
import org.tms.travel_agency.services.RegionService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@RequestMapping("/destinations")
public class DestinationController {

    private final DestinationService service;
    private final RegionService regionService;

    @GetMapping
    public ModelAndView getAllDestinations(){
        ModelAndView modelAndView = new ModelAndView("destinations");
        List<DestinationLightDto> destinationsList = service.getAll();
        modelAndView.addObject("allDestinations",destinationsList);
        return modelAndView;
    }
    @GetMapping("/destinationManager")
    public ModelAndView getDestinationManagerPage(@ModelAttribute(name = "newDestination") DestinationDetailsDto destinationDetailsDto){
        List<DestinationLightDto> destinationList = service.getAll();
        ModelAndView modelAndView = new ModelAndView("destinationManager");
        return modelAndView.addObject("destinationList",destinationList);
    }
    @PostMapping("/destinationManager")
    public String saveDestination(@Valid @ModelAttribute(name = "newDestination") DestinationDetailsDto destinationDetailsDto, BindingResult result){
        ModelAndView modelAndView = new ModelAndView("destinationManager");
        if(result.hasErrors()){
            return "destinationManager";
        }
        service.save(destinationDetailsDto);
        return "redirect:/destinations/destinationManager";
    }
    @GetMapping("/delete")
    public String deleteDestination( @RequestParam UUID id){
         service.delete(id);
        return "redirect:/destinations/destinationManager";
    }
    @PostMapping("/destinationManager/update")
    public String updateDestination(@Valid @ModelAttribute("newDestination") DestinationDetailsDto dto,BindingResult result){
        if(result.hasErrors()){
            return "destinationManager";
        }
        service.update(dto);
        return "redirect:/destinations/destinationManager";
    }
    @GetMapping("/details")
    public ModelAndView getDetails(@RequestParam(name="id") UUID id){
        DestinationDetailsDto destination = service.getById(id);
        List<RegionLightDto> regions = regionService.getByDestinationName(destination.getName());
        ModelAndView modelAndView = new ModelAndView("destinationDetails");
        modelAndView.addObject("destination",destination);
        modelAndView.addObject("regions", regions);
        return modelAndView;
    }



}

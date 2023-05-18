package org.tms.travel_agency.web;

import lombok.RequiredArgsConstructor;
import org.springframework.orm.hibernate5.SpringSessionContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.tms.travel_agency.domain.Role;
import org.tms.travel_agency.dto.destination.DestinationLightDto;
import org.tms.travel_agency.dto.region.RegionLightDto;
import org.tms.travel_agency.services.DestinationService;
import org.tms.travel_agency.services.RegionService;
import org.tms.travel_agency.services.UserService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class TravelAgencyController {
    private final RegionService regionService;
    private final DestinationService destinationService;
    private final UserService userService;

    @GetMapping
    public ModelAndView getHomePage(){
        boolean isAdmin = userService.isAdmin();
        ModelAndView modelAndView = new ModelAndView("home");
        List<DestinationLightDto> allDestinations = destinationService.getAll();
        modelAndView.addObject("isAdmin",isAdmin);
        modelAndView.addObject("allDestinations", allDestinations);
        return modelAndView;
    }
    @GetMapping("/allDestinations")
    public ModelAndView getAllDestinations(@RequestParam String type){
        Map<String, List<RegionLightDto>> regionsByDestinations = regionService.getRegionsByDestinations();
        ModelAndView modelAndView = new ModelAndView("allDestinations");
        boolean isAdmin = userService.isAdmin();
        modelAndView.addObject("regionsByDestinations",regionsByDestinations);
        modelAndView.addObject("type", type);
        modelAndView.addObject("isAdmin",isAdmin);
        return modelAndView;
    }
    @GetMapping("/adminPage")
    public ModelAndView getAdminPage(){
        ModelAndView modelAndView = new ModelAndView("adminPage");
        modelAndView.addObject("isAdmin",true);
        return  modelAndView;
    }
}

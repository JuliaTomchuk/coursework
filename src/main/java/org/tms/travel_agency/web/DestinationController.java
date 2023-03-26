package org.tms.travel_agency.web;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.tms.travel_agency.domain.Destination;
import org.tms.travel_agency.repository.DestinationRepository;
import org.tms.travel_agency.services.DestinationService;
import org.tms.travel_agency.services.DestinationServiceForAdmin;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Controller
@RequestMapping("/destinations")
public class DestinationController {

    private final DestinationServiceForAdmin destinationServiceForAdmin;

    @GetMapping
    public ModelAndView getAllDestinations(){
        ModelAndView modelAndView = new ModelAndView("allDestinations");
        List<Destination> all = destinationServiceForAdmin.getAll();
        modelAndView.addObject("destinationsList", all);
        return modelAndView;
    }
    @GetMapping("/addDestination")
    public ModelAndView addDestinationPage(@ModelAttribute(name = "newDestination") Destination destination){
        ModelAndView modelAndView = new ModelAndView("addDestination");
        List<Destination> all = destinationServiceForAdmin.getAll();
        return modelAndView.addObject("destinationList",all);
    }
    @PostMapping("/addDestination")
    public String saveDestination(Destination destination){
        Destination save = destinationServiceForAdmin.save(destination);
        return "redirect:/destinations/addDestination";


    }
    @GetMapping("/delete")
    public void deleteDestination(UUID uuid){
        destinationServiceForAdmin.delete(uuid);
    }
}

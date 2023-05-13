package org.tms.travel_agency.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.tms.travel_agency.dto.review.ReviewDetailsDto;
import org.tms.travel_agency.services.ReviewService;

import javax.validation.Valid;
import java.util.UUID;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService service;

    @GetMapping("/details")
    public ModelAndView getDetails(@RequestParam(name="id")UUID id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().contains("ROLE_ADMIN");
        ReviewDetailsDto review = service.getById(id);
        ModelAndView modelAndView = new ModelAndView("reviewDetails");
        modelAndView.addObject("review",review);
        if(isAdmin||name.equals(review.getUsername())){
            modelAndView= new ModelAndView("reviewDetailsEditable");
            modelAndView.addObject("review", review);
            return modelAndView;
        }

        return modelAndView;
    }
    @GetMapping("/save")
    public ModelAndView getAddReviewPage(@RequestParam(name="id") UUID idHotel){
        ModelAndView modelAndView = new ModelAndView("addReview");
        ReviewDetailsDto reviewDetailsDto = new ReviewDetailsDto();
        reviewDetailsDto.setHotelId(idHotel);
        modelAndView.addObject("review",reviewDetailsDto);
        return modelAndView;
    }
    @PostMapping("/save")
    public String addReview(@Valid @ModelAttribute(name = "review") ReviewDetailsDto reviewDetailsDto, BindingResult result, RedirectAttributes attributes){

        if(result.hasErrors()){
            return "addReview";
        }
        service.save(reviewDetailsDto);
        attributes.addAttribute("id",reviewDetailsDto.getHotelId());
        return "redirect:/hotels/detailsForUser";
    }
    @GetMapping("/delete")
    public String delete (@RequestParam(name="idReview") UUID idReview, @RequestParam(name="idHotel") UUID idHotel, RedirectAttributes attributes){
        service.delete(idReview);
         attributes.addAttribute("id", idHotel);
        return "redirect:/hotels/detailsForUser";
    }

    @PostMapping("/update")
    public String update (@Valid @ModelAttribute("review") ReviewDetailsDto dto,BindingResult result,  RedirectAttributes attributes){
         if(result.hasErrors()){
          return "reviewDetailsEditable";
        }
        service.update(dto);
        attributes.addAttribute("id", dto.getId());
        return "redirect:/review/details";
    }
}

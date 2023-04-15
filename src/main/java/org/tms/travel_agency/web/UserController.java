package org.tms.travel_agency.web;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.tms.travel_agency.dto.user.UserFullDescriptionDto;
import org.tms.travel_agency.services.UserService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService service;


    @GetMapping("/login")
    public ModelAndView getLoginPage(){
        ModelAndView modelAndView = new ModelAndView("/login");
        return modelAndView;
    }
    @GetMapping("/registration")
    public ModelAndView getRegistrationPage(@ModelAttribute(name = "newUser") UserFullDescriptionDto userDto){
        ModelAndView modelAndView = new ModelAndView("registration");
        return modelAndView;
    }
    @PostMapping("/registration")
    public String save(UserFullDescriptionDto dto){
         service.save(dto);
        return "redirect:/user/login";
    }

    @GetMapping("/update")
    public ModelAndView getUpdatePage(){
        UserFullDescriptionDto current = service.getCurrent();
        ModelAndView modelAndView = new ModelAndView("/update");
        modelAndView.addObject("currentUser",current);
        return modelAndView;
    }

    @PostMapping("/update")
    public String update(UserFullDescriptionDto userDto){
        service.update(userDto);
        return "redirect:/user/update";

    }
    @GetMapping("/delete")
    public String delete(){
        UserFullDescriptionDto current = service.getCurrent();
        service.delete(current.getId());
        return  "redirect:/user/registration";
     }
     @GetMapping("/accountManager")
     public ModelAndView getAccountManager(){
        UserFullDescriptionDto current = service.getCurrent();
         ModelAndView modelAndView = new ModelAndView("accountManager");
         modelAndView.addObject("currentUser",current);
         return modelAndView;

     }
//     @GetMapping("/userManager")
//    public ModelAndView getAllUsers(){
//        service.
//     }





}

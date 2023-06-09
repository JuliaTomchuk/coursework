package org.tms.travel_agency.web;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.tms.travel_agency.dto.user.UserFullDescriptionDto;
import org.tms.travel_agency.dto.user.UserLightDescriptionDto;
import org.tms.travel_agency.services.UserService;


import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {


    private final  UserService service;


    @GetMapping("/login")
    public ModelAndView getLoginPage() {
        return new ModelAndView("/login");

    }

    @GetMapping("/registration")
    public ModelAndView getRegistrationPage(@ModelAttribute(name = "newUser") UserFullDescriptionDto userDto) {
       return new ModelAndView("/registration");

    }

    @PostMapping("/registration")
    public ModelAndView save(@Valid @ModelAttribute(name = "newUser") UserFullDescriptionDto dto, BindingResult result) {
        ModelAndView modelAndView;
        if (result.hasErrors()) {
            modelAndView = new ModelAndView("/registration");
            return modelAndView;
        }
        modelAndView = new ModelAndView("/login");
        service.save(dto);

        return modelAndView;
    }

    @GetMapping("/update")
    public ModelAndView getUpdatePage() {
        UserFullDescriptionDto current = service.getCurrent();
        ModelAndView modelAndView = new ModelAndView("/update");
        modelAndView.addObject("currentUser", current);
        boolean isAdmin= service.isAdmin();
        modelAndView.addObject("isAdmin",isAdmin);
        return modelAndView;
    }

    @PostMapping("/update")
    public ModelAndView update(@Valid @ModelAttribute("currentUser") UserFullDescriptionDto userDto, BindingResult result) {
         if (result.hasErrors()) {
            return new ModelAndView("/update");
        }
        service.update(userDto);
        return new ModelAndView("/update");

    }

    @GetMapping("/delete")
    public String delete() {
        UserFullDescriptionDto current = service.getCurrent();
        service.delete(current.getId());
        return "redirect:/user/registration";
    }

    @GetMapping("/accountManager")
    public ModelAndView getAccountManager() {
        UserFullDescriptionDto current = service.getCurrent();
        ModelAndView modelAndView = new ModelAndView("/accountManager");
        boolean isAdmin= service.isAdmin();
        modelAndView.addObject("currentUser", current);
        modelAndView.addObject("isAdmin", isAdmin);
        return modelAndView;

    }

    @GetMapping("/usersManager")
    public ModelAndView getAllUsers() {
        List<UserLightDescriptionDto> users = service.getAll();
        ModelAndView modelAndView = new ModelAndView("/usersManager");
        modelAndView.addObject("users", users);
        modelAndView.addObject("isAdmin", true);
        return modelAndView;

    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") @NotNull UUID id) {
        service.delete(id);
        return "redirect:/user/usersManager";
    }

    @GetMapping("/changeRole")
    public String changeRole(@RequestParam(name = "role") @NotBlank String role, @RequestParam(name = "id") @NotBlank UUID id) {
        service.changeRole(role, id);
        return "redirect:/user/usersManager ";
    }



}

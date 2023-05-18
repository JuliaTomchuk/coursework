package org.tms.travel_agency.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.tms.travel_agency.domain.TourProduct;
import org.tms.travel_agency.exception.NoSuchServiceException;
import org.tms.travel_agency.services.CartService;
import org.tms.travel_agency.services.TourProductService;
import org.tms.travel_agency.services.UserService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final List<TourProductService> services;
    private final UserService userService;

    @GetMapping()
    public ModelAndView getAll() {
        List<TourProduct> products = cartService.preview();
        ModelAndView modelAndView = new ModelAndView("cart");
        boolean isAdmin = userService.isAdmin();
        modelAndView.addObject("products", products);
        modelAndView.addObject("isAdmin",isAdmin);
        return modelAndView;
    }

    @GetMapping("/book")
    public String book(@RequestParam(name = "id") UUID id, @RequestParam(name = "type") String type) {
        TourProductService tourProductService = services.stream().filter(service -> service.getClass().getName().contains(type)).findFirst().orElseThrow( ()->new NoSuchServiceException("no service for " + type));
        cartService.book(tourProductService, id);
        return "redirect:/cart";
    }

    @GetMapping("/deleteFromCart")
    public String deleteFromCart(@RequestParam(name = "id") UUID id, @RequestParam(name = "type") String type) {
        TourProductService tourProductService = services.stream().filter(service -> service.getClass().getName().contains(type)).findFirst().orElseThrow( ()->new NoSuchServiceException("no service for " + type));
        cartService.deleteFromCart(tourProductService, id);

        return "redirect:/cart";
    }

    @GetMapping("/cancelBooking")
    public String cancelBooking(@RequestParam(name = "idCart") UUID idCart, @RequestParam(name = "type") String type, @RequestParam(name = "idProduct") UUID idProduct) {
        TourProductService tourProductService = services.stream().filter(service -> service.getClass().getName().contains(type)).findFirst().orElseThrow( ()->new NoSuchServiceException("no service for " + type));
        cartService.cancelBooking(idCart, idProduct, tourProductService);
        return "redirect:/cart/allBooking";
    }

    @GetMapping("/allBooking")
    public ModelAndView getAllBooking() {
        Map<UUID, List<TourProduct>> allBooking = cartService.getAllBookings();
        ModelAndView modelAndView = new ModelAndView("bookingManager");
        boolean isAdmin= userService.isAdmin();
        modelAndView.addObject("allBooking", allBooking);
        modelAndView.addObject("isAdmin",isAdmin);
        return modelAndView;
    }

}

package org.tms.travel_agency.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.tms.travel_agency.domain.TourProduct;
import org.tms.travel_agency.dto.room.RoomDetailsDto;
import org.tms.travel_agency.services.CartService;
import org.tms.travel_agency.services.RoomService;
import org.tms.travel_agency.services.TourProductService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final RoomService roomService;

    @GetMapping()
    public ModelAndView getAll(){
        List<TourProduct> products = cartService.preview();
        ModelAndView modelAndView = new ModelAndView("cart");
        modelAndView.addObject("products", products);
        return modelAndView;
    }
    @GetMapping("/book")
    public String book(@RequestParam(name="id") UUID id, @RequestParam(name="type") String type){
        if(type.equals("room")) {
            cartService.book(roomService, id);
        }
        return "redirect:/cart";
    }

    @GetMapping("/deleteFromCart")
    public String deleteFromCart(@RequestParam(name="id") UUID id, @RequestParam(name="type") String type){
        if(type.equals("room")) {
            cartService.deleteFromCart(roomService, id);
        }
        return "redirect:/cart";
    }

    @GetMapping("/cancelBooking")
    public String cancelBooking(@RequestParam (name="idCart") UUID idCart, @RequestParam(name="type")String type, @RequestParam(name="idProduct") UUID idProduct){
        if(type.equals("room")) {
            cartService.cancelBooking(idCart, idProduct, roomService);
        }
        return "redirect:/cart/allBooking";
    }
    @GetMapping("/allBooking")
    public ModelAndView getAllBooking(){
        Map<UUID, List<TourProduct>> allBooking = cartService.getAllBookings();
        ModelAndView modelAndView = new ModelAndView("bookingManager");
        modelAndView.addObject("allBooking",allBooking);
        return modelAndView;
    }



}

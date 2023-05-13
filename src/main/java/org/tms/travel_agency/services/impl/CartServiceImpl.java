package org.tms.travel_agency.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.tms.travel_agency.domain.Cart;
import org.tms.travel_agency.domain.TourProduct;
import org.tms.travel_agency.repository.CartRepository;
import org.tms.travel_agency.services.CartService;
import org.tms.travel_agency.services.RoomService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final RoomService roomService;
   private final CartRepository cartRepository;
    @Override
    @Transactional
    public List<TourProduct> preview() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Cart> cartOptional = cartRepository.findByUserUsername(name);
        if(cartOptional.isEmpty()){
            return new ArrayList<>();
        }
        Cart cart = cartOptional.get();
        List<TourProduct> tourProductList = cart.getTourProductList();

        return tourProductList;

    }

    @Override
    public void book(UUID id, String type) {
        switch (type){
            case "room"-> roomService.book(id);
        }
    }

    @Override
    public void deleteFromCart(UUID id, String type) {
        switch (type){
            case "room"-> roomService.deleteFromCart(id);
        }
    }

    @Override
    public Map<UUID,List<TourProduct>> getAllBookings() {
        Map<UUID,List<TourProduct>> allBooking = new HashMap<>();
        cartRepository.findAll().stream().forEach(cart -> {allBooking.put(cart.getId(),cart.getTourProductList());});
        return allBooking;
    }

    @Override
    public void cancelBooking(UUID idCart, UUID idProduct, String type) {
        switch (type){
            case "room"->roomService.cancelBooking(idCart, idProduct);
        }
    }
}

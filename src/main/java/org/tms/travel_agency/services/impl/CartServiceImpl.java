package org.tms.travel_agency.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.tms.travel_agency.domain.TourProduct;
import org.tms.travel_agency.repository.CartRepository;
import org.tms.travel_agency.services.CartService;
import org.tms.travel_agency.services.RoomService;
import org.tms.travel_agency.services.TourProductService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    @Override
    @Transactional
    public List<TourProduct> preview() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return cartRepository.findByUserUsername(name).map(cart -> cart.getTourProductList()).orElse(new ArrayList<>());
    }

    @Override
    public void book(TourProductService tourProductService,UUID id) {
       tourProductService.book(id);
    }

    @Override
    public void deleteFromCart(TourProductService tourProductService,UUID id) {
       tourProductService.deleteFromCart(id);
    }

    @Override
    public Map<UUID, List<TourProduct>> getAllBookings() {
        Map<UUID, List<TourProduct>> allBooking = new HashMap<>();
        cartRepository.findAll().stream().forEach(cart -> {
            allBooking.put(cart.getId(), cart.getTourProductList());
        });
        return allBooking;
    }

    @Override
    public void cancelBooking(UUID idCart, UUID idProduct, TourProductService tourProductService) {
        tourProductService.cancelBooking(idCart, idProduct);

    }
}

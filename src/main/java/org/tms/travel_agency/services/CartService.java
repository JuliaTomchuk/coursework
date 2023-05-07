package org.tms.travel_agency.services;

import org.tms.travel_agency.domain.TourProduct;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CartService {
    List<TourProduct> preview();
    void book(UUID id, String type);
    void deleteFromCart(UUID id,String type);
    Map<UUID,List<TourProduct>> getAllBookings();
    void cancelBooking(UUID idCart, UUID idProduct,String type);
}

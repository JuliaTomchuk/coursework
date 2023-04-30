package org.tms.travel_agency.services;

import org.tms.travel_agency.domain.TourProduct;

import java.util.List;
import java.util.UUID;

public interface BookingService <T,V> {

    T book (UUID idUser, UUID id);
    List<V> bookAll(UUID idUser, List<UUID> ids);
    int getPrice(UUID id);
    void addToCart(UUID id, UUID idUser);
    List<TourProduct> cartPreview(UUID idUser);
    void deleteFromCart(UUID id);
    void cancelBooking(UUID id);
    List<V> search(T t);

}

package org.tms.travel_agency.services;

import org.tms.travel_agency.domain.TourProduct;
import org.tms.travel_agency.dto.room.RoomDetailsDto;

import java.util.List;
import java.util.UUID;

public interface BookingService <T,V> {

    void book ( UUID id);

    void deleteFromCart(UUID id);
    void cancelBooking( UUID idCart,UUID idProduct);

    void prebook (RoomDetailsDto dto);
    List<V> search(T t);



}

package org.tms.travel_agency.services;

import org.tms.travel_agency.domain.Hotel;

import java.util.UUID;

public interface HotelServiceForAdmin {
    Hotel save(Hotel hotel);
    void delete(UUID id);
    Hotel update(Hotel hotel);


}

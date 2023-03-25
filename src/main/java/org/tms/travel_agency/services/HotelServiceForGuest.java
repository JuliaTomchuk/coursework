package org.tms.travel_agency.services;

import org.tms.travel_agency.domain.Destination;
import org.tms.travel_agency.domain.Hotel;
import org.tms.travel_agency.domain.HotelTypeByStars;
import org.tms.travel_agency.domain.HotelTypeByTargetMarket;
import org.tms.travel_agency.domain.Region;
import org.tms.travel_agency.domain.Review;

import java.util.Set;
import java.util.UUID;

public interface HotelServiceForGuest {

    Set<Hotel> searchHotels(Destination destination, Region region, HotelTypeByStars hotelTypeByStars, HotelTypeByTargetMarket  hotelTypeByTargetMarket, String name);
    String getDescription(Integer id);
    Set<Review> getAllReview(UUID idHotel);


}

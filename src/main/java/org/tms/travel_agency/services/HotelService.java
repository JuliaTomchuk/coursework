package org.tms.travel_agency.services;

import org.tms.travel_agency.domain.Destination;
import org.tms.travel_agency.domain.Hotel;
import org.tms.travel_agency.domain.HotelTypeByStars;
import org.tms.travel_agency.domain.HotelTypeByTargetMarket;
import org.tms.travel_agency.domain.Region;

import java.util.Set;

public interface HotelService {

    Set<Hotel> searchHotels(Destination destination, Region region, HotelTypeByStars hotelTypeByStars, HotelTypeByTargetMarket  hotelTypeByTargetMarket, String name);

}

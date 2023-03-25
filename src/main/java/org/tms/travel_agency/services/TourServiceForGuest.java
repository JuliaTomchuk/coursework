package org.tms.travel_agency.services;

import org.tms.travel_agency.domain.Tour;
import org.tms.travel_agency.domain.User;

import java.util.UUID;

public interface TourServiceForGuest {
    Integer calculatePrice(Tour tour);
    void addTourToCart(Tour tour, User user);
    void deleteTourFromCart(UUID id);
}

package org.tms.travel_agency.services;

import org.tms.travel_agency.domain.Tour;

public interface TourServiceForUser {
    Integer calculatePrice(Tour tour);
    void bookTour(Tour tour);

}

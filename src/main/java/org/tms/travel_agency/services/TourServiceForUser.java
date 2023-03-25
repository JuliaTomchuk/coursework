package org.tms.travel_agency.services;

import org.tms.travel_agency.domain.Tour;

import java.util.UUID;

public interface TourServiceForUser {

    void bookTour(UUID id);
    Tour getBookedTour(UUID id);

}

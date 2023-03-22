package org.tms.travel_agency.services;

import org.tms.travel_agency.domain.Tour;

public interface TourServiceForAdmin {

    boolean cancelBooking(Tour tour);
}

package org.tms.travel_agency.services;

import org.tms.travel_agency.domain.Tour;

import java.util.UUID;

public interface TourServiceForAdmin {

    boolean cancelBooking(UUID id);
}

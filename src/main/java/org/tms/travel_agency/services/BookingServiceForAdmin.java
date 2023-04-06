package org.tms.travel_agency.services;

import java.util.UUID;

public interface BookingServiceForAdmin<T> extends BookingService<T>{

    void cancelBooking(UUID id);
}

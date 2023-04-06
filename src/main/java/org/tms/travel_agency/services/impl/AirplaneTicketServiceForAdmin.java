package org.tms.travel_agency.services.impl;

import org.tms.travel_agency.domain.AirplaneTicket;
import org.tms.travel_agency.services.BookingServiceForAdmin;
import org.tms.travel_agency.services.TravelAdminService;

import java.util.UUID;

public class AirplaneTicketServiceForAdmin extends AirplaneTicketService implements TravelAdminService<AirplaneTicket>, BookingServiceForAdmin<AirplaneTicket> {
    @Override
    public void cancelBooking(UUID id) {

    }

    @Override
    public AirplaneTicket save(AirplaneTicket airplaneTicket) {
        return null;
    }

    @Override
    public AirplaneTicket update(AirplaneTicket airplaneTicket) {
        return null;
    }

    @Override
    public void delete(UUID id) {

    }
}

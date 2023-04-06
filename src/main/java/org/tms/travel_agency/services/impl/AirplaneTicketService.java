package org.tms.travel_agency.services.impl;

import org.tms.travel_agency.domain.AirplaneTicket;
import org.tms.travel_agency.domain.TourProduct;
import org.tms.travel_agency.services.BookingService;
import org.tms.travel_agency.services.TravelUserService;

import java.util.List;
import java.util.UUID;

public class AirplaneTicketService implements TravelUserService<AirplaneTicket>, BookingService<AirplaneTicket> {
    @Override
    public List<AirplaneTicket> getAll() {
        return null;
    }

    @Override
    public AirplaneTicket getById() {
        return null;
    }

    @Override
    public List<AirplaneTicket> search(AirplaneTicket airplaneTicket) {
        return null;
    }

    @Override
    public AirplaneTicket book(UUID idUser, UUID id) {
        return null;
    }

    @Override
    public List<AirplaneTicket> bookAll(UUID idUser, List<UUID> ids) {
        return null;
    }

    @Override
    public int getPrice(UUID id) {
        return 0;
    }

    @Override
    public void addToCart(UUID id, UUID idUser) {

    }

    @Override
    public List<TourProduct> cartPreview(UUID idUser) {
        return null;
    }

    @Override
    public void deleteFromCart(UUID id) {

    }


}

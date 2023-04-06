package org.tms.travel_agency.services.impl;

import org.tms.travel_agency.domain.Room;
import org.tms.travel_agency.domain.TourProduct;
import org.tms.travel_agency.services.BookingService;
import org.tms.travel_agency.services.TravelUserService;

import java.util.List;
import java.util.UUID;

public class RoomService implements TravelUserService<Room>, BookingService<Room> {
    @Override
    public List<Room> getAll() {
        return null;
    }

    @Override
    public Room getById() {
        return null;
    }

    @Override
    public List<Room> search(Room room) {
        return null;
    }

    @Override
    public Room book(UUID idUser, UUID id) {
        return null;
    }

    @Override
    public List<Room> bookAll(UUID idUser, List<UUID> ids) {
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

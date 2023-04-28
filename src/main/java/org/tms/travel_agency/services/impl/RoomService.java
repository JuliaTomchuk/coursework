package org.tms.travel_agency.services.impl;

import org.tms.travel_agency.domain.Room;
import org.tms.travel_agency.domain.TourProduct;


import java.util.List;
import java.util.UUID;

public class RoomService  {

    public List<Room> getAll() {
        return null;
    }


    public Room getById() {
        return null;
    }


    public List<Room> search(Room room) {
        return null;
    }


    public Room book(UUID idUser, UUID id) {
        return null;
    }


    public List<Room> bookAll(UUID idUser, List<UUID> ids) {
        return null;
    }


    public int getPrice(UUID id) {
        return 0;
    }


    public void addToCart(UUID id, UUID idUser) {

    }


    public List<TourProduct> cartPreview(UUID idUser) {
        return null;
    }


    public void deleteFromCart(UUID id) {

    }
}

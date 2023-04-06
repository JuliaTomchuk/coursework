package org.tms.travel_agency.services.impl;

import org.tms.travel_agency.domain.BoardBasis;
import org.tms.travel_agency.domain.Room;
import org.tms.travel_agency.services.BookingServiceForAdmin;
import org.tms.travel_agency.services.OneComponentJoin;
import org.tms.travel_agency.services.TravelAdminService;

import java.util.List;
import java.util.UUID;

public class RoomServiceForAdmin extends RoomService implements TravelAdminService<Room>, BookingServiceForAdmin<Room>, OneComponentJoin<Room, BoardBasis> {

    @Override
    public Room save(Room room) {
        return null;
    }

    @Override
    public Room update(Room room) {
        return null;
    }

    @Override
    public void delete(UUID id) {

    }

    @Override
    public List<Room> search(Room room) {
        return null;
    }


    @Override
    public void cancelBooking(UUID id) {

    }


    @Override
    public void addComponent(UUID idObject, UUID idComponent) {

    }
}

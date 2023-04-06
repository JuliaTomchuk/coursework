package org.tms.travel_agency.services.impl;

import org.tms.travel_agency.domain.BoardBasis;
import org.tms.travel_agency.domain.Hotel;
import org.tms.travel_agency.domain.Review;
import org.tms.travel_agency.domain.Room;
import org.tms.travel_agency.services.TravelAdminService;
import org.tms.travel_agency.services.ThreeComponentJoin;

import java.util.UUID;

public class HotelServiceForAdmin extends HotelService implements TravelAdminService<Hotel>, ThreeComponentJoin<Hotel,Room, BoardBasis, Review> {
    @Override
    public Hotel save(Hotel hotel) {
        return null;
    }

    @Override
    public Hotel update(Hotel hotel) {
        return null;
    }

    @Override
    public void delete(UUID id) {

    }




    @Override
    public void addFirstComponent(UUID idObject, UUID idComponent) {

    }

    @Override
    public void addSecondComponent(UUID idObject, UUID idComponent) {

    }

    @Override
    public void addThirdComponent(UUID idObject, UUID idComponent) {

    }
}

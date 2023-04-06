package org.tms.travel_agency.services.impl;

import org.tms.travel_agency.domain.Hotel;
import org.tms.travel_agency.domain.Review;
import org.tms.travel_agency.services.OneComponentJoin;
import org.tms.travel_agency.services.TravelUserService;

import java.util.List;
import java.util.UUID;

public class HotelService implements TravelUserService<Hotel>, OneComponentJoin<Hotel, Review> {
    @Override
    public List<Hotel> getAll() {
        return null;
    }

    @Override
    public Hotel getById() {
        //подробный просмотр с описанием и отзывами
        return null;
    }

    @Override
    public List<Hotel> search(Hotel hotel) {
        // вернуть список названий отелей
        return null;
    }

    @Override
    public void addComponent(UUID idObject, UUID idComponent) {

    }
}

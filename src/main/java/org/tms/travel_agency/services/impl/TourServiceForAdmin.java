package org.tms.travel_agency.services.impl;

import org.tms.travel_agency.domain.RoundTrip;
import org.tms.travel_agency.domain.Tour;
import org.tms.travel_agency.services.OneComponentJoin;
import org.tms.travel_agency.services.TravelAdminService;

import java.util.List;
import java.util.UUID;

public class TourServiceForAdmin extends TourService implements TravelAdminService<Tour>, OneComponentJoin<Tour, RoundTrip> {
    @Override
    public Tour save(Tour tour) {
        return null;
    }

    @Override
    public Tour update(Tour tour) {
        return null;
    }

    @Override
    public void delete(UUID id) {

    }

    @Override
    public List<Tour> search(Tour tour) {
        return null;
    }

    @Override
    public void addComponent(UUID idObject, UUID idComponent) {

    }
}

package org.tms.travel_agency.services.impl;

import org.tms.travel_agency.domain.Hotel;
import org.tms.travel_agency.domain.OneWayFlight;
import org.tms.travel_agency.domain.Region;
import org.tms.travel_agency.domain.RoundTrip;
import org.tms.travel_agency.domain.Tour;
import org.tms.travel_agency.services.FourComponentJoin;
import org.tms.travel_agency.services.TravelAdminService;

import java.util.UUID;

public class RegionServiceForAdmin extends RegionService implements TravelAdminService<Region>, FourComponentJoin<Region,Hotel, Tour, OneWayFlight, RoundTrip> {

    @Override
    public Region save(Region region) {
        return null;
    }

    @Override
    public Region update(Region region) {
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

    @Override
    public void addFourthComponent(UUID idObject, UUID idComponent) {

    }
}

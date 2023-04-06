package org.tms.travel_agency.services.impl;

import org.tms.travel_agency.domain.RoundTrip;
import org.tms.travel_agency.services.TravelAdminService;

import java.util.UUID;

public class RoundTripServiceForAdmin extends RoundTripService implements TravelAdminService<RoundTrip> {

    @Override
    public RoundTrip save(RoundTrip roundTrip) {
        return null;
    }

    @Override
    public RoundTrip update(RoundTrip roundTrip) {
        return null;
    }

    @Override
    public void delete(UUID id) {

    }
}


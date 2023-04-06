package org.tms.travel_agency.services.impl;

import org.tms.travel_agency.domain.AirplaneTicket;
import org.tms.travel_agency.domain.OneWayFlight;
import org.tms.travel_agency.services.TravelAdminService;
import org.tms.travel_agency.services.OneComponentJoin;

import java.util.UUID;

public class OneWayFlightServiceForAdmin extends OneWayFlightService implements TravelAdminService<OneWayFlight>, OneComponentJoin<OneWayFlight, AirplaneTicket> {
    @Override
    public OneWayFlight save(OneWayFlight oneWayFlight) {
        return null;
    }

    @Override
    public OneWayFlight update(OneWayFlight oneWayFlight) {
        return null;
    }

    @Override
    public void delete(UUID id) {

    }


    @Override
    public void addComponent(UUID idObject, UUID idComponent) {

    }
}

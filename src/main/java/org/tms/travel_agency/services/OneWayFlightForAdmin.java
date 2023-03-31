package org.tms.travel_agency.services;

import org.tms.travel_agency.domain.OneWayFlight;

import java.util.UUID;

public interface OneWayFlightForAdmin {
    boolean save(OneWayFlight flight);
    boolean delete(UUID id);
    boolean update(OneWayFlight flight);
}

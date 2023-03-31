package org.tms.travel_agency.services;

import org.tms.travel_agency.domain.OneWayFlight;

import java.time.LocalDateTime;
import java.util.List;

public interface OneWayFlightService {
    List<OneWayFlight> searchFlight(LocalDateTime departureTime,LocalDateTime arrivalTime,String departureAirport, String arrivalAirport);

}

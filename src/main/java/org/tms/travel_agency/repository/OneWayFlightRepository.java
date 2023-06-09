package org.tms.travel_agency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tms.travel_agency.domain.OneWayFlight;

import java.util.UUID;

public interface OneWayFlightRepository extends JpaRepository<OneWayFlight, UUID> {
}

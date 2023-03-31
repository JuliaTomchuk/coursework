package org.tms.travel_agency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tms.travel_agency.domain.AirplaneSeat;

import java.util.UUID;

public interface AirplaneSeatRepository extends JpaRepository<AirplaneSeat, UUID> {
}

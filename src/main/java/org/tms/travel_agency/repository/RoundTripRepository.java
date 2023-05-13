package org.tms.travel_agency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tms.travel_agency.domain.RoundTrip;

import java.util.UUID;

public interface RoundTripRepository extends JpaRepository<RoundTrip, UUID> {
}

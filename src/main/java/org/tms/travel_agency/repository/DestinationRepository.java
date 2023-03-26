package org.tms.travel_agency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tms.travel_agency.domain.Destination;

import java.util.UUID;

public interface DestinationRepository extends JpaRepository<Destination, UUID> {

    Destination findByNameIgnoreCase(String name);
}

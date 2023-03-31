package org.tms.travel_agency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tms.travel_agency.domain.Region;

import java.util.UUID;

public interface RegionRepository extends JpaRepository<Region, UUID> {
}

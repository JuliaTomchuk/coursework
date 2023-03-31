package org.tms.travel_agency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tms.travel_agency.domain.Tour;

import java.util.UUID;

public interface TourRepository extends JpaRepository<Tour, UUID>{
}

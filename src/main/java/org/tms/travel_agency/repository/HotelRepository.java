package org.tms.travel_agency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tms.travel_agency.domain.Hotel;

import java.util.UUID;

public interface HotelRepository extends JpaRepository<Hotel, UUID> {
}

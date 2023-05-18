package org.tms.travel_agency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tms.travel_agency.domain.Review;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    List<Review> findByHotelId(UUID id);
}

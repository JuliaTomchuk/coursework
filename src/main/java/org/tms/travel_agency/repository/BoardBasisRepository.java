package org.tms.travel_agency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tms.travel_agency.domain.BoardBasis;

import java.util.UUID;

public interface BoardBasisRepository extends JpaRepository<BoardBasis, UUID> {
}

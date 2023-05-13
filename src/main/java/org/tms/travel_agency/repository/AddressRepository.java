package org.tms.travel_agency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tms.travel_agency.domain.Address;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
}

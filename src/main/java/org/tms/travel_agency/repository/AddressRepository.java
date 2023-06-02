package org.tms.travel_agency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.tms.travel_agency.domain.Address;

import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {

    @Query(value = "select a from Address a where a.city=:city and a.street=:street and a.home=:home")
    Optional<Address> findIdentical(String city,String street, String home );
}

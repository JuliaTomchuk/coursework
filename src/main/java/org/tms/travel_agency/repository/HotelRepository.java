package org.tms.travel_agency.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.tms.travel_agency.domain.Address;
import org.tms.travel_agency.domain.Hotel;
import org.tms.travel_agency.domain.Region;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HotelRepository extends JpaRepository<Hotel, UUID>, JpaSpecificationExecutor<Hotel> {

    @Override
    List<Hotel> findAll(Specification<Hotel> spec);


    Optional<Hotel> findByAddress_City_AndAddress_Street_AndAddress_Home_AndRegion_Name(String city,String street,String home,String regionName );
}

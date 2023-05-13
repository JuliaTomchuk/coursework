package org.tms.travel_agency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tms.travel_agency.domain.Cart;

import java.util.Optional;
import java.util.UUID;


public interface CartRepository extends JpaRepository<Cart, UUID> {

    Optional<Cart> findByUserUsername(String username);
}

package org.tms.travel_agency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tms.travel_agency.domain.User;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    User findByUsername(String username);
}

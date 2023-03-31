package org.tms.travel_agency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tms.travel_agency.domain.Room;

import java.util.UUID;

public interface RoomRepository extends JpaRepository<Room, UUID> {
}

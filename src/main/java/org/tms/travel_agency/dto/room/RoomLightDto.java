package org.tms.travel_agency.dto.room;

import org.tms.travel_agency.domain.RoomTypesByOccupancy;
import org.tms.travel_agency.domain.RoomTypesByView;

import java.util.UUID;

public class RoomLightDto {
    private UUID id;
    private RoomTypesByOccupancy typesByOccupancy;
    private RoomTypesByView typesByView;
}

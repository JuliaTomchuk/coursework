package org.tms.travel_agency.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tms.travel_agency.domain.RoomTypesByOccupancy;
import org.tms.travel_agency.domain.RoomTypesByView;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoomLightDto {
    private UUID id;
    private RoomTypesByOccupancy typesByOccupancy;
    private RoomTypesByView typesByView;
}

package org.tms.travel_agency.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tms.travel_agency.domain.BoardBasisTypes;
import org.tms.travel_agency.domain.RoomTypesByOccupancy;
import org.tms.travel_agency.domain.RoomTypesByView;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoomLightDto {
    private UUID id;
    private Integer number;
    private String hotelName;
    private String destination;
    private RoomTypesByOccupancy typesByOccupancy;
    private RoomTypesByView typesByView;
    private Integer numOfTourist;
    private BigDecimal price;
    private BoardBasisTypes boardBases;
    private boolean booked;
    private boolean preBooked;

}

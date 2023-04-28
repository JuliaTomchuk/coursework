package org.tms.travel_agency.dto.tour;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tms.travel_agency.dto.room.RoomDetailsDto;
import org.tms.travel_agency.dto.round_trip.RoundTripFullDescriptionDto;

import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourFullDescriptionDto {
    private UUID id;
    private Long bookingNumber;
    private Integer numOfTourist;
    private RoundTripFullDescriptionDto roundTrip;
    private RoomDetailsDto room;
    private String region;


}

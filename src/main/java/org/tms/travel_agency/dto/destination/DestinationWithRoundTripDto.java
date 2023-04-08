package org.tms.travel_agency.dto.destination;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tms.travel_agency.dto.round_trip.RoundTripLightDto;

import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DestinationWithRoundTripDto {
    private Set<RoundTripLightDto> roundTripSet;
}

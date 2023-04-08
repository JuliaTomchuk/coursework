package org.tms.travel_agency.dto.destination;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tms.travel_agency.dto.one_way_flight.OneWayFlightLightDto;

import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DestinationWithOneWayFlight extends DestinationLightDto{
    private Set <OneWayFlightLightDto> onewayFlightDto;
}

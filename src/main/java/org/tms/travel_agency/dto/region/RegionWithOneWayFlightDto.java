package org.tms.travel_agency.dto.region;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tms.travel_agency.domain.OneWayFlight;

import java.util.HashSet;
import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionWithOneWayFlightDto extends RegionLightDto{
    private Set<OneWayFlight> oneWayFlightSet = new HashSet<>();
}

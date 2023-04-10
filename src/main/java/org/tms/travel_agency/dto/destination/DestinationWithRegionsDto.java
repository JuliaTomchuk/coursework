package org.tms.travel_agency.dto.destination;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tms.travel_agency.dto.region.RegionLightDto;

import java.util.HashSet;
import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DestinationWithRegionsDto extends DestinationLightDto{
    private Set<RegionLightDto> regions = new HashSet<>();
}

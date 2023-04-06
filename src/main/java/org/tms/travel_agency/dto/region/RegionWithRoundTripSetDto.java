package org.tms.travel_agency.dto.region;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tms.travel_agency.domain.RoundTrip;

import java.util.HashSet;
import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionWithRoundTripSetDto extends RegionLightDto {
    private Set<RoundTrip> roundTripSet = new HashSet<>();
}

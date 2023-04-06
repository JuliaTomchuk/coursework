package org.tms.travel_agency.dto.region;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tms.travel_agency.domain.Tour;

import java.util.HashSet;
import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionWithTourSetDto extends RegionLightDto{

    private Set<Tour> tours = new HashSet<>();
}

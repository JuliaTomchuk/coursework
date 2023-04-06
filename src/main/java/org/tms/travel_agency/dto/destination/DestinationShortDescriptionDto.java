package org.tms.travel_agency.dto.destination;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tms.travel_agency.domain.Region;

import java.util.HashSet;
import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DestinationShortDescriptionDto extends DestinationLightDto{
    private Set<Region> regions = new HashSet<>();
}

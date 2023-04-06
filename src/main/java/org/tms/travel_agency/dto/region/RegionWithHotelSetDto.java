package org.tms.travel_agency.dto.region;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tms.travel_agency.domain.Hotel;

import java.util.HashSet;
import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionWithHotelSetDto extends RegionLightDto{

    private Set<Hotel> hotels = new HashSet<>();
}

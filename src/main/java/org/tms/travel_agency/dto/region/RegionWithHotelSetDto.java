package org.tms.travel_agency.dto.region;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tms.travel_agency.dto.hotel.HotelLightDto;

import java.util.HashSet;
import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionWithHotelSetDto extends RegionLightDto{

    private Set<HotelLightDto> hotels = new HashSet<>();
}

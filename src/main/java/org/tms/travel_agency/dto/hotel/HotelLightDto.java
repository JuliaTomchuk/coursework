package org.tms.travel_agency.dto.hotel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tms.travel_agency.domain.HotelTypeByStars;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelLightDto {
    private String name;
    private UUID id;
    private HotelTypeByStars typeByStars;

}

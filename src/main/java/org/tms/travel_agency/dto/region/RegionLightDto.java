package org.tms.travel_agency.dto.region;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionLightDto {
    private UUID id;
    private String name;

}

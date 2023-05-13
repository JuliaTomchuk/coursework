package org.tms.travel_agency.dto.region;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotBlank;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegionDetailsDto {

    private UUID id;
    @NotBlank
    private String name;
    @NotBlank
    private String destination;
    @NotBlank
    private String description;
}

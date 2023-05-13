package org.tms.travel_agency.dto.destination;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DestinationLightDto {
    private UUID id;
    private String name;
}

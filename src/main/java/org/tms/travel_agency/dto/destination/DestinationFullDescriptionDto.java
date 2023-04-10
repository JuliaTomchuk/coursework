package org.tms.travel_agency.dto.destination;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DestinationFullDescriptionDto extends DestinationWithRegionsDto {

    private String description;
}

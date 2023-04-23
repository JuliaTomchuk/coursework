package org.tms.travel_agency.dto.destination;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.tms.travel_agency.validator.OnUpdate;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.UUID;
@Data
@Getter
@Setter
@Valid
public class DestinationDetailsDto {

    private UUID id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;

}

package org.tms.travel_agency.dto.destination;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.tms.travel_agency.validator.OnCreate;
import org.tms.travel_agency.validator.OnUpdate;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.UUID;
@Data
@Getter
@Setter
@Valid
public class DestinationInputDto {
    @Null(groups = {OnCreate.class})
    @NotNull(groups = {OnUpdate.class})
    private UUID id;
    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    private String name;
    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    private String description;

}

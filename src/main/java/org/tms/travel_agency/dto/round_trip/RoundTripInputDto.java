package org.tms.travel_agency.dto.round_trip;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tms.travel_agency.validator.OnCreate;
import org.tms.travel_agency.validator.OnUpdate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoundTripInputDto {
    @Null(groups=OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    private UUID id;
    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    private String destination;
    private UUID airplaneTicketDepartId;
    private UUID airplaneTicketArriveId;


}

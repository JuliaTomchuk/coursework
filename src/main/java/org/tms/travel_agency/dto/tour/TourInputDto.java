package org.tms.travel_agency.dto.tour;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tms.travel_agency.validator.OnCreate;
import org.tms.travel_agency.validator.OnUpdate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourInputDto {
    @Null(groups = {OnCreate.class})
    @NotNull(groups = {OnUpdate.class})
    private UUID id;
    @Null(groups = {OnCreate.class})
    @NotNull(groups = {OnUpdate.class})
    private Integer numOfTourist;
    @NotNull(groups = {OnUpdate.class})
    private List<UUID> roundTripIds;
    private UUID roomId;
    private String region;


}

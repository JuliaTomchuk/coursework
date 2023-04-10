package org.tms.travel_agency.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tms.travel_agency.domain.Hotel;
import org.tms.travel_agency.domain.RoomTypesByOccupancy;
import org.tms.travel_agency.domain.RoomTypesByView;
import org.tms.travel_agency.validator.OnCreate;
import org.tms.travel_agency.validator.OnUpdate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomInputDto {
    @NotNull(groups = {OnUpdate.class})
    @Null(groups={OnCreate.class})
    private UUID id;
    @NotBlank(groups = {OnUpdate.class, OnCreate.class})
    private Integer number;
    @NotNull(groups = {OnUpdate.class, OnCreate.class})
    private RoomTypesByOccupancy typesByOccupancy;
    @NotNull(groups = {OnUpdate.class, OnCreate.class})
    private RoomTypesByView typesByView;
    @NotNull(groups = {OnUpdate.class, OnCreate.class})
    private Hotel hotel;
}

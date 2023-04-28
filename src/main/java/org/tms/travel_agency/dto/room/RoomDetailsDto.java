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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDetailsDto {
    @NotNull(groups = {OnUpdate.class})
    @Null(groups={OnCreate.class})
    private UUID id;
    @NotBlank(groups = {OnUpdate.class, OnCreate.class})
    private Integer number;
    @NotBlank(groups = {OnUpdate.class, OnCreate.class})
    private Integer numOfTourist;
    @NotNull(groups = {OnUpdate.class, OnCreate.class})
    private RoomTypesByOccupancy typesByOccupancy;
    @NotNull(groups = {OnUpdate.class, OnCreate.class})
    private RoomTypesByView typesByView;
    @NotNull(groups = {OnUpdate.class, OnCreate.class})
    private UUID idHotel;
    private String hotelName;
    private String destination;
    private String region;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private BigDecimal price;
}

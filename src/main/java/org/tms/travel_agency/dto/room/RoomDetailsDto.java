package org.tms.travel_agency.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.tms.travel_agency.domain.BoardBasisTypes;
import org.tms.travel_agency.domain.Hotel;
import org.tms.travel_agency.domain.RoomTypesByOccupancy;
import org.tms.travel_agency.domain.RoomTypesByView;
import org.tms.travel_agency.validator.OnCreate;
import org.tms.travel_agency.validator.OnSearch;
import org.tms.travel_agency.validator.OnUpdate;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDetailsDto implements Serializable {
    @NotNull(groups = OnUpdate.class)
    @Null(groups=OnCreate.class)
    private UUID id;
    @NotNull(groups = {OnUpdate.class, OnCreate.class})
    private Integer number;
    @NotNull(groups = {OnUpdate.class, OnCreate.class})
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
    @NotNull(groups = OnSearch.class)
    @FutureOrPresent(groups = OnSearch.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkIn;
    @NotNull(groups = OnSearch.class)
    @Future(groups = OnSearch.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkOut;
    private BigDecimal price;
    @NotNull(groups = OnSearch.class)
    private BoardBasisTypes boardBases;
    private boolean booked;
    private boolean preBooked;
}

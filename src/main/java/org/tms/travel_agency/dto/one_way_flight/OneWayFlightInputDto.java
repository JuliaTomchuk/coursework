package org.tms.travel_agency.dto.one_way_flight;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tms.travel_agency.validator.OnCreate;
import org.tms.travel_agency.validator.OnUpdate;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OneWayFlightInputDto {
    @NotNull(groups = {OnUpdate.class})
    @Null(groups = {OnCreate.class})
    private UUID id;
    @NotBlank(groups = {OnUpdate.class, OnCreate.class})
    private String region;
    @NotBlank(groups = {OnUpdate.class, OnCreate.class})
    private String departureAirport;
    @NotBlank(groups = {OnUpdate.class, OnCreate.class})
    private String arrivalAirport;
    @Future @NotNull
    private LocalDateTime departureTime;
    @Future @NotNull
    private LocalDateTime arrivalTime;
    @NotNull @Positive
    private Integer flightTime;
    @NotBlank(groups = {OnUpdate.class, OnCreate.class})
    private String flightNumber;
    @Positive
    private Integer cabinBaggage;
    @Positive
    private Integer checkedBaggage;
    @NotNull @Positive
    private BigDecimal pricePerHour;
}

package org.tms.travel_agency.dto.round_trip;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoundTripLightDto {
    private UUID id;
    private String departureAirport;
    private String arrivalAirport;
    private BigDecimal totalPrice;
    private LocalDateTime departureTimeToDestination;
    private LocalDateTime departureTimeFromDestination;
    private Integer flightTime;

}

package org.tms.travel_agency.dto.tour;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourLightDto {
    private UUID id;
    private String hotelName;
    private LocalDateTime arrivalTimeToDestination;
    private LocalDateTime arrivalTimeFromDestination;
    private BigDecimal price;
}

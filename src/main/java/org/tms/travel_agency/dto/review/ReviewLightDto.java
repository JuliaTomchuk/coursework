package org.tms.travel_agency.dto.review;

import org.tms.travel_agency.domain.Rating;

import java.time.LocalDateTime;
import java.util.UUID;

public class ReviewLightDto {
    private UUID id;
    private LocalDateTime date;
    private Rating rating;
}

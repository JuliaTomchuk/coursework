package org.tms.travel_agency.dto.review;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tms.travel_agency.domain.Rating;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDetailsDto {
    private UUID id;
    @NotBlank
    private String message;
    private UUID hotelId;
    private String username;
    private LocalDate date;
    @NotNull
    private Rating rating;
}

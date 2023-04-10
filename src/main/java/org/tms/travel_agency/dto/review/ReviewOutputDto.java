package org.tms.travel_agency.dto.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewOutputDto {

    private UUID id;
    private LocalDateTime date;
    private String login;
    private String message;
}

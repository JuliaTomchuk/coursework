package org.tms.travel_agency.dto.airplane_ticket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tms.travel_agency.domain.AirplaneSeatsTypes;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import javax.validation.constraints.Positive;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirplaneTicketInputDto {

    private UUID id;
    @NotNull @Positive
    private Integer numberOfSeat;
    @NotNull
    private AirplaneSeatsTypes airplaneSeatsTypes;
    @NotBlank
    private String flightNumber;

}

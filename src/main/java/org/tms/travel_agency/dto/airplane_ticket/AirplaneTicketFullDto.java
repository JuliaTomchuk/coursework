package org.tms.travel_agency.dto.airplane_ticket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tms.travel_agency.domain.AirplaneSeatsTypes;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirplaneTicketFullDto extends AirplaneTicketLightDto{
    private Integer numberOfSeat;
    private AirplaneSeatsTypes airplaneSeatsTypes;
    private String region;
    private Integer flightTime;
    private String flightNumber;
    private Integer cabinBaggage;
    private Integer checkedBaggage;
}

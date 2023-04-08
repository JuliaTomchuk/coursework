package org.tms.travel_agency.dto.round_trip;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tms.travel_agency.dto.airplane_ticket.AirplaneTicketFullDto;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoundTripFullDescriptionDto  {
    private UUID id;
    private String destination;
    private AirplaneTicketFullDto arrival;
    private AirplaneTicketFullDto depart;

}

package org.tms.travel_agency.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tms.travel_agency.domain.BoardBasisTypes;
import org.tms.travel_agency.domain.Hotel;


import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomFullDescriptionDto extends RoomLightDto {

    private Integer number;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private BoardBasisTypes boardBasisTypes;
    private Hotel hotel;

}

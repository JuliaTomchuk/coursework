package org.tms.travel_agency.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tms.travel_agency.domain.BoardBasisTypes;
import org.tms.travel_agency.domain.HotelTypeByStars;
import org.tms.travel_agency.domain.HotelTypeByTargetMarket;
import org.tms.travel_agency.domain.RoomTypesByOccupancy;
import org.tms.travel_agency.domain.RoomTypesByView;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelShortDescriptionDTO extends HotelLightDTO{
    private HotelTypeByTargetMarket typeByTargetMarket;
    private Set<BoardBasisTypes> boardBasisTypesSet;
    private Set<RoomTypesByView> roomTypesByViewSet;
    private Set <RoomTypesByOccupancy> roomTypesByOccupancySet;
    private BigDecimal basicPriceOfRoom;
}

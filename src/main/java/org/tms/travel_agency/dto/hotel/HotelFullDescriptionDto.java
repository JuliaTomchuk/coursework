package org.tms.travel_agency.dto.hotel;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tms.travel_agency.domain.BoardBasisTypes;
import org.tms.travel_agency.domain.HotelTypeByStars;
import org.tms.travel_agency.domain.HotelTypeByTargetMarket;
import org.tms.travel_agency.domain.RoomTypesByOccupancy;
import org.tms.travel_agency.domain.RoomTypesByView;
import org.tms.travel_agency.dto.review.ReviewLightDto;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelFullDescriptionDto extends HotelLightDto {
    private String name;
    private UUID id;
    private HotelTypeByStars typeByStars;
    private String region;
    private String city;
    private String street;
    private String home;
    private String description;
    private HotelTypeByTargetMarket typeByTargetMarket;
    private Set<BoardBasisTypes> boardBasisTypesSet;
    private Set<RoomTypesByView> roomTypesByViewSet;
    private Set <RoomTypesByOccupancy> roomTypesByOccupancySet;
    private BigDecimal basicPriceOfRoom;
    private Set<ReviewLightDto> reviews = new HashSet<>();

}

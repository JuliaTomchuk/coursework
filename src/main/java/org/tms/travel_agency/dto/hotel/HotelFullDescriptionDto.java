package org.tms.travel_agency.dto.hotel;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tms.travel_agency.domain.Review;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelFullDescriptionDto extends HotelShortDescriptionDto {
    private String region;
    private String city;
    private String street;
    private String home;
    private String description;
    private Set<Review> reviews = new HashSet<>();

}

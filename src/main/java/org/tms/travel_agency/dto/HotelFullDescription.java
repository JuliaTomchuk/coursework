package org.tms.travel_agency.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tms.travel_agency.domain.Review;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelFullDescription extends HotelShortDescriptionDTO{
    private String region;
    private String city;
    private String street;
    private String home;
    private String description;
    private Set<Review> reviews = new HashSet<>();


}

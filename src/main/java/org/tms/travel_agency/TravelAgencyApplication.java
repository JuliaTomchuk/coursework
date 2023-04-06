package org.tms.travel_agency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.tms.travel_agency.domain.Tour;
import org.tms.travel_agency.dto.HotelFullDescription;


@SpringBootApplication
public class TravelAgencyApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelAgencyApplication.class, args);

    }

}

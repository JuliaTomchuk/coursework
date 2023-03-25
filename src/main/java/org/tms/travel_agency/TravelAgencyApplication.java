package org.tms.travel_agency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.tms.travel_agency.domain.AirplaneSeat;

@SpringBootApplication
public class TravelAgencyApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelAgencyApplication.class, args);
        AirplaneSeat airplaneSeat = new AirplaneSeat();

    }

}

package org.tms.travel_agency.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;


@Entity
@NoArgsConstructor
@Setter
@Getter
public class AirplaneTicket extends TourProduct {

    @Id
    @GeneratedValue
    private UUID id;
    private Integer numberOfSeat;
    @ManyToOne
    private OneWayFlight oneWayFlight;
    private AirplaneSeatsTypes airplaneSeatsTypes;

    public AirplaneTicket(Integer numberOfSeat, OneWayFlight oneWayFlight, AirplaneSeatsTypes airplaneSeatsTypes) {
        this.numberOfSeat = numberOfSeat;
        this.oneWayFlight = oneWayFlight;
        this.airplaneSeatsTypes = airplaneSeatsTypes;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AirplaneTicket that)) return false;
        return getNumberOfSeat().equals(that.getNumberOfSeat()) && getOneWayFlight().equals(that.getOneWayFlight());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNumberOfSeat(), getOneWayFlight());
    }


}

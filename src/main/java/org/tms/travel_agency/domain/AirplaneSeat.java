package org.tms.travel_agency.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Objects;
import java.util.UUID;


@Entity
@NoArgsConstructor
@Setter
@Getter
public class AirplaneSeat {


    @Id
    @GeneratedValue
    private UUID id;
    @NaturalId
    private Integer number;
    @ManyToOne
    @NaturalId
    private OneWayFlight oneWayFlight;
    private AirplaneSeatsTypes airplaneSeatsTypes;
    private Integer price;
    private boolean booked;

    public AirplaneSeat(Integer number, OneWayFlight oneWayFlight, AirplaneSeatsTypes airplaneSeatsTypes,Integer price) {
        this.number = number;
        this.oneWayFlight = oneWayFlight;
        this.airplaneSeatsTypes = airplaneSeatsTypes;
        this.price=price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AirplaneSeat that)) return false;
        return getNumber().equals(that.getNumber()) && getOneWayFlight().equals(that.getOneWayFlight());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNumber(), getOneWayFlight());
    }
}

package org.tms.travel_agency.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@Table(name = "round_trips")
@NoArgsConstructor
public class RoundTrip extends TourProduct {

    @Id
    @GeneratedValue
    private UUID id;
    @OneToOne
    private AirplaneTicket depart;
    @OneToOne
     private AirplaneTicket arrive;
    @ManyToOne
    private Destination destination;

    public RoundTrip(AirplaneTicket depart, AirplaneTicket arrive,Destination destination) {

        this.depart = depart;
        this.arrive = arrive;
        this.destination=destination;

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoundTrip trip)) return false;
        if (!super.equals(o)) return false;
        return getDepart().equals(trip.getDepart()) && getArrive().equals(trip.getArrive());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getDepart(), getArrive());
    }


}

package org.tms.travel_agency.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tours")
@Setter
@Getter
@ToString
@NoArgsConstructor
public class Tour extends TourProduct {

    @Id
    @GeneratedValue
    private UUID id;
    private Long bookingNumber;
    private Integer numOfTourist;
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<RoundTrip> roundTrips = new ArrayList<>();
    @OneToOne
    private Room room;
    @ManyToOne
    private Region region;


    public Tour(List<RoundTrip> roundTrips, Room room, Region region, Integer numOfTourist) {
        this.roundTrips = roundTrips;
        this.room = room;
        this.region = region;
        this.numOfTourist = numOfTourist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tour tour)) return false;
        return getBookingNumber().equals(tour.getBookingNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBookingNumber());
    }



    protected BigDecimal calculatePrice() {
        BigDecimal roomPrice = room.getPrice();
        BigDecimal roundTripsPrice = new BigDecimal(0.0);
        for(RoundTrip trip: roundTrips){
            roundTripsPrice.add(trip.getPrice());
        }
        return roomPrice.add(roundTripsPrice);
    }



}

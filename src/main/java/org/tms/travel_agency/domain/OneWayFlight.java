package org.tms.travel_agency.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@Table(name = "one_way_flights")
public class OneWayFlight {

    @Id
    @GeneratedValue
    private UUID id;

    @NaturalId
    private String departureAirport;
    private String arrivalAirport;
    @NaturalId
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Integer flightTime;
     @NaturalId
    private Integer flightNumber;
    private Integer cabinBaggage;
    private Integer checkedBaggage;
    @OneToMany(cascade= CascadeType.ALL,mappedBy = "oneWayFlight",orphanRemoval = true)
    private Set<AirplaneSeat> airplaneSeats=new HashSet<>();

    public OneWayFlight(String departureAirport, String arrivalAirport, LocalDateTime departureTime, LocalDateTime arrivalTime, Integer flightTime, Integer flightNumber, Integer cabinBaggage, Integer checkedBaggage) {
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.flightTime = flightTime;
        this.flightNumber = flightNumber;
        this.cabinBaggage = cabinBaggage;
        this.checkedBaggage = checkedBaggage;
    }

    public boolean addSeat(AirplaneSeat seat){
        boolean isAdded = airplaneSeats.add(seat);
        seat.setOneWayFlight(this);
        return isAdded;
    }
    public boolean deleteSeat(AirplaneSeat seat){
        boolean isDeleted = airplaneSeats.remove(seat);
        seat.setOneWayFlight(null);
        return isDeleted;
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OneWayFlight that)) return false;
        return getDepartureAirport().equals(that.getDepartureAirport()) && getDepartureTime().equals(that.getDepartureTime()) && getFlightNumber().equals(that.getFlightNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDepartureAirport(), getDepartureTime(), getFlightNumber());
    }
}

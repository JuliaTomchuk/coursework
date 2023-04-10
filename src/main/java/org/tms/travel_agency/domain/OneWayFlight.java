package org.tms.travel_agency.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
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
    @ManyToOne
    private Destination destination;
    @NaturalId
    private String departureAirport;
    private String arrivalAirport;
    @NaturalId
     private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Integer flightTime;
     @NaturalId
     private String flightNumber;
    private Integer cabinBaggage;
    private Integer checkedBaggage;
    private BigDecimal pricePerHour;
    @OneToMany(cascade= CascadeType.ALL,mappedBy = "oneWayFlight",orphanRemoval = true)
    private Set<AirplaneTicket> airplaneTickets=new HashSet<>();

    public OneWayFlight(Destination destination,String departureAirport, String arrivalAirport, LocalDateTime departureTime, LocalDateTime arrivalTime, Integer flightTime, String flightNumber, Integer cabinBaggage, Integer checkedBaggage, BigDecimal pricePerHour) {
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.flightTime = flightTime;
        this.flightNumber = flightNumber;
        this.cabinBaggage = cabinBaggage;
        this.checkedBaggage = checkedBaggage;
        this.destination=destination;
        this.pricePerHour=pricePerHour;
    }

    public void addTicket(AirplaneTicket ticket){
        boolean isAdded = airplaneTickets.add(ticket);
        ticket.setOneWayFlight(this);
         }
    public void deleteTicket(AirplaneTicket ticket){
        boolean isDeleted = airplaneTickets.remove(ticket);
        ticket.setOneWayFlight(null);

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

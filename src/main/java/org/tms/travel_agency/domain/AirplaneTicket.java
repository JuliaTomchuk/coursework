package org.tms.travel_agency.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.NaturalId;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;


@Entity
@NoArgsConstructor
@Setter
@Getter
public class AirplaneTicket extends TourProduct
{

    @Id
    @GeneratedValue
    private UUID id;
   // @NaturalId
    private Integer numberOfSeat;
    @ManyToOne
   // @NaturalId
    private OneWayFlight oneWayFlight;
    private AirplaneSeatsTypes airplaneSeatsTypes;
     private boolean booked;

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

    @Override
    protected BigDecimal calculatePrice() {
        Integer flightTime = oneWayFlight.getFlightTime();
        BigDecimal pricePerHour = oneWayFlight.getPricePerHour();
        BigDecimal baggagePrice=calculateBaggagePrice();
        BigDecimal seatPrice =calculateSeatPrice();
        price= BigDecimal.valueOf(flightTime).multiply(pricePerHour).add(baggagePrice).add(seatPrice);
        return price;

    }
    private BigDecimal calculateBaggagePrice(){
        BigDecimal baggagePrice= new BigDecimal(0.0);
        if( oneWayFlight.getCabinBaggage()>0){
            baggagePrice = new BigDecimal(20.50);
        }
        if(oneWayFlight.getCheckedBaggage()>0){
            baggagePrice = new BigDecimal(50.40);
        }
        return baggagePrice;
    }
    private BigDecimal calculateSeatPrice(){
       BigDecimal seatPrice=new BigDecimal(0.0);
        switch(airplaneSeatsTypes){
            case ECONOMY_CLASS -> seatPrice=new BigDecimal(100.70);
            case BUSINESS_CLASS -> seatPrice=new BigDecimal(500.40);
            case FIRST_CLASS -> seatPrice=new BigDecimal(1000.10);
        }
        return seatPrice;

    }

    @Override
    protected void book() {
      booked =true;
    }

    @Override
    protected void cancelBooking() {
    booked =false;
    }
}

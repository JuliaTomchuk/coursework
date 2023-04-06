package org.tms.travel_agency.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
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



    public RoundTrip(AirplaneTicket depart, AirplaneTicket arrive) {

        this.depart = depart;
        this.arrive = arrive;

    }
    @Override
    public BigDecimal calculatePrice() {
        return depart.getPrice().add(arrive.getPrice());
    }

    @Override
    protected void book() {
        //переопределить методы нормально
        depart.setBooked(true);
        arrive.setBooked(true);
    }

    @Override
    protected void cancelBooking() {

    }


}

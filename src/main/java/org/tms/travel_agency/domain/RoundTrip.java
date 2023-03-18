package org.tms.travel_agency.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@Entity
@Table(name = "round_trips")
@NoArgsConstructor
public class RoundTrip {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Setter(AccessLevel.NONE)
    private Integer id;
    @OneToOne
    private AirplaneSeat depart;
    @OneToOne
    private AirplaneSeat arrive;


    public RoundTrip(AirplaneSeat depart, AirplaneSeat arrive) {

        this.depart = depart;
        this.arrive = arrive;

    }

    public int calculatePrice() {
        return depart.getPrice() + arrive.getPrice();
    }

}

package org.tms.travel_agency.domain;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
@Data
@NoArgsConstructor
@Entity
@Table(name = "rooms")
public class Room extends TourProduct{

    @Id
    @GeneratedValue
    private UUID id;

    @NaturalId
    private Integer number;
    private Integer numOfTourist;
    private RoomTypesByOccupancy typesByOccupancy;
    private RoomTypesByView typesByView;
    private Integer pricePerDay;
    private LocalDate checkIn;
    private LocalDate checkOut;
    @OneToMany()
    private List<BoardBasis> boardBases = new ArrayList<>();
    @ManyToOne
    @NaturalId
    private Hotel hotel;

    public Room(Integer number, RoomTypesByOccupancy typesByOccupancy, RoomTypesByView typesByView, Integer price, Hotel hotel,Integer numOfTourist) {
        this.number = number;
        this.typesByOccupancy = typesByOccupancy;
        this.typesByView = typesByView;
        this.pricePerDay = price;
        this.hotel = hotel;
        this.numOfTourist=numOfTourist;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room room)) return false;
        return getNumber().equals(room.getNumber()) && getHotel().equals(room.getHotel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNumber(), getHotel());
    }


    @Override
    protected BigDecimal calculatePrice() {
        return null;
    }

    @Override
    protected void book() {

    }

    @Override
    protected void cancelBooking() {

    }
}

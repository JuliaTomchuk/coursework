package org.tms.travel_agency.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Getter
//@ToString
@Setter
@Entity
@Table(name = "rooms", uniqueConstraints = {@UniqueConstraint(columnNames = {"number", "hotel_id"})})
public class Room extends TourProduct {

    @Id
    @GeneratedValue
    private UUID id;
    private Integer number;
    private Integer numOfTourist;
    private RoomTypesByOccupancy typesByOccupancy;
    private RoomTypesByView typesByView;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private BoardBasisTypes boardBases;
    @ManyToOne
    private Hotel hotel;


    public Room(Integer number, RoomTypesByOccupancy typesByOccupancy, RoomTypesByView typesByView, Hotel hotel) {
        this.number = number;
        this.typesByOccupancy = typesByOccupancy;
        this.typesByView = typesByView;
        this.hotel = hotel;
    }

    public Room() {
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
    public String toString() {
        return "Room{" +
                "id=" + id + "\n" +
                ", destination: " + hotel.getRegion().getDestination().getName() + "\n" +
                ", region: " + hotel.getRegion().getName() + "\n" +
                ", hotel: " + hotel.getName() + "\n" +
                ", stars: " + hotel.getTypeByStars() + "\n" +
                ", type Of hotel: " + hotel.getTypeByTargetMarket() + "\n" +
                ", room number: " + number + "\n" +
                ", numOfTourist: " + numOfTourist + "\n" +
                ", typesByOccupancy: " + typesByOccupancy + "\n" +
                ", typesByView: " + typesByView + "\n" +
                ", checkIn: " + checkIn + "\n" +
                ", checkOut: " + checkOut + "\n" +
                ", boardBases: " + boardBases +
                '}';
    }
}

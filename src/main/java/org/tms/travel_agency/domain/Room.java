package org.tms.travel_agency.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue
    private UUID id;

    @NaturalId
    private Integer number;
    private RoomTypesByOccupancy typesByOccupancy;
    private RoomTypesByView typesByView;
    private Integer price;
    private boolean booked;
    @ManyToOne
    @NaturalId
    private Hotel hotel;

    public Room(Integer number, RoomTypesByOccupancy typesByOccupancy, RoomTypesByView typesByView, Integer price, Hotel hotel) {
        this.number = number;
        this.typesByOccupancy = typesByOccupancy;
        this.typesByView = typesByView;
        this.price = price;
        this.hotel = hotel;
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
}

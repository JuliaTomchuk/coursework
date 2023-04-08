package org.tms.travel_agency.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.NaturalId;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;


@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "hotels")
public class Hotel {
    @NaturalId
    private String name;

    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "hotel")
    private Set<Room> rooms = new HashSet<>();
    private BigDecimal basicPriceOfRoomPerDay;
    @NaturalId
    @OneToOne
    private Address address;
    @ManyToOne
    private Region region;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true,mappedBy = "hotel")
    private Set<BoardBasis> boardBasisSet = new HashSet<>();
    @Basic(fetch = FetchType.LAZY)
    @Lob
    private String description;
    private HotelTypeByStars typeByStars;
    private HotelTypeByTargetMarket typeByTargetMarket;
    @OneToMany(mappedBy = "hotel",cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Review> reviews = new HashSet<>();

    public Hotel(String name, Set<Room> rooms, Address address, Region region, Set<BoardBasis> boardBasisSet, String description,HotelTypeByStars typeByStars, HotelTypeByTargetMarket typeByTargetMarket,BigDecimal basicPriceOfRoomPerDay) {
        this.name = name;
        this.rooms = rooms;
        this.address = address;
        this.region = region;
        this.boardBasisSet = boardBasisSet;
        this.description = description;
        this.typeByStars =typeByStars;
        this.typeByTargetMarket = typeByTargetMarket;
        this.basicPriceOfRoomPerDay =basicPriceOfRoomPerDay;
    }


    public void addRoom(Room room) {
        rooms.add(room);
        room.setHotel(this);
    }

    public void deleteRoom(Room room) {
        rooms.remove(room);
        room.setHotel(null);

    }

    public void addBoardBasis(BoardBasis boardBasis) {
        boardBasisSet.add(boardBasis);
        boardBasis.setHotel(this);
        }

    public void deleteBoardBasis(BoardBasis boardBasis) {
        boardBasisSet.remove(boardBasis);
        boardBasis.setHotel(null);
           }

    public void addReview(Review review) {
        reviews.add(review);
        review.setHotel(this);

    }

    public void deleteReview(Review review) {
        reviews.remove(review);
        review.setHotel(null);

    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hotel hotel)) return false;
        return getName().equals(hotel.getName()) && getAddress().equals(hotel.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getAddress());
    }
}

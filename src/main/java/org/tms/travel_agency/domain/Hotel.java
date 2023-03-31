package org.tms.travel_agency.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@ToString
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
    private UUID id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "hotel")
    private Set<Room> rooms = new HashSet<>();

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

    public Hotel(String name, Set<Room> rooms, Address address, Region region, Set<BoardBasis> boardBasisSet, String description,HotelTypeByStars typeByStars, HotelTypeByTargetMarket typeByTargetMarket) {
        this.name = name;
        this.rooms = rooms;
        this.address = address;
        this.region = region;
        this.boardBasisSet = boardBasisSet;
        this.description = description;
        this.typeByStars =typeByStars;
        this.typeByTargetMarket = typeByTargetMarket;
    }


    public boolean addRoom(Room room) {
        boolean isAdded = rooms.add(room);
        room.setHotel(this);
        return isAdded;
    }

    public boolean deleteRoom(Room room) {
        boolean isDeleted = rooms.remove(room);
        room.setHotel(null);
        return isDeleted;
    }

    public boolean addBoardBasis(BoardBasis boardBasis) {
        boolean isAdded = boardBasisSet.add(boardBasis);
        boardBasis.setHotel(this);
        return isAdded;
    }

    public boolean deleteBoardBasis(BoardBasis boardBasis) {
        boolean isDeleted = boardBasisSet.remove(boardBasis);
        boardBasis.setHotel(null);
        return isDeleted;
    }

    public boolean addReview(Review review) {
        boolean isAdded = reviews.add(review);
        review.setHotel(this);
        return isAdded;
    }

    public boolean deleteReview(Review review) {
        boolean isDeleted = reviews.remove(review);
        review.setHotel(null);
        return isDeleted;
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

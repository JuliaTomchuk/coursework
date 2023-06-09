package org.tms.travel_agency.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import lombok.ToString;
import org.hibernate.annotations.NaturalId;
import org.tms.travel_agency.repository.HotelRepository;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;


@Getter
@ToString
@Setter
@Entity
@NoArgsConstructor
@Table(name = "hotels")
@NamedQuery(name = "Hotel.findByAddressANDRegion", query = "select h.name from Hotel h where h.address.city = :city and h.address.street=:street and h.address.home= :home and h.region.name= :regionName  ")
public class Hotel {
    private String name;

    @Id
    @GeneratedValue
    private UUID id;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "hotel")
    @ToString.Exclude
    private Set<Room> rooms = new HashSet<>();
    private BigDecimal basicPriceOfRoomPerDay;
    @NaturalId
    @OneToOne(orphanRemoval = true)
    private Address address;
    @ManyToOne
    @NaturalId
    private Region region;
    @ElementCollection(targetClass = BoardBasisTypes.class)
    private Set<BoardBasisTypes> boardBasisSet = new HashSet<>();
    @Basic(fetch = FetchType.LAZY)
    @Lob
    private String description;
    private HotelTypeByStars typeByStars;
    private HotelTypeByTargetMarket typeByTargetMarket;
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Review> reviews = new HashSet<>();

    public Hotel(String name, Set<Room> rooms, Address address, Region region, Set<BoardBasisTypes> boardBasisSet, String description, HotelTypeByStars typeByStars, HotelTypeByTargetMarket typeByTargetMarket, BigDecimal basicPriceOfRoomPerDay) {
        this.name = name;
        this.rooms = rooms;
        this.address = address;
        this.region = region;
        this.boardBasisSet = boardBasisSet;
        this.description = description;
        this.typeByStars = typeByStars;
        this.typeByTargetMarket = typeByTargetMarket;
        this.basicPriceOfRoomPerDay = basicPriceOfRoomPerDay;
    }


    public void addRoom(Room room) {
        rooms.add(room);
        room.setHotel(this);
    }

    public void deleteRoom(Room room) {
        rooms.remove(room);
        room.setHotel(null);

    }


    public void addReview(Review review) {
        reviews.add(review);
        review.setHotel(this);

    }

    public void deleteReview(Review review) {
        reviews.remove(review);
        review.setHotel(null);

    }

    public void addBoardBasisType(BoardBasisTypes type) {
        boardBasisSet.add(type);
    }

    public void deleteBoardBasisType(BoardBasisTypes type) {
        boardBasisSet.remove(type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hotel hotel)) return false;
        return getAddress().equals(hotel.getAddress()) && getRegion().equals(hotel.getRegion());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAddress(), getRegion());
    }
}

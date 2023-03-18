package org.tms.travel_agency.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Setter(value = AccessLevel.NONE)
    private Integer id;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "hotel")
    private Set<Room> rooms = new HashSet<>();
    @OneToOne
    @NaturalId
    private Address address;
    @ManyToOne
    private Region region;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true,mappedBy = "hotel")
    private Set<BoardBasis> boardBasisSet = new HashSet<>();
    @Basic(fetch = FetchType.LAZY)
    @Lob
    private String description;

    public Hotel(String name, Set<Room> rooms, Address address, Region region, Set<BoardBasis> boardBasisSet, String description) {
        this.name = name;
        this.rooms = rooms;
        this.address = address;
        this.region = region;
        this.boardBasisSet = boardBasisSet;
        this.description = description;
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

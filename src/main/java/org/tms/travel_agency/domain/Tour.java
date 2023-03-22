package org.tms.travel_agency.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tours")
@Setter
@Getter
@ToString
@NoArgsConstructor
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Setter(AccessLevel.NONE)
    private Integer id;
    @NaturalId
    private Long bookingNumber;
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<RoundTrip> roundTrips = new ArrayList<>();
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<BoardBasis> boardBasisSet = new ArrayList<>();
    @OneToOne
    private Room room;


    public Tour(List<RoundTrip> roundTrips, List<BoardBasis> boardBasisSet, Room room) {
        this.roundTrips = roundTrips;
        this.boardBasisSet = boardBasisSet;
        this.room = room;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tour tour)) return false;
        return getBookingNumber().equals(tour.getBookingNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBookingNumber());
    }
}

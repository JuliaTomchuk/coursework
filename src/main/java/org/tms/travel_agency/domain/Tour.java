package org.tms.travel_agency.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

    public Integer calculatePrice() {
        return room.getPrice() + calculateRoundTripsPrice() + calculateBoardBasisPrice();

    }

    private Integer calculateRoundTripsPrice() {
        int roundTripsPrice = 0;
        for (RoundTrip trip : roundTrips) {
            roundTripsPrice = roundTripsPrice + trip.calculatePrice();
        }
        return roundTripsPrice;
    }

    private Integer calculateBoardBasisPrice() {
        int boardBasisPrice = 0;
        for (BoardBasis boardBasis : boardBasisSet) {
            boardBasisPrice = boardBasisPrice + boardBasis.getPrice();
        }
        return boardBasisPrice;
    }


}

package org.tms.travel_agency.domain;

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
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@Table(name = "regions")
public class Region {
    @Id
    @GeneratedValue
    private UUID id;
     @NaturalId
    private String name;
    @OneToMany(mappedBy = "region", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Hotel> hotels = new HashSet<>();
    @OneToMany(mappedBy = "region", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<OneWayFlight> oneWayFlightSet = new HashSet<>();
    @OneToMany(mappedBy = "region", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Tour> tours = new HashSet<>();
    @OneToMany(mappedBy = "region", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<RoundTrip>  roundTripSet = new HashSet<>();
     @ManyToOne
    private Destination destination;
    @Basic(fetch = FetchType.LAZY)
    @Lob
    private String description;

    public Region(String name, Set<Hotel> hotels, Destination destination, String description) {
        this.name = name;
        this.hotels = hotels;
        this.destination = destination;
        this.description = description;
    }


    public void addHotel(Hotel hotel) {
        boolean isAdded = hotels.add(hotel);
        hotel.setRegion(this);

    }

    public void deleteHotel(Hotel hotel) {
       hotels.remove(hotel);
        hotel.setRegion(null);

    }
    public void addOneWayFlight(OneWayFlight oneWayFlight) {
        oneWayFlightSet.add(oneWayFlight);
        oneWayFlight.setRegion(this);

    }

    public void deleteOneWayFlight(OneWayFlight oneWayFlight) {
        oneWayFlightSet.remove(oneWayFlight);
        oneWayFlight.setRegion(null);

    }
    public void addTour(Tour tour){
        tours.add(tour);
        tour.setRegion(this);
    }
    public void deleteTour(Tour tour){
        tours.add(tour);
        tour.setRegion(this);
    }
    public void addRoundTrip(RoundTrip trip){
        roundTripSet.add(trip);
        trip.setRegion(this);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Region region)) return false;
        return getName().equals(region.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}

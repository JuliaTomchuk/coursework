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
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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


    public boolean addHotel(Hotel hotel) {
        boolean isAdded = hotels.add(hotel);
        hotel.setRegion(this);
        return isAdded;
    }

    public boolean deleteHotel(Hotel hotel) {
        boolean isDeleted = hotels.remove(hotel);
        hotel.setRegion(null);
        return isDeleted;
    }
    public boolean addOneWayFlight(OneWayFlight oneWayFlight) {
        boolean isAdded = oneWayFlightSet.add(oneWayFlight);
        oneWayFlight.setRegion(this);
        return isAdded;
    }

    public boolean deleteHotel(OneWayFlight oneWayFlight) {
        boolean isDeleted = oneWayFlightSet.remove(oneWayFlight);
        oneWayFlight.setRegion(null);
        return isDeleted;
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

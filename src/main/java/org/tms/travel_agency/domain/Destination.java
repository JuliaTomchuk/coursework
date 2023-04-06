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
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Setter
@ToString
@Entity
@Table(name = "destinations")
public class Destination {

    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    @NaturalId
    private String name;
    @OneToMany(mappedBy = "destination", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Region> regions = new HashSet<>();
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String description;

    public Destination(String name, Set<Region> regions, String description) {

        this.name = name;
        this.regions = regions;
        this.description = description;
    }


    public void addRegion(Region region) {
        regions.add(region);
        region.setDestination(this);

    }

    public void removeRegion(Region region) {
        regions.remove(region);
        region.setDestination(null);
           }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Destination that)) return false;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}

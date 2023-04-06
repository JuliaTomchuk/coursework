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
import javax.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@ToString
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    @NaturalId
    private String city;
    @NaturalId
    private String street;

    @NaturalId
    private String home;

    public Address( String city, String street, String home) {


        this.city = city;
        this.street = street;
        this.home=home;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address address)) return false;
        return getCity().equals(address.getCity()) && getStreet().equals(address.getStreet()) && getHome().equals(address.getHome());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCity(), getStreet(), getHome());
    }
}
package org.tms.travel_agency.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
@Entity
@Table(name="carts")
@NoArgsConstructor
@Getter
@Setter
public class Cart {

    @Id
    @GeneratedValue
    private UUID id;

    @NaturalId
    @OneToOne
    private User user;
    @OneToMany
    private Set<Tour> tours = new HashSet<>();

    public Cart(User user, Set<Tour> tours) {
        this.user = user;
        this.tours = tours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cart cart)) return false;
        return getUser().equals(cart.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUser());
    }
}

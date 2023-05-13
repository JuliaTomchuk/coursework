package org.tms.travel_agency.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "carts")
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
    private List<TourProduct> tourProductList = new ArrayList<>();

    public Cart(User user, List<TourProduct> tourProductList) {
        this.user = user;
        this.tourProductList = tourProductList;
    }

    public void addTourProduct(TourProduct tourProduct) {
        tourProductList.add(tourProduct);
    }

    public void deleteTourProduct(TourProduct tourProduct) {
        tourProductList.remove(tourProduct);
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

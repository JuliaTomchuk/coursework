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
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardBasis {

    @Id
    @GeneratedValue
    private UUID id;

    @NaturalId
    private BoardBasisTypes type;
    private BigDecimal price;
    @ManyToOne
    @NaturalId
    private Hotel hotel;

    public BoardBasis(BoardBasisTypes type, BigDecimal price, Hotel hotel) {
        this.type = type;
        this.price = price;
        this.hotel = hotel;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoardBasis that)) return false;
        return getType() == that.getType() && getHotel().equals(that.getHotel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), getHotel());
    }
}

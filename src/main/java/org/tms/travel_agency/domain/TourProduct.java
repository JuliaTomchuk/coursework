package org.tms.travel_agency.domain;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
 public abstract class TourProduct {
    @Id
    @GeneratedValue
    private UUID id;
    protected boolean booked;
    protected BigDecimal price;
    protected abstract BigDecimal calculatePrice();
    protected abstract void book();
    protected abstract void cancelBooking();



}

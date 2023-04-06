package org.tms.travel_agency.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
 public abstract class TourProduct {
    protected boolean booked;
    protected BigDecimal price;
    protected abstract BigDecimal calculatePrice();
    protected abstract void book();
    protected abstract void cancelBooking();


}

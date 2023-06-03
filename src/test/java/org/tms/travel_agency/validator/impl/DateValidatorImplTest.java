package org.tms.travel_agency.validator.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.tms.travel_agency.validator.DateValidator;

import java.time.LocalDate;


class DateValidatorImplTest {
    private DateValidator dateValidator = new DateValidatorImpl();

    @Test
    void checkInEarlierThanCheckOut() {
        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = checkIn.plusDays(7);
        boolean checkInEarlierThanCheckOut = dateValidator.isCheckInEarlierThanCheckOut(checkIn, checkOut);
        Assertions.assertThat(checkInEarlierThanCheckOut).isTrue();
    }

    @Test
    void checkInLaterThanCheckOut() {
        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = checkIn.minusDays(7);
        boolean checkInEarlierThanCheckOut = dateValidator.isCheckInEarlierThanCheckOut(checkIn, checkOut);
        Assertions.assertThat(checkInEarlierThanCheckOut).isFalse();

    }
}
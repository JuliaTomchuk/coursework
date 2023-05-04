package org.tms.travel_agency.validator;

import java.time.LocalDate;

public interface DateValidator {
    boolean isCheckInLessThanCheckOut(LocalDate checkIn, LocalDate checkOut);
}

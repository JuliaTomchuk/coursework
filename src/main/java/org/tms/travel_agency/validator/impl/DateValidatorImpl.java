package org.tms.travel_agency.validator.impl;

import org.springframework.stereotype.Service;
import org.tms.travel_agency.validator.DateValidator;

import java.time.LocalDate;
@Service
public class DateValidatorImpl implements DateValidator {
    @Override
    public boolean isCheckInLessThanCheckOut(LocalDate checkIn, LocalDate checkOut) {
      return  checkOut.isAfter(checkIn);
    }
}

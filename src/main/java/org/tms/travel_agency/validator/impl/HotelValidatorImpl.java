package org.tms.travel_agency.validator.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.tms.travel_agency.domain.Hotel;
import org.tms.travel_agency.dto.hotel.HotelDetailsDto;
import org.tms.travel_agency.repository.HotelRepository;
import org.tms.travel_agency.validator.DuplicateValidator;


import java.util.Optional;


@Service
@RequiredArgsConstructor
public class HotelValidatorImpl implements DuplicateValidator<HotelDetailsDto> {
    private final HotelRepository repository;

    @Override

    public boolean isUnique(HotelDetailsDto dto) {
        return repository.findByAddressANDRegion(dto.getCity(), dto.getStreet(), dto.getHome(), dto.getRegion()).isEmpty();
    }

}

package org.tms.travel_agency.validator.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.tms.travel_agency.dto.hotel.HotelDetailsDto;
import org.tms.travel_agency.repository.HotelRepository;
import org.tms.travel_agency.validator.DuplicateValidator;


@Service
@RequiredArgsConstructor
public class HotelValidator implements DuplicateValidator<HotelDetailsDto> {
    private final HotelRepository repository;

    @Override

    public boolean isUnique(HotelDetailsDto dto) {
        return repository.findByAddressANDRegion(dto.getCity(), dto.getStreet(), dto.getHome(), dto.getRegion()).isEmpty();
    }

}

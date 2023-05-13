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
        Optional<Hotel> hotelOptional = repository.findByAddress_City_AndAddress_Street_AndAddress_Home_AndRegion_Name(dto.getCity(), dto.getStreet(), dto.getHome(), dto.getRegion());

            if (hotelOptional.isEmpty()) {
                return true;
            }
            return false;
        }


    }



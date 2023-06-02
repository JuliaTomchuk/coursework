package org.tms.travel_agency.validator.impl;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tms.travel_agency.domain.Hotel;
import org.tms.travel_agency.dto.hotel.HotelDetailsDto;
import org.tms.travel_agency.repository.HotelRepository;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class HotelValidatorTest {
    private HotelRepository repository;
    private HotelValidator validator;

    @BeforeEach
    void init() {
        repository = Mockito.mock(HotelRepository.class);
        validator = new HotelValidator(repository);
    }

    @Test
    void isUnique() {
        HotelDetailsDto dto = new HotelDetailsDto();
        dto.setRegion("TEST");
        dto.setCity("TEST");
        dto.setStreet("TEST");
        dto.setHome("TEST");

        Mockito.when(repository.findByAddressANDRegion(dto.getCity(), dto.getStreet(), dto.getHome(), dto.getRegion())).thenReturn(Optional.empty());
        boolean unique = validator.isUnique(dto);
        Assertions.assertThat(unique).isTrue();
    }

    @Test
    void isNotUnique() {
        HotelDetailsDto dto = new HotelDetailsDto();
        dto.setRegion("TEST");
        dto.setCity("TEST");
        dto.setStreet("TEST");
        dto.setHome("TEST");

        Mockito.when(repository.findByAddressANDRegion(dto.getCity(), dto.getStreet(), dto.getHome(), dto.getRegion())).thenReturn(Optional.of(new Hotel()));
        boolean unique = validator.isUnique(dto);
        Assertions.assertThat(unique).isFalse();
    }

}
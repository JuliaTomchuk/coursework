package org.tms.travel_agency.validator.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tms.travel_agency.domain.Destination;
import org.tms.travel_agency.dto.destination.DestinationDetailsDto;
import org.tms.travel_agency.repository.DestinationRepository;

import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class DestinationValidatorTest {

    private DestinationRepository repository;
    private DestinationValidator validator;

    @BeforeEach
    void init() {
        repository = Mockito.mock(DestinationRepository.class);
        validator = new DestinationValidator(repository);
    }

    @Test
    void isUnique() {
        DestinationDetailsDto dto = new DestinationDetailsDto();
        dto.setName("TEST");
        Mockito.when(repository.findByNameIgnoreCase(dto.getName())).thenReturn(Optional.empty());
        boolean unique = validator.isUnique(dto);
        Assertions.assertThat(unique).isTrue();
    }

    @Test
    void isNotUnique() {
        DestinationDetailsDto dto = new DestinationDetailsDto();
        dto.setName("TEST");
        Mockito.when(repository.findByNameIgnoreCase(dto.getName())).thenReturn(Optional.of(new Destination()));
        boolean unique = validator.isUnique(dto);
        Assertions.assertThat(unique).isFalse();
    }


}
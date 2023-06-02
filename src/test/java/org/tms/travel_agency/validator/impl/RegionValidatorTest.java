package org.tms.travel_agency.validator.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.tms.travel_agency.domain.Region;
import org.tms.travel_agency.dto.region.RegionDetailsDto;
import org.tms.travel_agency.repository.RegionRepository;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class RegionValidatorTest {

    private RegionRepository repository;
    private RegionValidator validator;

    @BeforeEach
    public void init(){
        repository = Mockito.mock(RegionRepository.class);
        validator=new RegionValidator(repository);
    }


    @Test
    public void isUnique(){
        RegionDetailsDto dto= new RegionDetailsDto();
        dto.setName("TEST");
        Mockito.when(repository.findByNameIgnoreCase(dto.getName())).thenReturn(Optional.empty());
        boolean unique = validator.isUnique(dto);
        Assertions.assertThat(unique).isTrue();
    }
    @Test
    public void isNotUnique(){
        RegionDetailsDto dto= new RegionDetailsDto();
        dto.setName("TEST");
        Mockito.when(repository.findByNameIgnoreCase(dto.getName())).thenReturn(Optional.of(new Region()));
        boolean unique = validator.isUnique(dto);
        Assertions.assertThat(unique).isFalse();
    }

}
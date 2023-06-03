package org.tms.travel_agency.validator.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tms.travel_agency.domain.Room;
import org.tms.travel_agency.dto.room.RoomDetailsDto;
import org.tms.travel_agency.repository.RoomRepository;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class RoomValidatorTest {
    private RoomValidator validator;
    private RoomRepository repository;

    @BeforeEach
    void initServices() {
        repository = Mockito.mock(RoomRepository.class);
        validator = new RoomValidator(repository);
    }

    @Test
    void isUniqueSuccess() {
        RoomDetailsDto dto = new RoomDetailsDto();
        dto.setNumber(1);
        dto.setIdHotel(UUID.randomUUID());
        Mockito.when(repository.findByNumberAndHotelId(dto.getNumber(), dto.getIdHotel())).thenReturn(Optional.empty());
        boolean unique = validator.isUnique(dto);
        Assertions.assertThat(unique).isTrue();
    }

    @Test
    void isNotUnique() {
        RoomDetailsDto dto = new RoomDetailsDto();
        dto.setNumber(1);
        dto.setIdHotel(UUID.randomUUID());
        Mockito.when(repository.findByNumberAndHotelId(dto.getNumber(), dto.getIdHotel())).thenReturn(Optional.of(new Room()));
        boolean unique = validator.isUnique(dto);
        Assertions.assertThat(unique).isFalse();
    }
}
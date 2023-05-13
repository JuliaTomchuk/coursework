package org.tms.travel_agency.validator.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tms.travel_agency.domain.Room;
import org.tms.travel_agency.dto.room.RoomDetailsDto;
import org.tms.travel_agency.repository.RoomRepository;
import org.tms.travel_agency.validator.DuplicateValidator;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomValidator implements DuplicateValidator<RoomDetailsDto> {
    private final RoomRepository roomRepository;
    @Override
    @Transactional
    public boolean isUnique(RoomDetailsDto roomDetailsDto) {

        Optional<Room> byNumberAndHotelId = roomRepository.findByNumberAndHotelId(roomDetailsDto.getNumber(), roomDetailsDto.getIdHotel());
        if(byNumberAndHotelId.isEmpty()){
            return true;
        }
        return false;
    }
}

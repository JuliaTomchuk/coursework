package org.tms.travel_agency.services;

import org.tms.travel_agency.domain.Room;
import org.tms.travel_agency.dto.room.RoomDetailsDto;
import org.tms.travel_agency.dto.room.RoomLightDto;

import java.util.List;

public interface RoomService extends CRUDService<RoomDetailsDto, RoomLightDto>, BookingService<RoomDetailsDto,RoomLightDto>{
    List<RoomDetailsDto> getRoomsListForBooking(RoomDetailsDto dto);
}

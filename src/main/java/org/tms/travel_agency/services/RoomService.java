package org.tms.travel_agency.services;

import org.tms.travel_agency.dto.room.RoomDetailsDto;
import org.tms.travel_agency.dto.room.RoomLightDto;

public interface RoomService extends CRUDService<RoomDetailsDto, RoomLightDto>, BookingService<RoomDetailsDto,RoomLightDto>{
}

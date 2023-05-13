package org.tms.travel_agency.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.tms.travel_agency.domain.Hotel;
import org.tms.travel_agency.domain.Room;
import org.tms.travel_agency.dto.room.RoomDetailsDto;
import org.tms.travel_agency.dto.room.RoomLightDto;
import org.tms.travel_agency.exception.NoSuchHotelException;
import org.tms.travel_agency.repository.HotelRepository;


import java.util.List;

@Mapper(componentModel = "spring")
public abstract class RoomMapper {
    @Autowired
    private HotelRepository hotelRepository;

    public abstract Room convert(RoomDetailsDto dto);
    @AfterMapping
    public Room addHotel(RoomDetailsDto dto,@MappingTarget Room entity){
        if(dto.getIdHotel()!=null) {
            Hotel hotel = hotelRepository.findById(dto.getIdHotel()).orElseThrow(() -> new NoSuchHotelException("No hotel with id " + dto.getIdHotel()));
            entity.setHotel(hotel);
        }
        return entity;
    }
    @Mapping(target = "idHotel",source="hotel.id")
    @Mapping(target = "hotelName",source = "hotel.name")
    @Mapping(target ="destination",source = "hotel.region.destination.name")
    @Mapping(target="region", source = "hotel.region.name")
    public abstract RoomDetailsDto convert (Room room);

    @Mapping(target = "hotelName",source = "hotel.name")
    @Mapping(target ="destination",source = "hotel.region.destination.name")
    public abstract List<RoomLightDto> convert(List<Room> rooms);

    @Mapping(target = "hotelName",source = "hotel.name")
    @Mapping(target ="destination",source = "hotel.region.destination.name")
    public abstract RoomLightDto convertToLightDto(Room room);
    @Mapping(target="hotel",ignore = true)
       public abstract Room update(RoomDetailsDto dto, @MappingTarget Room entity);

    public abstract List<RoomDetailsDto> convertToListDto(List<Room> rooms);
}

package org.tms.travel_agency.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.tms.travel_agency.domain.Address;
import org.tms.travel_agency.domain.Hotel;
import org.tms.travel_agency.domain.RoomTypesByOccupancy;
import org.tms.travel_agency.domain.RoomTypesByView;
import org.tms.travel_agency.dto.hotel.HotelDetailsDto;
import org.tms.travel_agency.dto.hotel.HotelLightDto;
import org.tms.travel_agency.exception.NoSuchRegionException;
import org.tms.travel_agency.repository.AddressRepository;
import org.tms.travel_agency.repository.RegionRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Mapper(componentModel = "spring")
public abstract class HotelMapper {
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Mapping(target="region",ignore = true)
    @Mapping(target="basicPriceOfRoomPerDay", source = "basicPriceOfRoom")
    public abstract  Hotel convert(HotelDetailsDto dto);
    @AfterMapping
    public Hotel addAddressAndRegion(HotelDetailsDto dto, @MappingTarget Hotel entity){
        Address  address = new Address(dto.getCity(), dto.getStreet(), dto.getHome());
        Optional<Address> byCityAndStreetAndHome = addressRepository.findIdentical(dto.getCity(), dto.getStreet(), dto.getHome());
        if(byCityAndStreetAndHome.isEmpty()) {
            Address save = addressRepository.save(address);
            entity.setAddress(save);
        }else{
            entity.setAddress(byCityAndStreetAndHome.get());
        }
        entity.setRegion(regionRepository.findByNameIgnoreCase(dto.getRegion()).orElseThrow(() -> new NoSuchRegionException("no region with name: " + dto.getRegion())));
        return entity;
    }

    @Mapping(target = "region", source = "region.name")
    @Mapping(target = "street", source = "address.street")
    @Mapping(target = "city", source = "address.city")
    @Mapping(target = "home", source = "address.home")
    @Mapping(target="basicPriceOfRoom", source = "basicPriceOfRoomPerDay")
    public abstract HotelDetailsDto convert(Hotel hotel);
     @AfterMapping
     public HotelDetailsDto createSetOfRoomTypes(Hotel entity, @MappingTarget HotelDetailsDto dto){
          Set<RoomTypesByView> roomTypesByViewSet = new HashSet<>();
          Set <RoomTypesByOccupancy> roomTypesByOccupancySet = new HashSet<>();
          entity.getRooms().forEach(r->{roomTypesByViewSet.add(r.getTypesByView());
              roomTypesByOccupancySet.add(r.getTypesByOccupancy());
          });
          dto.setRoomTypesByViewSet(roomTypesByViewSet);
          dto.setRoomTypesByOccupancySet(roomTypesByOccupancySet);
          return dto;
     }

    public abstract List<HotelLightDto> convert(List<Hotel> hotels);

    @Mapping(target="id", ignore = true)
    @Mapping(target="rooms", ignore = true)
    @Mapping(target="address", ignore = true)
    @Mapping(target="region", ignore = true)
    @Mapping(target="boardBasisSet", ignore = true)
    @Mapping(target="reviews", ignore = true)
    public abstract Hotel update(HotelDetailsDto dto,@MappingTarget Hotel entity);
}

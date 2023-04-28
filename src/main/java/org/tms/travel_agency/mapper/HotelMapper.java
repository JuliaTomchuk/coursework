package org.tms.travel_agency.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.tms.travel_agency.domain.Address;
import org.tms.travel_agency.domain.Hotel;
import org.tms.travel_agency.domain.Review;
import org.tms.travel_agency.dto.hotel.HotelDetailsDto;
import org.tms.travel_agency.dto.hotel.HotelLightDto;
import org.tms.travel_agency.dto.review.ReviewLightDto;
import org.tms.travel_agency.exception.NoSuchRegionException;
import org.tms.travel_agency.repository.AddressRepository;
import org.tms.travel_agency.repository.RegionRepository;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class HotelMapper {
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private AddressRepository addressRepository;

    public Hotel convert(HotelDetailsDto dto) {
        Hotel hotel = new Hotel();
        hotel.setName(dto.getName());
        Address  address = new Address(dto.getCity(), dto.getStreet(), dto.getHome());
        Address saved= addressRepository.save(address);
        hotel.setAddress(saved);
        hotel.setRegion(regionRepository.findByNameIgnoreCase(dto.getRegion()).orElseThrow(() -> new NoSuchRegionException("no region with name: " + dto.getRegion())));
        hotel.setDescription(dto.getDescription());
        hotel.setTypeByStars(dto.getTypeByStars());
        hotel.setTypeByTargetMarket(dto.getTypeByTargetMarket());
        hotel.setBasicPriceOfRoomPerDay(dto.getBasicPriceOfRoom());
        return hotel;

    }

    @Mapping(target = "region", source = "region.name")
    @Mapping(target = "street", source = "address.street")
    @Mapping(target = "city", source = "address.city")
    @Mapping(target = "home", source = "address.home")
    @Mapping(target="basicPriceOfRoom", source = "basicPriceOfRoomPerDay")
    public abstract HotelDetailsDto convert(Hotel hotel);
     public abstract ReviewLightDto convert(Review value);

    public abstract List<HotelLightDto> convert(List<Hotel> hotels);

    public Hotel update(HotelDetailsDto dto, Hotel entity){
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setBasicPriceOfRoomPerDay(dto.getBasicPriceOfRoom());
        entity.setTypeByTargetMarket(dto.getTypeByTargetMarket());
        entity.setTypeByStars(dto.getTypeByStars());
        return entity;

    }
}

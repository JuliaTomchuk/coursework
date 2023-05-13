package org.tms.travel_agency.services;

import org.tms.travel_agency.domain.BoardBasisTypes;
import org.tms.travel_agency.dto.hotel.HotelDetailsDto;
import org.tms.travel_agency.dto.hotel.HotelLightDto;

import java.util.List;
import java.util.UUID;

public interface HotelService extends CRUDService <HotelDetailsDto, HotelLightDto> {
    List<HotelLightDto> getByRegionName(String name);
    void addBoardBasis(BoardBasisTypes type, UUID id);
    void deleteBoardBasis(BoardBasisTypes type, UUID id);

}

package org.tms.travel_agency.services;

import org.tms.travel_agency.dto.region.RegionDetailsDto;
import org.tms.travel_agency.dto.region.RegionLightDto;
import org.tms.travel_agency.dto.room.RoomLightDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface RegionService extends CRUDService<RegionDetailsDto, RegionLightDto>{
     Map<String,List<RegionLightDto>> getRegionsByDestinations();

}

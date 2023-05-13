package org.tms.travel_agency.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.tms.travel_agency.domain.Destination;
import org.tms.travel_agency.dto.destination.DestinationDetailsDto;
import org.tms.travel_agency.dto.destination.DestinationLightDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DestinationMapper {

    DestinationDetailsDto convert (Destination destination);
    Destination convert (DestinationDetailsDto destinationDetailsDto);
    List<DestinationLightDto> convert(List <Destination> destinations);
    @Mapping(target="id", ignore = true)
    Destination update(DestinationDetailsDto dto,@MappingTarget Destination destination);
}

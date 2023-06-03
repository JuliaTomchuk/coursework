package org.tms.travel_agency.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.tms.travel_agency.domain.Destination;
import org.tms.travel_agency.domain.Region;
import org.tms.travel_agency.dto.region.RegionDetailsDto;
import org.tms.travel_agency.dto.region.RegionLightDto;
import org.tms.travel_agency.exception.NoSuchDestinationException;
import org.tms.travel_agency.repository.DestinationRepository;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public abstract class RegionMapper {
    @Autowired
    private DestinationRepository repository;

    @Mapping(target="destination", ignore = true)
    public abstract  Region convert(RegionDetailsDto dto);
    @AfterMapping
    public Region convert(RegionDetailsDto dto, @MappingTarget Region entity){
        Destination destination = repository.findByNameIgnoreCase(dto.getDestination()).orElseThrow( ()-> new NoSuchDestinationException("no destination with name: "+dto.getDestination()));
        Set<Region> regions = destination.getRegions();
        if(!regions.contains(entity)) {
            destination.addRegion(entity);
        }
        return entity;
    }

    @Mapping (source = "destination.name", target = "destination")
    public abstract RegionDetailsDto convert(Region region);
    public abstract List<RegionLightDto> convert(List <Region> regionList);

     public Region update( RegionDetailsDto dto,  Region entity){
         Destination destination = repository.findByNameIgnoreCase(dto.getDestination()).orElseThrow(()-> new NoSuchDestinationException("no destination with name: "+dto.getDestination()));
         destination.addRegion(entity);
         entity.setDescription(dto.getDescription());
         return entity;
     }
}

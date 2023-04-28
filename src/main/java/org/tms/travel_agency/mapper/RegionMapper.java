package org.tms.travel_agency.mapper;

import lombok.AllArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tms.travel_agency.domain.Destination;
import org.tms.travel_agency.domain.Region;
import org.tms.travel_agency.dto.region.RegionDetailsDto;
import org.tms.travel_agency.dto.region.RegionLightDto;
import org.tms.travel_agency.exception.NoSuchDestinationException;
import org.tms.travel_agency.repository.DestinationRepository;
import org.tms.travel_agency.services.DestinationService;
import org.tms.travel_agency.services.impl.DestinationServiceImpl;

import javax.transaction.Transactional;
import java.util.List;
@Service
@Mapper(componentModel = "spring")
public abstract class RegionMapper {
    @Autowired
    private DestinationRepository repository;



    public  Region convert(RegionDetailsDto dto){
        Region entity = new Region();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        Destination destination = repository.findByNameIgnoreCase(dto.getDestination()).orElseThrow( ()-> new NoSuchDestinationException("no destination with name: "+dto.getDestination()));
        destination.addRegion(entity);
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

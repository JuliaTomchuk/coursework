package org.tms.travel_agency.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.tms.travel_agency.domain.Destination;
import org.tms.travel_agency.domain.Region;
import org.tms.travel_agency.dto.destination.DestinationLightDto;
import org.tms.travel_agency.dto.region.RegionDetailsDto;
import org.tms.travel_agency.dto.region.RegionLightDto;
import org.tms.travel_agency.dto.room.RoomLightDto;
import org.tms.travel_agency.exception.DuplicateRegionException;
import org.tms.travel_agency.exception.NoSuchRegionException;
import org.tms.travel_agency.mapper.RegionMapper;
import org.tms.travel_agency.repository.RegionRepository;
import org.tms.travel_agency.services.DestinationService;
import org.tms.travel_agency.services.RegionService;
import org.tms.travel_agency.validator.DuplicateValidator;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
@Service
@AllArgsConstructor
public class RegionServiceImpl implements RegionService {
    private final RegionRepository repository;
    private final DuplicateValidator<RegionDetailsDto> validator;
    private final RegionMapper mapper;
    private final DestinationService service;
    @Override
    @Transactional
    public RegionDetailsDto save(RegionDetailsDto dto) {
        if (!validator.isUnique(dto)){
            throw new DuplicateRegionException("region with name " + dto.getName()+" already exist");
        }
        Region region = mapper.convert(dto);
        Region saved = repository.save(region);
        return mapper.convert(saved);

    }

    @Override
    public List<RegionLightDto> getAll() {
        List<Region> regions = repository.findAll();
        return mapper.convert(regions);
    }

    @Override
    public RegionDetailsDto getById(UUID id) {
       Region region = repository.findById(id).orElseThrow(()->new NoSuchRegionException("no region with id: "+ id));
       return mapper.convert(region);
    }

    @Override
    @Transactional
    public RegionDetailsDto update(RegionDetailsDto dto) {
        Region region = repository.findByNameIgnoreCase(dto.getName()).orElseThrow(() -> new NoSuchRegionException("no region with name: " + dto.getName()));
        Region updated = mapper.update(dto, region);
        Region saved = repository.save(updated);
        return mapper.convert(saved);
    }

    @Override
    public void delete(UUID id) {
        repository.findById(id).ifPresentOrElse(region->repository.delete(region), ()-> new NoSuchRegionException("no region with id: "+id));
    }

    @Override
    @Transactional
    public Map<String,List<RegionLightDto>> getRegionsByDestinations(){
        Map<String,List<RegionLightDto>> regionsByDestination=new HashMap<>();
        List<DestinationLightDto> destinations = service.getAll();

        destinations.stream().forEach((destination)->{
            List<Region> regions = repository.findByDestinationNameIgnoreCase(destination.getName());
            List<RegionLightDto> convert = mapper.convert(regions);
            regionsByDestination.put(destination.getName(),convert);
        });
        return regionsByDestination;
    }

    @Override
    public List<RegionLightDto> getByDestinationName(String name) {
        List<Region> regions = repository.findByDestinationNameIgnoreCase(name);
        return mapper.convert(regions);
    }


}

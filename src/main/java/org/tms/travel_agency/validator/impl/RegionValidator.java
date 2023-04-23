package org.tms.travel_agency.validator.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.tms.travel_agency.domain.Region;
import org.tms.travel_agency.repository.RegionRepository;
import org.tms.travel_agency.validator.DuplicateValidator;

import java.util.Optional;

@AllArgsConstructor
@Service
public class RegionValidator implements DuplicateValidator<Region> {

    private final RegionRepository repository;
    @Override
    public boolean isUnique(String name) {
        Optional<Region> regionOptional = repository.findByNameIgnoreCase(name);
        if(regionOptional.isPresent())
            return false;
        return true;
    }
}

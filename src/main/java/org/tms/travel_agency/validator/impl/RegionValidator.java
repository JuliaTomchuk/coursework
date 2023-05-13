package org.tms.travel_agency.validator.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tms.travel_agency.domain.Region;
import org.tms.travel_agency.dto.region.RegionDetailsDto;
import org.tms.travel_agency.repository.RegionRepository;
import org.tms.travel_agency.validator.DuplicateValidator;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RegionValidator implements DuplicateValidator<RegionDetailsDto> {

    private final RegionRepository repository;
    @Override
    public boolean isUnique(RegionDetailsDto dto) {

        return repository.findByNameIgnoreCase(dto.getName()).isEmpty();

    }
}

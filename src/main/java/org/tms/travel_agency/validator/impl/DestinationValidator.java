package org.tms.travel_agency.validator.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tms.travel_agency.domain.Destination;
import org.tms.travel_agency.dto.destination.DestinationDetailsDto;
import org.tms.travel_agency.repository.DestinationRepository;
import org.tms.travel_agency.validator.DuplicateValidator;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DestinationValidator implements DuplicateValidator<DestinationDetailsDto> {
    private final DestinationRepository repository;
    @Override
    public boolean isUnique(DestinationDetailsDto dto) {
       return repository.findByNameIgnoreCase(dto.getName()).isEmpty();

}}

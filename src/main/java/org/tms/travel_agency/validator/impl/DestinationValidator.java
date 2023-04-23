package org.tms.travel_agency.validator.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.tms.travel_agency.domain.Destination;
import org.tms.travel_agency.repository.DestinationRepository;
import org.tms.travel_agency.validator.DuplicateValidator;

import java.util.Optional;

@AllArgsConstructor
@Service
public class DestinationValidator implements DuplicateValidator<Destination> {
    private final DestinationRepository repository;
    @Override
    public boolean isUnique(String name) {
        Optional<Destination> destination = repository.findByNameIgnoreCase(name);
        if(destination.isEmpty()){
            return true;
        }
        return false;
    }
}

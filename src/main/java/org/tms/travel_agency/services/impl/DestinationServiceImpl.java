package org.tms.travel_agency.services.impl;

import lombok.AllArgsConstructor;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tms.travel_agency.domain.Destination;
import org.tms.travel_agency.repository.DestinationRepository;
import org.tms.travel_agency.services.DestinationService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@NoArgsConstructor
@AllArgsConstructor
@Service
public class DestinationServiceImpl implements DestinationService {


    @Autowired
    private   DestinationRepository destinationRepository;



    @Override
    public List<Destination> getAll() {
        //add pagination and sort
        return destinationRepository.findAll();
    }

    @Override
    public Destination getByName(String name) {
       return destinationRepository.findByNameIgnoreCase(name);
    }

    @Override
    @Transactional
    public String getDescription(UUID id) {
        Optional<Destination> byId = destinationRepository.findById(id);
        return byId.stream().map(d->d.getDescription()).findFirst().orElse("");
    }
}

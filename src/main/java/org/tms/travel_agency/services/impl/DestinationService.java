package org.tms.travel_agency.services.impl;

import lombok.AllArgsConstructor;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tms.travel_agency.domain.Destination;
import org.tms.travel_agency.repository.DestinationRepository;
import org.tms.travel_agency.services.TravelUserService;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Service
public class DestinationService implements TravelUserService<Destination> {


    @Autowired
    private   DestinationRepository destinationRepository;



    @Override
    public List<Destination> getAll() {
        //add pagination and sort
        return destinationRepository.findAll();
    }

    @Override
    public Destination getById() {
        return null;
    }

    @Override
    public List<Destination> search(Destination destination) {
        return null;
    }


}

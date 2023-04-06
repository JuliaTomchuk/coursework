package org.tms.travel_agency.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.tms.travel_agency.domain.Destination;
import org.tms.travel_agency.domain.Region;
import org.tms.travel_agency.repository.DestinationRepository;
import org.tms.travel_agency.services.TravelAdminService;
import org.tms.travel_agency.services.OneComponentJoin;

import java.util.UUID;

@Service
@AllArgsConstructor
public class DestinationServiceForAdmin extends DestinationService implements TravelAdminService<Destination>, OneComponentJoin<Destination,Region> {
   private final DestinationRepository destinationRepository;

    @Override
    public Destination save(Destination destination) {
        Destination save = destinationRepository.save(destination);
        return save;
    }

    @Override
    public void delete(UUID id) {
        destinationRepository.deleteById(id);
    }



    @Override
    public Destination update(Destination destination) {

        return null;
    }


    @Override
    public void addComponent(UUID idObject, UUID idComponent) {

    }
}

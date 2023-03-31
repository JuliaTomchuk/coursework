package org.tms.travel_agency.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.tms.travel_agency.domain.Destination;
import org.tms.travel_agency.repository.DestinationRepository;
import org.tms.travel_agency.services.DestinationServiceForAdmin;

import java.util.UUID;

@Service
@AllArgsConstructor
public class DestinationServiceForAdminImpl extends DestinationServiceImpl implements DestinationServiceForAdmin {
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
    public boolean update(Destination destination) {
        return false;
    }
}

package org.tms.travel_agency.services;

import org.tms.travel_agency.domain.Destination;

import java.util.UUID;

public interface DestinationServiceForAdmin extends DestinationService {

    Destination save(Destination destination);
    void delete(UUID id);
    boolean update(Destination destination);

}

package org.tms.travel_agency.services;

import org.tms.travel_agency.domain.Destination;

import java.util.List;
import java.util.UUID;

public interface DestinationService {

    List<Destination> getAll();
    Destination getByName(String name);
    String getDescription(UUID id);

}

package org.tms.travel_agency.services;

import org.tms.travel_agency.domain.Destination;

import java.util.Set;

public interface DestinationService {

    Set<Destination> getAll();
    Destination getByName(String name);
    String getDescription(Integer id);

}

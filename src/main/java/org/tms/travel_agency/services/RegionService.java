package org.tms.travel_agency.services;

import org.tms.travel_agency.domain.Destination;
import org.tms.travel_agency.domain.Region;

import java.util.Set;

public interface RegionService {

    Set<Region> getAllRegionByDestination(Destination destination);

}

package org.tms.travel_agency.services;

import org.tms.travel_agency.domain.Destination;

public interface DestinationServiceForAdmin  {

    boolean addDestination(Destination destination);
    boolean deleteDestination(Destination destination);

}

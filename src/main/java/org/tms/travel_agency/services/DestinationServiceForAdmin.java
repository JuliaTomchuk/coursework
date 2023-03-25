package org.tms.travel_agency.services;

import org.tms.travel_agency.domain.Destination;

public interface DestinationServiceForAdmin  {

    boolean save(Destination destination);
    boolean delete(Integer id);
    boolean update(Destination destination);

}

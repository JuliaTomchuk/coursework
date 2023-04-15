package org.tms.travel_agency.services;

import java.util.UUID;

public interface TravelAdminService<T , V >  {
    V save (T inputDto);
    V update (T inputDto);
    void delete (UUID id);
    T getCurrent(String name);

}

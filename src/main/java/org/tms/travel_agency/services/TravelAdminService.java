package org.tms.travel_agency.services;

import java.util.UUID;

public interface TravelAdminService<T> extends TravelUserService<T> {
    T save (T t);
    T update (T t);
    void delete (UUID id);

}

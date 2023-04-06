package org.tms.travel_agency.services;

import java.util.List;

public interface TravelUserService<T> {

    List<T> getAll();
    T getById();
    List<T> search(T t);
}

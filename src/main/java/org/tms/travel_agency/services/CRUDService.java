package org.tms.travel_agency.services;

import java.util.List;
import java.util.UUID;

public interface CRUDService <T,K> {
    T save(T t);
    List<K> getAll();
    T getById(UUID id);
    T update(T t);
    void delete(UUID id);




}

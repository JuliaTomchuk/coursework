package org.tms.travel_agency.services;

import java.util.UUID;

public interface OneComponentJoin <T,V>{
    void addComponent(UUID idObject, UUID idComponent);
}

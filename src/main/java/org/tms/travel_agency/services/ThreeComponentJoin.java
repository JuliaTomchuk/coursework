package org.tms.travel_agency.services;

import java.util.UUID;

public interface ThreeComponentJoin <M,T,V,K>{
    void addFirstComponent(UUID idObject, UUID idComponent);
    void addSecondComponent(UUID idObject, UUID idComponent);
    void addThirdComponent(UUID idObject, UUID idComponent);

}

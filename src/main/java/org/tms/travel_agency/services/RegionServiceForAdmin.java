package org.tms.travel_agency.services;

import org.tms.travel_agency.domain.Region;

import java.util.UUID;

public interface RegionServiceForAdmin {
    boolean saveRegion(Region region);
    boolean deleteRegion(UUID id );
}

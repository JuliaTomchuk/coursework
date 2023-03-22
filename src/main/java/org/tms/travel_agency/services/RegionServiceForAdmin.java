package org.tms.travel_agency.services;

import org.tms.travel_agency.domain.Region;

public interface RegionServiceForAdmin {
    boolean addRegion(Region region);
    boolean deleteRegion(Region region);
}

package org.tms.travel_agency.services.impl;

import org.tms.travel_agency.domain.BoardBasis;
import org.tms.travel_agency.services.TravelAdminService;

import java.util.UUID;

public class BoardBasisServiceForAdmin extends BoardBasisService implements TravelAdminService<BoardBasis> {
    @Override
    public BoardBasis save(BoardBasis boardBasis) {
        return null;
    }

    @Override
    public BoardBasis update(BoardBasis boardBasis) {
        return null;
    }

    @Override
    public void delete(UUID id) {

    }
}

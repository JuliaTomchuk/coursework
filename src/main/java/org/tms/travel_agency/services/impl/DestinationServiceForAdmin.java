package org.tms.travel_agency.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.tms.travel_agency.domain.Destination;
import org.tms.travel_agency.domain.OneWayFlight;
import org.tms.travel_agency.domain.Region;
import org.tms.travel_agency.domain.RoundTrip;
import org.tms.travel_agency.dto.destination.DestinationFullDescriptionDto;
import org.tms.travel_agency.dto.destination.DestinationInputDto;
import org.tms.travel_agency.repository.DestinationRepository;
import org.tms.travel_agency.services.ThreeComponentJoin;
import org.tms.travel_agency.services.TravelAdminService;
import org.tms.travel_agency.services.OneComponentJoin;

import java.util.UUID;

@Service
@AllArgsConstructor
public class DestinationServiceForAdmin implements TravelAdminService <DestinationInputDto, DestinationFullDescriptionDto> {
    @Override
    public DestinationFullDescriptionDto save(DestinationInputDto inputDto) {
        return null;
    }

    @Override
    public DestinationFullDescriptionDto update(DestinationInputDto inputDto) {
        return null;
    }

    @Override
    public void delete(UUID id) {

    }

    @Override
    public DestinationInputDto getCurrent(String name) {
        return null;
    }
}

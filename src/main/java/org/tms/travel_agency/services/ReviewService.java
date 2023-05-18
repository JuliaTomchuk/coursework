package org.tms.travel_agency.services;

import org.tms.travel_agency.dto.review.ReviewDetailsDto;
import org.tms.travel_agency.dto.review.ReviewLightDto;
import org.tms.travel_agency.services.CRUDService;

import java.util.List;
import java.util.UUID;

public interface ReviewService extends CRUDService<ReviewDetailsDto, ReviewLightDto> {
    List<ReviewLightDto> getByHotel(UUID hotelId);
}

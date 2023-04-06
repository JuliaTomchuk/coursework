package org.tms.travel_agency.services.impl;

import org.tms.travel_agency.domain.Review;
import org.tms.travel_agency.services.TravelAdminService;

import java.util.UUID;

public class ReviewServiceForAdmin extends ReviewService implements TravelAdminService<Review> {


    @Override
    public Review save(Review review) {
        return null;
    }

    @Override
    public Review update(Review review) {
        return null;
    }

    @Override
    public void delete(UUID id) {

    }
}

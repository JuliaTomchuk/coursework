package org.tms.travel_agency.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.tms.travel_agency.domain.Review;
import org.tms.travel_agency.dto.review.ReviewDetailsDto;
import org.tms.travel_agency.dto.review.ReviewLightDto;
import org.tms.travel_agency.exception.NoSuchReviewException;
import org.tms.travel_agency.mapper.ReviewMapper;
import org.tms.travel_agency.repository.ReviewRepository;
import org.tms.travel_agency.services.ReviewService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository repository;
    private final ReviewMapper mapper;
    @Override
    public ReviewDetailsDto save(ReviewDetailsDto reviewDetailsDto) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        reviewDetailsDto.setDate(LocalDate.now());
        reviewDetailsDto.setUsername(name);
        Review convert = mapper.convert(reviewDetailsDto);
        Review save = repository.save(convert);
        return mapper.convert(save);
    }

    @Override
    public List<ReviewLightDto> getAll() {
        List<Review> all = repository.findAll();
        return mapper.convert(all);
    }

    @Override
    public ReviewDetailsDto getById(UUID id) {
        Review review = findById(id);
        return mapper.convert(review);
    }

    @Override
    public ReviewDetailsDto update(ReviewDetailsDto reviewDetailsDto) {
        Review byId = findById(reviewDetailsDto.getId());
        Review update = mapper.update(reviewDetailsDto, byId);
        Review save = repository.save(update);
        return mapper.convert(save);
    }

    @Override
    public void delete(UUID id) {
     repository.findById(id).ifPresentOrElse((review)->repository.delete(review),()-> {throw new NoSuchReviewException("no review with id: "+id);});
    }

    @Override
    public List<ReviewLightDto> getByHotel(UUID hotelId) {
        List<Review> byHotelId = repository.findByHotelId(hotelId);
        return mapper.convert(byHotelId);
    }
    private Review findById(UUID id){
        return repository.findById(id).orElseThrow(() -> new NoSuchReviewException("no review with id: " + id));
    }
}

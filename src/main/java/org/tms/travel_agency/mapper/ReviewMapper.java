package org.tms.travel_agency.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.tms.travel_agency.domain.Hotel;
import org.tms.travel_agency.domain.Review;
import org.tms.travel_agency.domain.User;
import org.tms.travel_agency.dto.review.ReviewDetailsDto;
import org.tms.travel_agency.dto.review.ReviewLightDto;
import org.tms.travel_agency.exception.NoSuchHotelException;
import org.tms.travel_agency.exception.NoSuchUserException;
import org.tms.travel_agency.repository.HotelRepository;
import org.tms.travel_agency.repository.UserRepository;
import org.tms.travel_agency.services.HotelService;
import org.tms.travel_agency.services.UserService;

import java.util.List;


@Mapper(componentModel = "spring")
public abstract class ReviewMapper {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HotelRepository hotelRepository;


   public abstract List<ReviewLightDto> convert (List <Review> reviews);

   public abstract Review convert (ReviewDetailsDto dto);
   
   @AfterMapping
    public Review addUserAndHotel(ReviewDetailsDto dto, @MappingTarget Review entity){
       User user = userRepository.findByUsername(dto.getUsername()).orElseThrow(() -> new NoSuchUserException("no user with username: " + dto.getUsername()));
       entity.setUser(user);
       Hotel hotel = hotelRepository.findById(dto.getHotelId()).orElseThrow(() -> new NoSuchHotelException("no hotel with id: " + dto.getHotelId()));
       entity.setHotel(hotel);
       return entity;
   }
   @Mapping(target = "hotelId", source = "hotel.id")
   @Mapping(target="username", source = "user.username")
   public abstract ReviewDetailsDto convert(Review review);

   @Mapping(target="user",ignore = true)
   @Mapping(target="hotel",ignore = true)
   @Mapping(target="id",ignore = true)
   @Mapping(target="date",ignore = true)
   public abstract Review update(ReviewDetailsDto dto,@MappingTarget Review entity);



}

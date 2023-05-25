package org.tms.travel_agency.services.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tms.travel_agency.domain.Hotel;
import org.tms.travel_agency.domain.Rating;
import org.tms.travel_agency.domain.Review;
import org.tms.travel_agency.domain.User;
import org.tms.travel_agency.dto.review.ReviewDetailsDto;
import org.tms.travel_agency.dto.review.ReviewLightDto;
import org.tms.travel_agency.exception.NoSuchReviewException;
import org.tms.travel_agency.mapper.ReviewMapper;
import org.tms.travel_agency.repository.ReviewRepository;
import org.tms.travel_agency.services.ReviewService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration
class ReviewServiceImplTest {
    private  ReviewRepository repository;
    private  ReviewMapper mapper;
    private ReviewService service;
    @BeforeEach
    public void init(){
        repository= Mockito.mock(ReviewRepository.class);
        mapper =Mockito.mock(ReviewMapper.class);
        service = new ReviewServiceImpl(repository,mapper);
    }

    @ParameterizedTest
    @MethodSource("save")
    @WithMockUser
    public void save(ReviewDetailsDto input, ReviewDetailsDto convert,Review entity){
        Mockito.when(mapper.convert(input)).thenReturn(entity);
        Mockito.when(repository.save(entity)).thenReturn(entity);
        Mockito.when(mapper.convert(entity)).thenReturn(convert);
        ReviewDetailsDto saved = service.save(input);
        Assertions.assertThat(convert).isEqualTo(saved);

    }
    @ParameterizedTest
    @MethodSource("findAll")
    public void findAll(List<Review> entityList, List<ReviewLightDto> dtoList){
        Mockito.when(repository.findAll()).thenReturn(entityList);
        Mockito.when(mapper.convert(entityList)).thenReturn(dtoList);
        List<ReviewLightDto> all = service.getAll();
        Assertions.assertThat(all).isEqualTo(dtoList);
    }
    @Test
    public void getByIdIfNotExist(){
        UUID id = UUID.randomUUID();
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(NoSuchReviewException.class).isThrownBy(()->service.getById(id));
    }
    @ParameterizedTest
    @MethodSource("getEntityAndDto")
    public void getByIdSuccess(Review entity, ReviewDetailsDto dto){
        Mockito.when(repository.findById(dto.getId())).thenReturn(Optional.of(entity));
        Mockito.when(mapper.convert(entity)).thenReturn(dto);
        ReviewDetailsDto byId = service.getById(dto.getId());
        Assertions.assertThat(byId).isEqualTo(dto);
    }
    @Test
    public void updateIfNotExist(){
        UUID id =UUID.randomUUID();
        ReviewDetailsDto dto = new ReviewDetailsDto();
        dto.setId(id);
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(NoSuchReviewException.class).isThrownBy(()->service.update(dto));
    }
    @ParameterizedTest
    @MethodSource("getEntityAndDto")
    public void updateSuccess(Review entity, ReviewDetailsDto dto){
        Mockito.when(repository.findById(dto.getId())).thenReturn(Optional.of(entity));
        Mockito.when(mapper.update(dto,entity)).thenReturn(entity);
        Mockito.when(repository.save(entity)).thenReturn(entity);
        Mockito.when(mapper.convert(entity)).thenReturn(dto);
        ReviewDetailsDto update = service.update(dto);
        Assertions.assertThat(update).isEqualTo(dto);
    }

    @Test
    public void deleteIfNotExist(){
        UUID id =UUID.randomUUID();
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(NoSuchReviewException.class).isThrownBy(()->service.delete(id));
    }
    @ParameterizedTest
    @MethodSource("getEntity")
    public void deleteSuccess(Review entity){
        Mockito.when(repository.findById(entity.getId())).thenReturn(Optional.of(entity));
        service.delete(entity.getId());
        Mockito.verify(repository,Mockito.times(1)).delete(entity);
        ArgumentCaptor<Review> reviewArgumentCaptor = ArgumentCaptor.forClass(Review.class);
        Mockito.verify(repository).delete(reviewArgumentCaptor.capture());
        Review value = reviewArgumentCaptor.getValue();
        Assertions.assertThat(value).isEqualTo(entity);
    }

    @ParameterizedTest
    @MethodSource("findAll")
    public void getByHotel(List<Review> entityList, List<ReviewLightDto> dtoList){
        UUID id= UUID.randomUUID();
        Mockito.when(repository.findByHotelId(id)).thenReturn(entityList);
        Mockito.when(mapper.convert(entityList)).thenReturn(dtoList);
        List<ReviewLightDto> resultList = service.getByHotel(id);
        Assertions.assertThat(resultList).isEqualTo(dtoList);

    }
    public static Stream<Arguments> save(){
        ReviewDetailsDto dtoInput = new ReviewDetailsDto();
        dtoInput.setHotelId(UUID.randomUUID());
        dtoInput.setRating(Rating.EXCELLENT);
        dtoInput.setMessage("TEST");

        Review entity = new Review();
        User user = new User();
        user.setUsername(dtoInput.getUsername());
        entity.setDate(LocalDate.now());
        entity.setId(UUID.randomUUID());
        entity.setRating(dtoInput.getRating());
        entity.setMessage(dtoInput.getMessage());
        entity.setUser(user);
        Hotel hotel = new Hotel();
        hotel.setId(dtoInput.getHotelId());
        entity.setHotel(hotel);

        ReviewDetailsDto convert = new ReviewDetailsDto();
        convert.setMessage(dtoInput.getMessage());
        convert.setId(entity.getId());
        convert.setDate(entity.getDate());
        convert.setRating(dtoInput.getRating());
        convert.setHotelId(dtoInput.getHotelId());
        convert.setUsername(entity.getUser().getUsername());
        return Stream.of(Arguments.arguments(dtoInput,convert,entity));
    }
    public static Stream<Arguments> findAll(){
        List<Review> entityList = new ArrayList<>();
        Review entity = new Review();
        User user = new User();
        user.setUsername("user");
        entity.setDate(LocalDate.now());
        entity.setId(UUID.randomUUID());
        entity.setRating(Rating.EXCELLENT);
        entity.setMessage("TEST");
        entity.setUser(user);
        Hotel hotel = new Hotel();
        hotel.setId(UUID.randomUUID());
        entity.setHotel(hotel);

        Review entity2 = new Review();
        entity2.setDate(LocalDate.now());
        entity2.setId(UUID.randomUUID());
        entity2.setRating(Rating.VERY_GOOD);
        entity2.setMessage("TEST2");
        entity2.setUser(user);
        entity2.setHotel(hotel);
        entityList.add(entity);
        entityList.add(entity2);

        List<ReviewLightDto> dtoList= new ArrayList<>();
        ReviewLightDto dto= new ReviewLightDto();
        dto.setId(entity.getId());
        dto.setDate(entity.getDate());
        dto.setRating(entity.getRating());
        ReviewLightDto dto2 = new ReviewLightDto();
        dto2.setId(entity2.getId());
        dto2.setRating(entity2.getRating());
        dto2.setDate(entity2.getDate());
        dtoList.add(dto);
        dtoList.add(dto2);
        return Stream.of(Arguments.arguments(entityList,dtoList));

    }

    public static Stream<Arguments> getEntityAndDto(){
        Review entity = new Review();
        User user = new User();
        user.setUsername("user");
        entity.setDate(LocalDate.now());
        entity.setId(UUID.randomUUID());
        entity.setRating(Rating.EXCELLENT);
        entity.setMessage("TEST");
        entity.setUser(user);
        Hotel hotel = new Hotel();
        hotel.setId(UUID.randomUUID());
        entity.setHotel(hotel);

        ReviewDetailsDto dto = new ReviewDetailsDto();
        dto.setMessage(entity.getMessage());
        dto.setId(entity.getId());
        dto.setDate(entity.getDate());
        dto.setRating(entity.getRating());
        dto.setHotelId(hotel.getId());
        dto.setUsername(user.getUsername());
        return Stream.of(Arguments.arguments(entity,dto));
    }
    public static Stream<Review> getEntity(){
        Review entity = new Review();
        User user = new User();
        user.setUsername("user");
        entity.setDate(LocalDate.now());
        entity.setId(UUID.randomUUID());
        entity.setRating(Rating.EXCELLENT);
        entity.setMessage("TEST");
        entity.setUser(user);
        Hotel hotel = new Hotel();
        hotel.setId(UUID.randomUUID());
        entity.setHotel(hotel);
        return Stream.of(entity);
    }


}
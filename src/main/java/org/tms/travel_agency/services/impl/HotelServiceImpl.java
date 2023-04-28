package org.tms.travel_agency.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tms.travel_agency.domain.Hotel;
import org.tms.travel_agency.dto.hotel.HotelDetailsDto;
import org.tms.travel_agency.dto.hotel.HotelLightDto;
import org.tms.travel_agency.exception.DuplicateHotelException;
import org.tms.travel_agency.exception.NoSuchHotelException;
import org.tms.travel_agency.mapper.HotelMapper;
import org.tms.travel_agency.repository.HotelRepository;
import org.tms.travel_agency.services.HotelService;
import org.tms.travel_agency.validator.DuplicateValidator;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {
 private final HotelRepository repository;
 private final HotelMapper mapper;
 private final DuplicateValidator<HotelDetailsDto> validator;
    @Override
    @Transactional
    public HotelDetailsDto save(HotelDetailsDto hotelDetailsDto) {

        if(!validator.isUnique(hotelDetailsDto)){
            throw new DuplicateHotelException("hotel with region "+ hotelDetailsDto.getRegion()+" or with address "+ hotelDetailsDto.getCity()+", "+hotelDetailsDto.getStreet()+", "+ hotelDetailsDto.getHome()+" already exist" );
        }
        Hotel entity = mapper.convert(hotelDetailsDto);
        Hotel saved = repository.save(entity);
        return mapper.convert(saved);

    }

    @Override
    public List<HotelLightDto> getAll() {
        List<Hotel> all = repository.findAll();
        return mapper.convert(all);
    }

    @Override
    public HotelDetailsDto getById(UUID id) {
        Hotel hotel = repository.findById(id).orElseThrow(() -> new NoSuchHotelException("no hotel with id: " + id));
        return mapper.convert(hotel);
    }

    @Override
    public HotelDetailsDto update(HotelDetailsDto hotelDetailsDto) {
        Hotel hotel = repository.findById(hotelDetailsDto.getId()).orElseThrow(()-> new NoSuchHotelException("no hotel with id: " + hotelDetailsDto.getId()));
        Hotel update = mapper.update(hotelDetailsDto, hotel);
        Hotel saved = repository.save(update);
        return mapper.convert(saved);
    }

    @Override
    public void delete(UUID id) {
       repository.findById(id).ifPresentOrElse(hotel-> repository.delete(hotel), ()->new NoSuchHotelException("no hotel with id: "+ id));
    }


}

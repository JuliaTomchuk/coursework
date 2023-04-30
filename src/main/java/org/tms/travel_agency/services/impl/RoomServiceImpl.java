package org.tms.travel_agency.services.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.tms.travel_agency.domain.Room;
import org.tms.travel_agency.domain.Room_;
import org.tms.travel_agency.domain.TourProduct;
import org.tms.travel_agency.dto.room.RoomDetailsDto;
import org.tms.travel_agency.dto.room.RoomLightDto;
import org.tms.travel_agency.exception.DuplicateRoomException;
import org.tms.travel_agency.exception.NoSuchRoomException;
import org.tms.travel_agency.mapper.RoomMapper;
import org.tms.travel_agency.repository.RoomRepository;
import org.tms.travel_agency.services.RoomService;
import org.tms.travel_agency.validator.DuplicateValidator;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final DuplicateValidator<RoomDetailsDto> validator;
    @Override
    public RoomDetailsDto book(UUID idUser, UUID id) {
        return null;
    }

    @Override
    public List<RoomLightDto> bookAll(UUID idUser, List<UUID> ids) {
        return null;
    }

    @Override
    public int getPrice(UUID id) {
        return 0;
    }

    @Override
    public void addToCart(UUID id, UUID idUser) {

    }

    @Override
    public List<TourProduct> cartPreview(UUID idUser) {
        return null;
    }

    @Override
    public void deleteFromCart(UUID id) {

    }

    @Override
    public void cancelBooking(UUID id) {

    }

    @Override
    public List<RoomLightDto> search(RoomDetailsDto dto) {
        Room convert = roomMapper.convert(dto);
        List<Room> rooms = roomRepository.findAll(createSpecification(convert));
        return roomMapper.convert(rooms);

    }
    private Specification<Room> createSpecification(Room room){
        return (root, query, builder) -> {
            List<Predicate> conditions = new ArrayList<>();
            if(room.getNumOfTourist()!=null){
                Predicate equalNumOfTourist = builder.equal(root.get(Room_.NUM_OF_TOURIST),room.getNumOfTourist());
                conditions.add(equalNumOfTourist);
            }
            if(room.getTypesByOccupancy()!=null){
                Predicate equalTypeByOccupancy = builder.equal(root.get(Room_.TYPES_BY_OCCUPANCY), room.getTypesByOccupancy());
                conditions.add(equalTypeByOccupancy);
            }
            if(room.getTypesByView()!=null){
                Predicate equalTypeByView = builder.equal(root.get(Room_.TYPES_BY_VIEW),room.getTypesByView());
                conditions.add(equalTypeByView);
            }
            if(room.getHotel()!=null){
                Predicate equalHotel = builder.equal(root.get(Room_.HOTEL),room.getHotel());
                conditions.add(equalHotel);

            }
            return builder.and(conditions.toArray(new Predicate[]{}));
        };
    }

    @Override
    public RoomDetailsDto save(RoomDetailsDto roomDetailsDto) {
       if(!validator.isUnique(roomDetailsDto)){
           throw new DuplicateRoomException("Room with number: "+roomDetailsDto.getNumber()+", and hotel: "+roomDetailsDto.getIdHotel()+" already exist");
       }
        Room entity = roomMapper.convert(roomDetailsDto);
        Room saved = roomRepository.save(entity);
        return roomMapper.convert(saved);
    }

    @Override
    public List<RoomLightDto> getAll() {
        List<Room> rooms = roomRepository.findAll();
        return roomMapper.convert(rooms);
    }

    @Override
    public RoomDetailsDto getById(UUID id) {
        Room room = roomRepository.findById(id).orElseThrow(() -> new NoSuchRoomException("no room with id: " + id));
        return roomMapper.convert(room);
    }
    @Override
    public RoomDetailsDto update(RoomDetailsDto roomDetailsDto) {
        Room room = roomRepository.findById(roomDetailsDto.getId()).orElseThrow(() -> new NoSuchRoomException("No room with id: " + roomDetailsDto.getId()));
        if(!room.getNumber().equals(roomDetailsDto.getNumber())){
           if( !validator.isUnique(roomDetailsDto)){
               throw new DuplicateRoomException("room with humber: "+roomDetailsDto.getNumber()+" and hotel: "+roomDetailsDto.getIdHotel() +" already exist");
           }
        }
        Room update = roomMapper.update(roomDetailsDto, room);
        Room saved = roomRepository.save(update);
        return roomMapper.convert(saved);

    }

    @Override
    public void delete(UUID id) {
     roomRepository.findById(id).ifPresentOrElse((room)->roomRepository.delete(room),()->new NoSuchRoomException("no room with id: "+ id));
    }
}

package org.tms.travel_agency.services.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.tms.travel_agency.domain.Cart;
import org.tms.travel_agency.domain.Hotel;
import org.tms.travel_agency.domain.Hotel_;
import org.tms.travel_agency.domain.Room;
import org.tms.travel_agency.domain.Room_;
import org.tms.travel_agency.dto.room.RoomDetailsDto;
import org.tms.travel_agency.dto.room.RoomLightDto;
import org.tms.travel_agency.exception.DuplicateRoomException;
import org.tms.travel_agency.exception.NoSuchCartException;
import org.tms.travel_agency.exception.NoSuchRoomException;
import org.tms.travel_agency.exception.NoSuchUserException;
import org.tms.travel_agency.exception.NotAllowedException;
import org.tms.travel_agency.exception.TravelDateException;
import org.tms.travel_agency.mapper.RoomMapper;
import org.tms.travel_agency.repository.CartRepository;
import org.tms.travel_agency.repository.RoomRepository;
import org.tms.travel_agency.repository.UserRepository;
import org.tms.travel_agency.services.HotelService;
import org.tms.travel_agency.services.RoomService;
import org.tms.travel_agency.validator.DateValidator;
import org.tms.travel_agency.validator.DuplicateValidator;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import java.util.List;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final HotelService hotelService;
    private final RoomMapper roomMapper;
    private final DuplicateValidator<RoomDetailsDto> validator;
    private final DateValidator dateValidator;
    private final CartRepository cartRepository;

    @Override
    public void book(UUID id) {
        roomRepository.findById(id).ifPresentOrElse(room -> {
            room.setBooked(true);
            roomRepository.save(room);
        }, () -> { throw new NoSuchRoomException("no room with id" + id);});
    }

    private void addToCart(Room room) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Cart> optionalCart = cartRepository.findByUserUsername(username);
        Cart cart;
        if (optionalCart.isPresent()) {
            cart = optionalCart.get();
        } else {
            cart = new Cart();
            userRepository.findByUsername(username).ifPresentOrElse(cart::setUser, () -> { throw new NoSuchUserException("No user with username: " + username);});
        }
        room.setType("Room");
        cart.addTourProduct(room);
        cartRepository.save(cart);
    }


    @Override
    public void deleteFromCart(UUID id) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Room room = roomRepository.findById(id).orElseThrow(() -> new NoSuchRoomException("no room with id: " + id));
        if (room.getBooked()) {
            throw new NotAllowedException("Product has been booked, you can't delete it from cart");

        } else {
            Cart cart = cartRepository.findByUserUsername(name).orElseThrow(() -> new NoSuchCartException("No cart for user: " + name));
            cart.deleteTourProduct(room);
            room.setCheckOut(null);
            room.setCheckIn(null);
            room.setBoardBases(null);
            room.setPrice(null);
            room.setPreBooked(false);
            roomRepository.save(room);
        }
    }


    @Override
    public void cancelBooking(UUID idCart, UUID idProduct) {
        Cart cart = cartRepository.findById(idCart).orElseThrow(() -> new NoSuchCartException("No cart with id: " + idCart));
        Room room = roomRepository.findById(idProduct).orElseThrow(() -> new NoSuchRoomException("No room with id: " + idProduct));
        cart.deleteTourProduct(room);
        room.setPreBooked(false);
        room.setBooked(false);
        room.setPrice(null);
        room.setBoardBases(null);
        room.setCheckIn(null);
        room.setCheckOut(null);
        roomRepository.save(room);
    }

    @Override
    public List<RoomLightDto> search(RoomDetailsDto dto) {
        Room convert = roomMapper.convert(dto);
        List<Room> rooms = roomRepository.findAll(createSpecification(convert));
        return roomMapper.convert(rooms);

    }

    @Override
    public void prebook(RoomDetailsDto dto) {
        Optional<Room> roomOptional = roomRepository.findById(dto.getId());
        if (roomOptional.isEmpty()) {
            throw new NoSuchRoomException("no room with id" + dto.getId());
        }
        Room room = roomOptional.get();
        room.setPrice(dto.getPrice());
        room.setCheckIn(dto.getCheckIn());
        room.setCheckOut(dto.getCheckOut());
        room.setBoardBases(dto.getBoardBases());
        room.setPreBooked(true);
        addToCart(room);
    }

    private Specification<Room> createSpecification(Room room) {
        return (root, query, builder) -> {
            List<Predicate> conditions = new ArrayList<>();
            if (room.getNumOfTourist() != null) {
                Predicate equalNumOfTourist = builder.equal(root.get(Room_.NUM_OF_TOURIST), room.getNumOfTourist());
                conditions.add(equalNumOfTourist);
            }
            if (room.getTypesByOccupancy() != null) {
                Predicate equalTypeByOccupancy = builder.equal(root.get(Room_.TYPES_BY_OCCUPANCY), room.getTypesByOccupancy());
                conditions.add(equalTypeByOccupancy);
            }
            if (room.getTypesByView() != null) {
                Predicate equalTypeByView = builder.equal(root.get(Room_.TYPES_BY_VIEW), room.getTypesByView());
                conditions.add(equalTypeByView);
            }
            if (room.getHotel() != null) {
                Predicate equalHotel = builder.equal(root.get(Room_.HOTEL), room.getHotel());
                conditions.add(equalHotel);

            }
            if (room.getBooked() != null) {
                Predicate equalBooked = builder.equal(root.get(Room_.BOOKED), room.getBooked());
                conditions.add(equalBooked);

            }
            if (room.getPreBooked() != null) {
                Predicate equalPreBooked = builder.equal(root.get(Room_.PRE_BOOKED), room.getPreBooked());
                conditions.add(equalPreBooked);

            }
            if (room.getBoardBases() != null) {
                Join<Room, Hotel> hotelJoin = root.join(Room_.HOTEL);

                Predicate equalBoardBasis = builder.isMember(room.getBoardBases(), hotelJoin.get(Hotel_.BOARD_BASIS_SET));
                conditions.add(equalBoardBasis);

            }
            return builder.and(conditions.toArray(new Predicate[]{}));

        };
    }

    @Override
    public RoomDetailsDto save(RoomDetailsDto roomDetailsDto) {
        if (!validator.isUnique(roomDetailsDto)) {
            throw new DuplicateRoomException("Room with number: " + roomDetailsDto.getNumber() + ", and hotel: " + roomDetailsDto.getIdHotel() + " already exist");
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
    @Transactional
    public RoomDetailsDto update(RoomDetailsDto roomDetailsDto) {
        Room room = roomRepository.findById(roomDetailsDto.getId()).orElseThrow(() -> new NoSuchRoomException("No room with id: " + roomDetailsDto.getId()));
        if (!room.getNumber().equals(roomDetailsDto.getNumber())) {
            if (!validator.isUnique(roomDetailsDto)) {
                throw new DuplicateRoomException("room with humber: " + roomDetailsDto.getNumber() + " and hotel: " + roomDetailsDto.getIdHotel() + " already exist");
            }
        }
        Room update = roomMapper.update(roomDetailsDto, room);
        return roomMapper.convert(update);

    }

    @Override
    public void delete(UUID id) {
        roomRepository.findById(id).ifPresentOrElse(roomRepository::delete, () -> {throw new NoSuchRoomException("no room with id: " + id);});
    }


    @Override
    public List<RoomDetailsDto> getRoomsListForBooking(RoomDetailsDto dto) {
        if (!dateValidator.isCheckInEarlierThanCheckOut(dto.getCheckIn(), dto.getCheckOut())) {
            throw new TravelDateException("check in date should be less than check out date");
        }
        Room convert = roomMapper.convert(dto);
        List<Room> rooms = roomRepository.findAll(createSpecification(convert));
        if (rooms.size() == 0) {
            throw new NoSuchRoomException("no such rooms");
        }
        List<RoomDetailsDto> roomDtos = roomMapper.convertToListDto(rooms);
        return roomDtos.stream().peek(room -> {
            room.setCheckIn(dto.getCheckIn());
            room.setBoardBases(dto.getBoardBases());
            room.setCheckOut(dto.getCheckOut());
            room.setPrice(calculatePrice(room));
        }).collect(Collectors.toList());
    }

    private BigDecimal calculatePrice(RoomDetailsDto room) {
        BigDecimal basicPriceOfRoomPerDay = hotelService.getById(room.getIdHotel()).getBasicPriceOfRoom();
        long numOfDays = room.getCheckOut().toEpochDay() - room.getCheckIn().toEpochDay();
        BigDecimal priceForView = room.getTypesByView().price;
        BigDecimal priceForOccupancy = room.getTypesByOccupancy().price;
        BigDecimal boardBasisPricePerDay = room.getBoardBases().price.multiply(BigDecimal.valueOf(room.getNumOfTourist()));
        BigDecimal price = basicPriceOfRoomPerDay.add(priceForOccupancy).add(priceForView).add(boardBasisPricePerDay).multiply(BigDecimal.valueOf(numOfDays));
        price = price.setScale(2, RoundingMode.UP);
        return price;
    }

}

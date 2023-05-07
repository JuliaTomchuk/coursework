package org.tms.travel_agency.services.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.tms.travel_agency.domain.BoardBasisTypes;
import org.tms.travel_agency.domain.Cart;
import org.tms.travel_agency.domain.Destination;
import org.tms.travel_agency.domain.Hotel;
import org.tms.travel_agency.domain.HotelTypeByStars;
import org.tms.travel_agency.domain.HotelTypeByTargetMarket;
import org.tms.travel_agency.domain.Hotel_;
import org.tms.travel_agency.domain.Region;
import org.tms.travel_agency.domain.Room;
import org.tms.travel_agency.domain.RoomTypesByOccupancy;
import org.tms.travel_agency.domain.RoomTypesByView;
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
        roomRepository.findById(id).ifPresentOrElse(room ->{ room.setBooked(true); roomRepository.save(room);}, () -> new NoSuchRoomException("no room with id" + id));
    }

    private void addToCart(Room room) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Cart> optionalCart = cartRepository.findByUserUsername(username);
        Cart cart;
        if (optionalCart.isPresent()) {
            cart = optionalCart.get();
        } else {
            cart = new Cart();
            userRepository.findByUsername(username).ifPresentOrElse(user -> cart.setUser(user), () -> new NoSuchUserException("No user with username: " + username));
        }
        room.setType("room");
        cart.addTourProduct(room);
        cartRepository.save(cart);
    }


    @Override
    public void deleteFromCart(UUID id) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Room room = roomRepository.findById(id).orElseThrow(() -> new NoSuchRoomException("no room with id: " + id));
        if(room.getBooked()){
            throw new NotAllowedException("Product has been booked, you can't delete it from cart");

        }else{
            Cart cart = cartRepository.findByUserUsername(name).orElseThrow(() -> new NoSuchCartException("No cart for user: " + name));
            cart.deleteTourProduct(room);
            room.setCheckOut(null);
            room.setCheckIn(null);
            room.setBoardBases(null);
            room.setPrice(null);
            room.setPreBooked(false);
            room.setDescription(null);
            roomRepository.save(room);
        }
    }



    @Override
    public void cancelBooking( UUID idCart, UUID idProduct) {
        Cart cart = cartRepository.findById(idCart).orElseThrow(() -> new NoSuchCartException("No cart with id: " + idCart));
        Room room = roomRepository.findById(idProduct).orElseThrow(() -> new NoSuchRoomException("No room with id: " + idProduct));
        cart.deleteTourProduct(room);
        room.setPreBooked(false);
        room.setBooked(false);
        room.setPrice(null);
        room.setBoardBases(null);
        room.setCheckIn(null);
        room.setCheckOut(null);
        room.setDescription(null);
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
        room.setDescription(generateDescription(room));
        addToCart(room);
    }
    private String generateDescription(Room room){
        return "Room{" +
                "id=" + room.getId() +"\n"+
                ", destination: " + room.getHotel().getRegion().getDestination().getName()+"\n"+
                ", region: " + room.getHotel().getRegion().getName() +"\n"+
                ", hotel: " + room.getHotel().getName() +"\n"+
                ", stars: " + room.getHotel().getTypeByStars() +"\n"+
                ", type Of hotel: " + room.getHotel().getTypeByTargetMarket() +"\n"+
                ", room number: " + room.getNumber() +"\n"+
                ", numOfTourist: " + room.getNumOfTourist() +"\n"+
                ", typesByOccupancy: " + room.getTypesByOccupancy() +"\n"+
                ", typesByView: " + room.getTypesByView() +"\n"+
                ", checkIn: " + room.getCheckIn() +"\n"+
                ", checkOut: " + room.getCheckOut() +"\n"+
                ", boardBases: " + room.getBoardBases() +
                '}';
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
    public RoomDetailsDto update(RoomDetailsDto roomDetailsDto) {
        Room room = roomRepository.findById(roomDetailsDto.getId()).orElseThrow(() -> new NoSuchRoomException("No room with id: " + roomDetailsDto.getId()));
        if (!room.getNumber().equals(roomDetailsDto.getNumber())) {
            if (!validator.isUnique(roomDetailsDto)) {
                throw new DuplicateRoomException("room with humber: " + roomDetailsDto.getNumber() + " and hotel: " + roomDetailsDto.getIdHotel() + " already exist");
            }
        }
        Room update = roomMapper.update(roomDetailsDto, room);
        Room saved = roomRepository.save(update);
        return roomMapper.convert(saved);

    }

    @Override
    public void delete(UUID id) {
        roomRepository.findById(id).ifPresentOrElse((room) -> roomRepository.delete(room), () -> new NoSuchRoomException("no room with id: " + id));
    }


    @Override
    public List<RoomDetailsDto> getRoomsListForBooking(RoomDetailsDto dto) {
        if (!dateValidator.isCheckInLessThanCheckOut(dto.getCheckIn(), dto.getCheckOut())) {
            throw new TravelDateException("check in date should be less than check out date");
        }
        Room convert = roomMapper.convert(dto);
        List<Room> rooms = roomRepository.findAll(createSpecification(convert));
        if (rooms.size() == 0) {
            throw new NoSuchRoomException("no such rooms");
        }
        List<RoomDetailsDto> roomDtos = roomMapper.convertToListDto(rooms);
        List<RoomDetailsDto> roomsForBooking = roomDtos.stream().peek(room -> {
            room.setCheckIn(dto.getCheckIn());
            room.setBoardBases(dto.getBoardBases());
            room.setCheckOut(dto.getCheckOut());
            room.setPrice(calculatePrice(room));
        }).collect(Collectors.toList());
        return roomsForBooking;
    }

    private BigDecimal calculatePrice(RoomDetailsDto room) {
        BigDecimal basicPriceOfRoomPerDay = hotelService.getById(room.getIdHotel()).getBasicPriceOfRoom();
        long numOfDays = room.getCheckOut().toEpochDay() - room.getCheckIn().toEpochDay();
        BigDecimal priceForView = calculatePriceByView(room.getTypesByView());
        BigDecimal priceForOccupancy = calculatePriceByOccupancy(room.getTypesByOccupancy());
        BigDecimal boardBasisPricePerDay = calculateBoardBasisPricePerDay(room.getBoardBases(), room.getNumOfTourist());
        BigDecimal price = basicPriceOfRoomPerDay.add(priceForOccupancy).add(priceForView).add(boardBasisPricePerDay).multiply(BigDecimal.valueOf(numOfDays));
        price = price.setScale(2, RoundingMode.UP);
        return price;
    }

    private BigDecimal calculatePriceByView(RoomTypesByView typesByView) {
        BigDecimal price = new BigDecimal(0.0);
        switch (typesByView) {
            case INSIDE -> price = new BigDecimal(15.2);
            case CITY -> price = new BigDecimal(30.8);
            case LAND -> price = new BigDecimal(25.7);
            case PARK -> price = new BigDecimal(35.4);
            case GARDEN -> price = new BigDecimal(20.9);
            case POOL -> price = new BigDecimal(40.6);
            case MOUNTAIN -> price = new BigDecimal(50.5);
            case SIDE_SEA -> price = new BigDecimal(60.2);
            case SEA -> price = new BigDecimal(70.5);

        }
        return price;
    }

    private BigDecimal calculatePriceByOccupancy(RoomTypesByOccupancy typesByOccupancy) {

        BigDecimal price = new BigDecimal("0.0");
        switch (typesByOccupancy) {
            case SINGLE -> price = new BigDecimal(100.60);
            case DOUBLE, TWIN -> price = new BigDecimal(180.60);
            case TRIPLE -> price = new BigDecimal(300.60);
            case QUAD -> price = new BigDecimal(460.10);
        }
        return price;
    }

    private BigDecimal calculateBoardBasisPricePerDay(BoardBasisTypes boardBases, Integer numOfTourist) {
        BigDecimal price = new BigDecimal(0.0);
        switch (boardBases) {
            case BED_AND_BREAKFAST -> new BigDecimal(100.9);
            case HALF_BOARD -> new BigDecimal(250.8);
            case FULL_BOARD -> new BigDecimal(350.6);
            case All_INCLUSIVE -> new BigDecimal(550.80);
            case ULTRA_All_INCLUSIVE -> new BigDecimal(690.9);
        }
        return price.multiply(BigDecimal.valueOf(numOfTourist));

    }
}

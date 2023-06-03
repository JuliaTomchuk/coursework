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
import org.tms.travel_agency.domain.BoardBasisTypes;
import org.tms.travel_agency.domain.Cart;
import org.tms.travel_agency.domain.Destination;
import org.tms.travel_agency.domain.Hotel;
import org.tms.travel_agency.domain.Region;
import org.tms.travel_agency.domain.Room;
import org.tms.travel_agency.domain.RoomTypesByOccupancy;
import org.tms.travel_agency.domain.RoomTypesByView;
import org.tms.travel_agency.domain.User;
import org.tms.travel_agency.dto.room.RoomDetailsDto;
import org.tms.travel_agency.dto.room.RoomLightDto;
import org.tms.travel_agency.exception.DuplicateRoomException;
import org.tms.travel_agency.exception.NoSuchCartException;
import org.tms.travel_agency.exception.NoSuchRoomException;
import org.tms.travel_agency.exception.NotAllowedException;
import org.tms.travel_agency.exception.TravelDateException;
import org.tms.travel_agency.mapper.RoomMapper;
import org.tms.travel_agency.repository.CartRepository;
import org.tms.travel_agency.repository.RoomRepository;
import org.tms.travel_agency.repository.UserRepository;
import org.tms.travel_agency.services.HotelService;
import org.tms.travel_agency.validator.DateValidator;

import org.tms.travel_agency.validator.impl.RoomValidator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;


@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration
class RoomServiceImplTest {
    private UserRepository userRepository;
    private RoomRepository roomRepository;
    private HotelService hotelService;
    private RoomMapper roomMapper;
    private RoomValidator roomValidator;
    private DateValidator dateValidator;
    private CartRepository cartRepository;
    private RoomServiceImpl roomService;

    @BeforeEach
    void init() {
        userRepository = Mockito.mock(UserRepository.class);
        roomRepository = Mockito.mock(RoomRepository.class);
        hotelService = Mockito.mock(HotelService.class);
        roomMapper = Mockito.mock(RoomMapper.class);
        roomValidator = Mockito.mock(RoomValidator.class);
        dateValidator = Mockito.mock(DateValidator.class);
        cartRepository = Mockito.mock(CartRepository.class);
        roomService = new RoomServiceImpl(userRepository, roomRepository, hotelService, roomMapper, roomValidator, dateValidator, cartRepository);
    }

    @Test
    void bookSuccess() {
        UUID id = UUID.randomUUID();
        Room room = new Room();
        ArgumentCaptor<Room> roomArgumentCaptor = ArgumentCaptor.forClass(Room.class);
        room.setId(id);

        Mockito.when(roomRepository.findById(id)).thenReturn(Optional.of(room));
        roomService.book(id);
        Mockito.verify(roomRepository, Mockito.times(1)).save(room);
        Mockito.verify(roomRepository).save(roomArgumentCaptor.capture());
        Room value = roomArgumentCaptor.getValue();

        Assertions.assertThat(value.getBooked()).isTrue();
        Assertions.assertThat(value.getId()).isEqualTo(id);

    }

    @Test
    void bookIfNotExist() {
        UUID id = UUID.randomUUID();
        Mockito.when(roomRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(NoSuchRoomException.class).isThrownBy(() -> roomService.book(id));
    }

    @Test
    @WithMockUser
    void deleteFromCartIfRoomNotExist() {
        UUID id = UUID.randomUUID();
        Mockito.when(roomRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(NoSuchRoomException.class).isThrownBy(() -> roomService.deleteFromCart(id));

    }

    @Test
    @WithMockUser
    void deleteFromCartIfCartNotExist() {
        UUID id = UUID.randomUUID();
        Room room = new Room();
        room.setBooked(false);
        room.setId(id);

        Mockito.when(roomRepository.findById(id)).thenReturn(Optional.of(room));
        Mockito.when(cartRepository.findByUserUsername("user")).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(NoSuchCartException.class).isThrownBy(() -> roomService.deleteFromCart(id));
    }

    @Test
    @WithMockUser
    void deleteFromCartIfRoomBooked() {
        UUID id = UUID.randomUUID();
        Room room = new Room();
        room.setBooked(true);
        room.setId(id);
        Mockito.when(roomRepository.findById(id)).thenReturn(Optional.of(room));
        Assertions.assertThatExceptionOfType(NotAllowedException.class).isThrownBy(() -> roomService.deleteFromCart(id));

    }

    @ParameterizedTest
    @MethodSource("getRoomAndCart")
    @WithMockUser
    void deleteFromCartSuccess(Room room, Cart cart) {
        Mockito.when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));
        Mockito.when(cartRepository.findByUserUsername("user")).thenReturn(Optional.of(cart));
        roomService.deleteFromCart(room.getId());
        Mockito.verify(roomRepository, Mockito.times(1)).save(room);
        ArgumentCaptor<Room> roomArgumentCaptor = ArgumentCaptor.forClass(Room.class);
        Mockito.verify(roomRepository).save(roomArgumentCaptor.capture());
        Assertions.assertThat(room.getPreBooked()).isFalse();
        Assertions.assertThat(room.getCheckIn()).isNull();
        Assertions.assertThat(room.getCheckOut()).isNull();
        Assertions.assertThat(room.getBoardBases()).isNull();
        Assertions.assertThat(room.getPrice()).isNull();
        Assertions.assertThat(cart.getTourProductList()).isNullOrEmpty();

    }

    @Test
    void cancelBookingIfCartNotExist() {
        UUID idCart = UUID.randomUUID();
        UUID idProduct = UUID.randomUUID();

        Mockito.when(cartRepository.findById(idCart)).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(NoSuchCartException.class).isThrownBy(() -> roomService.cancelBooking(idCart, idProduct));
    }

    @Test
    void cancelBookingIfRoomNotExist() {
        UUID idCart = UUID.randomUUID();
        UUID idProduct = UUID.randomUUID();
        Cart cart = new Cart();
        cart.setId(idCart);

        Mockito.when(cartRepository.findById(idCart)).thenReturn(Optional.of(cart));
        Mockito.when(roomRepository.findById(idProduct)).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(NoSuchRoomException.class).isThrownBy(() -> roomService.cancelBooking(idCart, idProduct));
    }

    @ParameterizedTest
    @MethodSource("getRoomAndCart")
    void cancelBookingSuccess(Room room, Cart cart) {
        room.setBooked(true);

        Mockito.when(cartRepository.findById(cart.getId())).thenReturn(Optional.of(cart));
        Mockito.when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));
        roomService.cancelBooking(cart.getId(), room.getId());
        Mockito.verify(roomRepository, Mockito.times(1)).save(room);
        ArgumentCaptor<Room> capture = ArgumentCaptor.forClass(Room.class);
        Mockito.verify(roomRepository).save(capture.capture());
        Room roomSaved = capture.getValue();
        Assertions.assertThat(roomSaved.getBooked()).isFalse();
        Assertions.assertThat(roomSaved.getPreBooked()).isFalse();
        Assertions.assertThat(roomSaved.getCheckIn()).isNull();
        Assertions.assertThat(roomSaved.getCheckOut()).isNull();
        Assertions.assertThat(roomSaved.getBoardBases()).isNull();
        Assertions.assertThat(roomSaved.getPrice()).isNull();
        Assertions.assertThat(cart.getTourProductList()).isNullOrEmpty();

    }

    @Test
    void saveIfDuplicate() {
        UUID idHotel = UUID.randomUUID();
        RoomDetailsDto dto = new RoomDetailsDto();
        dto.setIdHotel(idHotel);
        dto.setNumber(1);
        Mockito.when(roomValidator.isUnique(dto)).thenReturn(false);
        Assertions.assertThatExceptionOfType(DuplicateRoomException.class).isThrownBy(() -> roomService.save(dto));
    }

    @ParameterizedTest
    @MethodSource("getEntityAndDto")
    void saveSuccess(Room entity, RoomDetailsDto dto) {
        Mockito.when(roomValidator.isUnique(dto)).thenReturn(true);
        Mockito.when(roomMapper.convert(dto)).thenReturn(entity);
        Mockito.when(roomRepository.save(entity)).thenReturn(entity);
        Mockito.when(roomMapper.convert(entity)).thenReturn(dto);
        RoomDetailsDto save = roomService.save(dto);
        Assertions.assertThat(save).isEqualTo(dto);
    }

    @ParameterizedTest
    @MethodSource("getListsOfEntityAndDto")
    void getAll(List<Room> entityList, List<RoomLightDto> dtoList) {
        Mockito.when(roomRepository.findAll()).thenReturn(entityList);
        Mockito.when(roomMapper.convert(entityList)).thenReturn(dtoList);
        List<RoomLightDto> all = roomService.getAll();
        Assertions.assertThat(all).isEqualTo(dtoList);
    }

    @Test
    void getByIdIfRoomNotExist() {
        UUID id = UUID.randomUUID();
        Mockito.when(roomRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(NoSuchRoomException.class).isThrownBy(() -> roomService.getById(id));
    }

    @ParameterizedTest
    @MethodSource("getEntityAndDto")
    void getByIdSuccess(Room entity, RoomDetailsDto dto) {
        Mockito.when(roomRepository.findById(dto.getId())).thenReturn(Optional.of(entity));
        Mockito.when(roomMapper.convert(entity)).thenReturn(dto);
        RoomDetailsDto byId = roomService.getById(dto.getId());
        Assertions.assertThat(byId).isEqualTo(dto);
    }

    @Test
    void updateIfRoomNotExist() {
        UUID id = UUID.randomUUID();
        RoomDetailsDto dto = new RoomDetailsDto();
        dto.setId(id);
        Mockito.when(roomRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(NoSuchRoomException.class).isThrownBy(() -> roomService.update(dto));
    }

    @Test
    void updateIfDuplicate() {
        UUID id = UUID.randomUUID();
        RoomDetailsDto dto = new RoomDetailsDto();
        dto.setId(id);
        dto.setNumber(2);
        Room room = new Room();
        room.setNumber(1);
        room.setId(id);
        Mockito.when(roomRepository.findById(id)).thenReturn(Optional.of(room));
        Mockito.when(roomValidator.isUnique(dto)).thenReturn(false);
        Assertions.assertThatExceptionOfType(DuplicateRoomException.class).isThrownBy(() -> roomService.update(dto));
    }

    @ParameterizedTest
    @MethodSource("getEntityAndDto")
    void updateSuccess(Room entity, RoomDetailsDto dto) {
        dto.setNumber(2);
        Mockito.when(roomRepository.findById(dto.getId())).thenReturn(Optional.of(entity));
        Mockito.when(roomValidator.isUnique(dto)).thenReturn(true);
        Mockito.when(roomMapper.update(dto, entity)).thenReturn(entity);
        Mockito.when(roomMapper.convert(entity)).thenReturn(dto);
        RoomDetailsDto updated = roomService.update(dto);
        Assertions.assertThat(updated).isEqualTo(dto);

    }

    @Test
    void deleteIfRoomNotExist() {
        UUID id = UUID.randomUUID();
        Mockito.when(roomRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(NoSuchRoomException.class).isThrownBy(() -> roomService.delete(id));
    }

    @ParameterizedTest
    @MethodSource("getEntity")
    void deleteSuccess(Room entity) {
        Mockito.when(roomRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        roomService.delete(entity.getId());
        ArgumentCaptor<Room> captor = ArgumentCaptor.forClass(Room.class);
        Mockito.verify(roomRepository, Mockito.times(1)).delete(entity);
        Mockito.verify(roomRepository).delete(captor.capture());
        Room value = captor.getValue();
        Assertions.assertThat(value).isEqualTo(entity);
    }

    @Test
    void getRoomListForBookingIfCheckInLaterThanCheckOut() {
        LocalDate checkIn = LocalDate.now().plusDays(10);
        LocalDate checkOut = LocalDate.now();
        RoomDetailsDto dto = new RoomDetailsDto();
        dto.setCheckIn(checkIn);
        dto.setCheckOut(checkOut);
        Mockito.when(dateValidator.isCheckInEarlierThanCheckOut(checkIn, checkOut)).thenReturn(false);
        Assertions.assertThatExceptionOfType(TravelDateException.class).isThrownBy(() -> roomService.getRoomsListForBooking(dto));
    }

    public static Stream<Arguments> getRoomAndCart() {
        UUID id = UUID.randomUUID();
        Room room = new Room();
        room.setId(id);
        room.setBooked(false);
        room.setPrice(BigDecimal.valueOf(600));
        room.setCheckIn(LocalDate.now().plusDays(1));
        room.setCheckOut(LocalDate.now().plusDays(8));
        room.setBoardBases(BoardBasisTypes.FULL_BOARD);
        room.setPreBooked(true);

        User user = new User();
        user.setUsername("user");

        Cart cart = new Cart();
        cart.addTourProduct(room);
        cart.setUser(user);
        return Stream.of(Arguments.arguments(room, cart));
    }

    public static Stream<Arguments> getEntityAndDto() {
        Room entity = new Room();
        entity.setId(UUID.randomUUID());
        entity.setNumber(1);
        entity.setNumOfTourist(2);
        entity.setTypesByView(RoomTypesByView.GARDEN);
        entity.setTypesByOccupancy(RoomTypesByOccupancy.TWIN);

        Hotel hotel = new Hotel();
        hotel.setName("TEST");
        Destination destination = new Destination();
        destination.setName("TEST");
        Region region = new Region();
        region.setName("TEST");
        region.setDestination(destination);
        hotel.setRegion(region);
        hotel.setId(UUID.randomUUID());
        entity.setHotel(hotel);

        RoomDetailsDto dto = new RoomDetailsDto();
        dto.setId(entity.getId());
        dto.setNumber(entity.getNumber());
        dto.setIdHotel(hotel.getId());
        dto.setTypesByView(entity.getTypesByView());
        dto.setTypesByOccupancy(entity.getTypesByOccupancy());
        dto.setDestination(hotel.getRegion().getDestination().getName());
        dto.setRegion(hotel.getRegion().getName());
        dto.setHotelName(hotel.getName());
        dto.setNumOfTourist(entity.getNumOfTourist());

        return Stream.of(Arguments.arguments(entity, dto));
    }

    public static Stream<Arguments> getListsOfEntityAndDto() {
        List<Room> entityList = new ArrayList<>();
        Room entity = new Room();
        entity.setId(UUID.randomUUID());
        entity.setNumber(1);
        entity.setNumOfTourist(2);
        entity.setTypesByView(RoomTypesByView.GARDEN);
        entity.setTypesByOccupancy(RoomTypesByOccupancy.TWIN);

        Hotel hotel = new Hotel();
        hotel.setName("TEST");
        Destination destination = new Destination();
        destination.setName("TEST");
        Region region = new Region();
        region.setName("TEST");
        region.setDestination(destination);
        hotel.setRegion(region);
        hotel.setId(UUID.randomUUID());
        entity.setHotel(hotel);

        Room entity2 = new Room();
        entity2.setId(UUID.randomUUID());
        entity2.setNumber(2);
        entity2.setNumOfTourist(4);
        entity2.setTypesByView(RoomTypesByView.SEA);
        entity2.setTypesByOccupancy(RoomTypesByOccupancy.QUAD);
        entity2.setHotel(hotel);
        entityList.add(entity);
        entityList.add(entity2);

        List<RoomLightDto> dtoList = new ArrayList<>();
        RoomLightDto dto = new RoomLightDto();
        dto.setId(entity.getId());
        dto.setNumber(entity.getNumber());
        dto.setTypesByView(entity.getTypesByView());
        dto.setTypesByOccupancy(entity.getTypesByOccupancy());
        dto.setDestination(hotel.getRegion().getDestination().getName());
        dto.setHotelName(hotel.getName());
        dto.setNumOfTourist(entity.getNumOfTourist());

        RoomLightDto dto2 = new RoomLightDto();
        dto2.setId(entity2.getId());
        dto2.setNumber(entity2.getNumber());
        dto2.setTypesByView(entity2.getTypesByView());
        dto2.setTypesByOccupancy(entity2.getTypesByOccupancy());
        dto2.setDestination(hotel.getRegion().getDestination().getName());
        dto2.setHotelName(hotel.getName());
        dto2.setNumOfTourist(entity2.getNumOfTourist());
        dtoList.add(dto);
        dtoList.add(dto2);

        return Stream.of(Arguments.arguments(entityList, dtoList));
    }

    public static Stream<Room> getEntity() {
        Room entity = new Room();
        entity.setId(UUID.randomUUID());
        entity.setNumber(1);
        entity.setNumOfTourist(2);
        entity.setTypesByView(RoomTypesByView.GARDEN);
        entity.setTypesByOccupancy(RoomTypesByOccupancy.TWIN);

        Hotel hotel = new Hotel();
        hotel.setName("TEST");
        Destination destination = new Destination();
        destination.setName("TEST");
        Region region = new Region();
        region.setName("TEST");
        region.setDestination(destination);
        hotel.setRegion(region);
        hotel.setId(UUID.randomUUID());
        entity.setHotel(hotel);
        return Stream.of(entity);
    }


}
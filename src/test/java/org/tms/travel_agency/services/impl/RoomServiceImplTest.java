package org.tms.travel_agency.services.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tms.travel_agency.domain.BoardBasisTypes;
import org.tms.travel_agency.domain.Cart;
import org.tms.travel_agency.domain.Room;
import org.tms.travel_agency.domain.User;
import org.tms.travel_agency.dto.room.RoomDetailsDto;
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

import java.util.Optional;
import java.util.UUID;


@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration
class RoomServiceImplTest {
    private  UserRepository userRepository;
    private  RoomRepository roomRepository;
    private  HotelService hotelService;
    private  RoomMapper roomMapper;
    private RoomValidator roomValidator;
    private  DateValidator dateValidator;
    private  CartRepository cartRepository;
    private RoomServiceImpl roomService;

    @BeforeEach
    public void init(){
        userRepository = Mockito.mock(UserRepository.class);
        roomRepository=Mockito.mock(RoomRepository.class);
        hotelService=Mockito.mock(HotelService.class);
        roomMapper=Mockito.mock(RoomMapper.class);
        roomValidator=Mockito.mock(RoomValidator.class);
        dateValidator=Mockito.mock(DateValidator.class);
        cartRepository=Mockito.mock(CartRepository.class);
        roomService = new RoomServiceImpl(userRepository,roomRepository,hotelService,roomMapper,roomValidator,dateValidator,cartRepository);
    }
    @Test
    public void bookSuccess(){
        UUID id =UUID.randomUUID();
        Room room = new Room();
        ArgumentCaptor<Room> roomArgumentCaptor = ArgumentCaptor.forClass(Room.class);
        room.setId(id);

        Mockito.when(roomRepository.findById(id)).thenReturn(Optional.of(room));
        roomService.book(id);
        Mockito.verify(roomRepository,Mockito.times(1)).save(room);
        Mockito.verify(roomRepository).save(roomArgumentCaptor.capture());
        Room value = roomArgumentCaptor.getValue();

        Assertions.assertThat(value.getBooked()).isTrue();
        Assertions.assertThat(value.getId()).isEqualTo(id);

    }
    @Test
    public void bookIfNotExist(){
        UUID id = UUID.randomUUID();
        Mockito.when(roomRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(NoSuchRoomException.class).isThrownBy(()->roomService.book(id));
    }
    @Test
    @WithMockUser
    public void deleteFromCartIfRoomNotExist(){
        UUID id = UUID.randomUUID();
        Mockito.when(roomRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(NoSuchRoomException.class).isThrownBy(()->roomService.deleteFromCart(id));

    }
    @Test
    @WithMockUser
    public void deleteFromCartIfCartNotExist(){
        UUID id =UUID.randomUUID();
        Room room = new Room();
        room.setBooked(false);
        room.setId(id);

        Mockito.when(roomRepository.findById(id)).thenReturn(Optional.of(room));
        Mockito.when(cartRepository.findByUserUsername("user")).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(NoSuchCartException.class).isThrownBy(()->roomService.deleteFromCart(id));
    }
    @Test
    @WithMockUser
    public void deleteFromCartIfRoomBooked(){
        UUID id = UUID.randomUUID();
        Room room = new Room();
        room.setBooked(true);
        room.setId(id);
        Mockito.when(roomRepository.findById(id)).thenReturn(Optional.of(room));
        Assertions.assertThatExceptionOfType(NotAllowedException.class).isThrownBy(()->roomService.deleteFromCart(id));

    }
    @Test
    @WithMockUser
    public void deleteFromCartSuccess(){
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
        Cart cart= new Cart();
        cart.addTourProduct(room);
        cart.setUser(user);
        Mockito.when(roomRepository.findById(id)).thenReturn(Optional.of(room));
        Mockito.when(cartRepository.findByUserUsername("user")).thenReturn(Optional.of(cart));
        roomService.deleteFromCart(id);
        Mockito.verify(roomRepository,Mockito.times(1)).save(room);
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
    public void cancelBookingIfCartNotExist(){
        UUID idCart=UUID.randomUUID();
        UUID idProduct=UUID.randomUUID();

        Mockito.when(cartRepository.findById(idCart)).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(NoSuchCartException.class).isThrownBy(()->roomService.cancelBooking(idCart,idProduct));
    }
    @Test
    public void cancelBookingIfRoomNotExist(){
        UUID idCart=UUID.randomUUID();
        UUID idProduct=UUID.randomUUID();
        Cart cart= new Cart();
        cart.setId(idCart);

        Mockito.when(cartRepository.findById(idCart)).thenReturn(Optional.of(cart));
        Mockito.when(roomRepository.findById(idProduct)).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(NoSuchRoomException.class).isThrownBy(()->roomService.cancelBooking(idCart,idProduct));
    }
    @Test
    public void cancelBookingSuccess(){
        UUID idCart=UUID.randomUUID();
        UUID idProduct=UUID.randomUUID();

        Room room = new Room();
        room.setId(idProduct);
        room.setBooked(true);
        room.setPrice(BigDecimal.valueOf(600));
        room.setCheckIn(LocalDate.now().plusDays(1));
        room.setCheckOut(LocalDate.now().plusDays(8));
        room.setBoardBases(BoardBasisTypes.FULL_BOARD);
        room.setPreBooked(true);

        Cart cart= new Cart();
        cart.setId(idCart);
        cart.addTourProduct(room);

        Mockito.when(cartRepository.findById(idCart)).thenReturn(Optional.of(cart));
        Mockito.when(roomRepository.findById(idProduct)).thenReturn(Optional.of(room));
        roomService.cancelBooking(idCart, idProduct);
        Mockito.verify(roomRepository,Mockito.times(1)).save(room);
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
   public void saveIfDuplicate(){
        UUID idHotel=UUID.randomUUID();
        RoomDetailsDto dto=new RoomDetailsDto();
        dto.setIdHotel(idHotel);
        dto.setNumber(1);
        Mockito.when(roomValidator.isUnique(dto)).thenReturn(false);
        Assertions.assertThatExceptionOfType(DuplicateRoomException.class).isThrownBy(()->roomService.save(dto));
    }
    @Test
    public void saveSuccess(){
        RoomDetailsDto dto=new RoomDetailsDto();
        Room room = new Room();
        Mockito.when(roomValidator.isUnique(dto)).thenReturn(true);
        Mockito.when(roomMapper.convert(dto)).thenReturn(room);
        roomService.save(dto);
        Mockito.verify(roomRepository,Mockito.times(1)).save(room);
    }

    @Test
    public void getAll(){
        Mockito.when(roomRepository.findAll()).thenReturn(new ArrayList<>());
        roomService.getAll();
        Mockito.verify(roomRepository,Mockito.times(1)).findAll();
    }

    @Test
    public void getByIdIfRoomNotExist(){
        UUID id=UUID.randomUUID();
        Mockito.when(roomRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(NoSuchRoomException.class).isThrownBy(()->roomService.getById(id));
    }
    @Test
    public void getByIdSuccess(){
        UUID id=UUID.randomUUID();
        Room room = new Room();
        room.setId(id);
        RoomDetailsDto dto = new RoomDetailsDto();
        dto.setId(id);
        Mockito.when(roomRepository.findById(id)).thenReturn(Optional.of(room));
        Mockito.when(roomMapper.convert(room)).thenReturn(dto);
        RoomDetailsDto byId = roomService.getById(id);
        Assertions.assertThat(byId.getId()).isEqualTo(id);
    }

    @Test
    public void updateIfRoomNotExist(){
        UUID id =UUID.randomUUID();
        RoomDetailsDto dto= new RoomDetailsDto();
        dto.setId(id);
        Mockito.when(roomRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(NoSuchRoomException.class).isThrownBy(()->roomService.update(dto));
    }

    @Test
    public void updateIfDuplicate() {
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
    @Test
    public void updateSuccess(){
        UUID id = UUID.randomUUID();
        RoomDetailsDto dto = new RoomDetailsDto();
        dto.setId(id);
        dto.setNumber(1);
        Room room = new Room();
        room.setNumber(2);
        room.setId(id);

        Mockito.when(roomRepository.findById(id)).thenReturn(Optional.of(room));
        Mockito.when(roomValidator.isUnique(dto)).thenReturn(true);
        Mockito.when(roomMapper.update(dto,room)).thenReturn(room);
        Mockito.when(roomMapper.convert(room)).thenReturn(dto);
        roomService.update(dto);

        Mockito.verify(roomMapper,Mockito.times(1)).update(dto,room);

    }
    @Test
    public void deleteIfRoomNotExist(){
        UUID id =UUID.randomUUID();
        Mockito.when(roomRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(NoSuchRoomException.class).isThrownBy(()->roomService.delete(id));
    }
    @Test
    public void deleteIfSuccess(){
        UUID id =UUID.randomUUID();
        Room room = new Room();
        room.setId(id);
        Mockito.when(roomRepository.findById(id)).thenReturn(Optional.of(room));
        roomService.delete(id);
        Mockito.verify(roomRepository, Mockito.times(1)).delete(room);
    }

    @Test
    public void getRoomListForBookingIfCheckInLaterThanCheckOut(){
        LocalDate checkIn = LocalDate.now().plusDays(10);
        LocalDate checkOut = LocalDate.now();
        RoomDetailsDto dto = new RoomDetailsDto();
        dto.setCheckIn(checkIn);
        dto.setCheckOut(checkOut);
        Mockito.when(dateValidator.isCheckInEarlierThanCheckOut(checkIn,checkOut)).thenReturn(false);
        Assertions.assertThatExceptionOfType(TravelDateException.class).isThrownBy(()->roomService.getRoomsListForBooking(dto));
    }


}
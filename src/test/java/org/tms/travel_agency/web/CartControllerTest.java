package org.tms.travel_agency.web;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.tms.travel_agency.domain.Address;
import org.tms.travel_agency.domain.BoardBasisTypes;
import org.tms.travel_agency.domain.Cart;
import org.tms.travel_agency.domain.Destination;
import org.tms.travel_agency.domain.Hotel;
import org.tms.travel_agency.domain.HotelTypeByStars;
import org.tms.travel_agency.domain.HotelTypeByTargetMarket;
import org.tms.travel_agency.domain.Region;
import org.tms.travel_agency.domain.Role;
import org.tms.travel_agency.domain.Room;
import org.tms.travel_agency.domain.RoomTypesByOccupancy;
import org.tms.travel_agency.domain.RoomTypesByView;
import org.tms.travel_agency.domain.TourProduct;
import org.tms.travel_agency.domain.User;
import org.tms.travel_agency.mapper.RoomMapper;
import org.tms.travel_agency.repository.AddressRepository;
import org.tms.travel_agency.repository.CartRepository;
import org.tms.travel_agency.repository.DestinationRepository;
import org.tms.travel_agency.repository.HotelRepository;
import org.tms.travel_agency.repository.RegionRepository;
import org.tms.travel_agency.repository.RoomRepository;
import org.tms.travel_agency.repository.UserRepository;
import org.tms.travel_agency.services.RoomService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;


import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class CartControllerTest {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private DestinationRepository destinationRepository;
    @Autowired
    private CartRepository cartRepository;


    @BeforeEach
    public void clearDB() {
        destinationRepository.deleteAll();
        regionRepository.deleteAll();
        hotelRepository.deleteAll();
        cartRepository.deleteAll();
        roomRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Container
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer<>("postgres:12.12")
            .withDatabaseName("postgres")
            .withUsername("postgres")
            .withPassword("14271915");

    @DynamicPropertySource
    public static void initSpringProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> postgreSQLContainer.getJdbcUrl());
        registry.add("spring.datasource.username", () -> postgreSQLContainer.getUsername());
        registry.add("spring.datasource.password", () -> postgreSQLContainer.getPassword());
    }

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @ParameterizedTest
    @MethodSource("getTourProduct")
    @WithMockUser
    void getAllIfUser(Room room, Hotel hotel, Region region, Address address, User user, Destination destination) throws Exception {
        destinationRepository.save(destination);
        destination.addRegion(region);
        userRepository.save(user);
        regionRepository.save(region);
        addressRepository.save(address);
        hotel.setAddress(address);
        region.addHotel(hotel);
        hotelRepository.save(hotel);
        room.setHotel(hotel);
        roomRepository.save(room);
        roomService.prebook(roomMapper.convert(room));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/cart")).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();
        Map<String, Object> model = modelAndView.getModel();
        List<TourProduct> products = (List<TourProduct>) model.get("products");
        Boolean isAdmin = (Boolean) model.get("isAdmin");
        Assertions.assertThat(viewName).isEqualTo("cart");
        Assertions.assertThat(products.get(0)).isEqualTo(room);
        Assertions.assertThat(isAdmin).isFalse();
    }
    @ParameterizedTest
    @MethodSource("getTourProduct")
    @WithMockUser(roles="ADMIN")
    void getAllIfAdmin(Room room, Hotel hotel, Region region, Address address, User user, Destination destination) throws Exception {
        destinationRepository.save(destination);
        destination.addRegion(region);
        userRepository.save(user);
        regionRepository.save(region);
        addressRepository.save(address);
        hotel.setAddress(address);
        region.addHotel(hotel);
        hotelRepository.save(hotel);
        room.setHotel(hotel);
        roomRepository.save(room);
        roomService.prebook(roomMapper.convert(room));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/cart")).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();
        Map<String, Object> model = modelAndView.getModel();
        List<TourProduct> products = (List<TourProduct>) model.get("products");
        Boolean isAdmin = (Boolean) model.get("isAdmin");
        Assertions.assertThat(viewName).isEqualTo("cart");
        Assertions.assertThat(products.get(0)).isEqualTo(room);
        Assertions.assertThat(isAdmin).isTrue();
    }
    @WithMockUser
    @MethodSource("getTourProduct")
    @ParameterizedTest
    void bookSuccess(Room room, Hotel hotel, Region region, Address address, User user, Destination destination) throws Exception {
        destinationRepository.save(destination);
        destination.addRegion(region);
        userRepository.save(user);
        regionRepository.save(region);
        addressRepository.save(address);
        hotel.setAddress(address);
        region.addHotel(hotel);
        hotelRepository.save(hotel);
        room.setHotel(hotel);
        roomRepository.save(room);
        roomService.prebook(roomMapper.convert(room));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/cart/book").param("id",room.getId().toString()).param("type","Room")).andReturn();
        Optional<Room> byId = roomRepository.findById(room.getId());
        String redirectedUrl = mvcResult.getResponse().getRedirectedUrl();
        Assertions.assertThat(byId.get().getBooked()).isTrue();
        Assertions.assertThat(redirectedUrl).isEqualTo("/cart");
    }
    @WithMockUser
    @MethodSource("getTourProduct")
    @ParameterizedTest
    void bookWithWrongType(Room room, Hotel hotel, Region region, Address address, User user, Destination destination) throws Exception {
        destinationRepository.save(destination);
        destination.addRegion(region);
        userRepository.save(user);
        regionRepository.save(region);
        addressRepository.save(address);
        hotel.setAddress(address);
        region.addHotel(hotel);
        hotelRepository.save(hotel);
        room.setHotel(hotel);
        roomRepository.save(room);
        roomService.prebook(roomMapper.convert(room));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/cart/book").param("id",room.getId().toString()).param("type","test_type")).andReturn();
        Optional<Room> byId = roomRepository.findById(room.getId());
        String viewName = mvcResult.getModelAndView().getViewName();
        Map<String, Object> model = mvcResult.getModelAndView().getModel();
        String exception =(String) model.get("exception");
        Assertions.assertThat(byId.get().getBooked()).isFalse();
        Assertions.assertThat(viewName).isEqualTo("exception");
        Assertions.assertThat(exception).isEqualTo("no service for test_type");

    }
    @WithMockUser
    @MethodSource("getTourProduct")
    @ParameterizedTest
    void deleteFromCart(Room room, Hotel hotel, Region region, Address address, User user, Destination destination) throws Exception {
        destinationRepository.save(destination);
        destination.addRegion(region);
        userRepository.save(user);
        regionRepository.save(region);
        addressRepository.save(address);
        hotel.setAddress(address);
        region.addHotel(hotel);
        hotelRepository.save(hotel);
        room.setHotel(hotel);
        roomRepository.save(room);
        roomService.prebook(roomMapper.convert(room));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/cart/deleteFromCart").param("id", room.getId().toString()).param("type", "Room")).andReturn();
        Optional<Room> byId = roomRepository.findById(room.getId());
        Room roomResult = byId.get();
        Assertions.assertThat(roomResult.getPreBooked()).isFalse();
        Assertions.assertThat(roomResult.getCheckOut()).isNull();
        Assertions.assertThat(roomResult.getCheckIn()).isNull();
        Assertions.assertThat(roomResult.getPrice()).isNull();
        Assertions.assertThat(roomResult.getBoardBases()).isNull();
        String redirectedUrl = mvcResult.getResponse().getRedirectedUrl();
        Assertions.assertThat(redirectedUrl).isEqualTo("/cart");
    }
    @WithMockUser(roles="ADMIN")
    @MethodSource("getTourProduct")
    @ParameterizedTest
    void cancelBookingSuccess(Room room, Hotel hotel, Region region, Address address, User user, Destination destination) throws Exception {
        destinationRepository.save(destination);
        destination.addRegion(region);
        userRepository.save(user);
        regionRepository.save(region);
        addressRepository.save(address);
        hotel.setAddress(address);
        region.addHotel(hotel);
        hotelRepository.save(hotel);
        room.setHotel(hotel);
        roomRepository.save(room);
        roomService.prebook(roomMapper.convert(room));
        roomService.book(room.getId());
        Optional<Cart> cartOptional = cartRepository.findByUserUsername(user.getUsername());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/cart/cancelBooking").param("idProduct", room.getId().toString()).param("type", "Room").param("idCart",cartOptional.get().getId().toString())).andReturn();
        Optional<Room> byId = roomRepository.findById(room.getId());
        Room roomResult = byId.get();
        String redirectedUrl = mvcResult.getResponse().getRedirectedUrl();
        Assertions.assertThat(redirectedUrl).isEqualTo("/cart/allBooking");
        Assertions.assertThat(roomResult.getPreBooked()).isFalse();
        Assertions.assertThat(roomResult.getBooked()).isFalse();
        Assertions.assertThat(roomResult.getCheckOut()).isNull();
        Assertions.assertThat(roomResult.getCheckIn()).isNull();
        Assertions.assertThat(roomResult.getPrice()).isNull();
        Assertions.assertThat(roomResult.getBoardBases()).isNull();

    }
    @WithMockUser
    @MethodSource("getTourProduct")
    @ParameterizedTest
    void cancelBookingFailed(Room room, Hotel hotel, Region region, Address address, User user, Destination destination) throws Exception {
        destinationRepository.save(destination);
        destination.addRegion(region);
        userRepository.save(user);
        regionRepository.save(region);
        addressRepository.save(address);
        hotel.setAddress(address);
        region.addHotel(hotel);
        hotelRepository.save(hotel);
        room.setHotel(hotel);
        roomRepository.save(room);
        roomService.prebook(roomMapper.convert(room));
        roomService.book(room.getId());
        Optional<Cart> cartOptional = cartRepository.findByUserUsername(user.getUsername());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/cart/cancelBooking").param("idProduct", room.getId().toString()).param("type", "Room").param("idCart",cartOptional.get().getId().toString())).andReturn();
        String errorMessage = mvcResult.getResponse().getErrorMessage();
        Optional<Room> byId = roomRepository.findById(room.getId());
        Room roomResult = byId.get();
        Assertions.assertThat(roomResult).isEqualTo(room);
        Assertions.assertThat(errorMessage).isEqualTo("Forbidden");

    }
    @WithMockUser(roles="ADMIN")
    @MethodSource("getTourProduct")
    @ParameterizedTest
    void getAllBookingSuccess(Room room, Hotel hotel, Region region, Address address, User user, Destination destination) throws Exception {
        destinationRepository.save(destination);
        destination.addRegion(region);
        userRepository.save(user);
        regionRepository.save(region);
        addressRepository.save(address);
        hotel.setAddress(address);
        region.addHotel(hotel);
        hotelRepository.save(hotel);
        room.setHotel(hotel);
        roomRepository.save(room);
        roomService.prebook(roomMapper.convert(room));
        roomService.book(room.getId());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/cart/allBooking")).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();
        Map<String, Object> model = modelAndView.getModel();
        Boolean isAdmin = (Boolean) model.get("isAdmin");
        Map<UUID, List<TourProduct>> allBooking = (Map<UUID, List<TourProduct>>) model.get("allBooking");
        Assertions.assertThat(allBooking).containsValue(List.of(room));
        Assertions.assertThat(isAdmin).isTrue();
        Assertions.assertThat(viewName).isEqualTo("bookingManager");
    }
    @WithMockUser
    @MethodSource("getTourProduct")
    @ParameterizedTest
    void getAllBookingFailed(Room room, Hotel hotel, Region region, Address address, User user, Destination destination) throws Exception {
        destinationRepository.save(destination);
        destination.addRegion(region);
        userRepository.save(user);
        regionRepository.save(region);
        addressRepository.save(address);
        hotel.setAddress(address);
        region.addHotel(hotel);
        hotelRepository.save(hotel);
        room.setHotel(hotel);
        roomRepository.save(room);
        roomService.prebook(roomMapper.convert(room));
        roomService.book(room.getId());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/cart/allBooking")).andReturn();
        String errorMessage = mvcResult.getResponse().getErrorMessage();
        Assertions.assertThat(errorMessage).isEqualTo("Forbidden");
    }
    public static Stream<Arguments> getTourProduct(){
        Hotel hotel = new Hotel();
        hotel.setName("TEST_NAME");
        hotel.setDescription("TEST_DESCRIPTION");
        hotel.setBasicPriceOfRoomPerDay(new BigDecimal("89.90"));
        hotel.setBoardBasisSet(Set.of(BoardBasisTypes.FULL_BOARD));
        hotel.setTypeByStars(HotelTypeByStars.FIVE_STAR);
        hotel.setTypeByTargetMarket(HotelTypeByTargetMarket.BUSINESS);

        Destination destination = new Destination();
        destination.setName("TEST_NAME_DESTINATION");
        destination.setDescription("REST_DESCRIPTION_DESTINATION");

        Region region= new Region();
        region.setName("TEST_NAME");
        region.setDescription("TEST_REGION_DESCRIPTION");

        Address address = new Address("TEST_CITY","TEST_STREET","TEST_HOME" );
        User user = new User();
        user.setUsername("user");
        user.setPassword("password");
        user.setRole(Role.ROLE_USER);

        Room room = new Room();
        room.setNumber(1);
        room.setTypesByOccupancy(RoomTypesByOccupancy.TWIN);
        room.setTypesByView(RoomTypesByView.GARDEN);
        room.setNumOfTourist(2);
        room.setBoardBases(BoardBasisTypes.FULL_BOARD);
        room.setBooked(false);
        room.setPreBooked(false);
        room.setPrice(new BigDecimal("8000"));
        room.setCheckIn(LocalDate.now());
        room.setCheckOut(LocalDate.now().plusDays(10));
        return Stream.of(Arguments.arguments(room,hotel, region,address, user, destination));
    }


}
package org.tms.travel_agency.web;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.tms.travel_agency.domain.Address;
import org.tms.travel_agency.domain.BoardBasisTypes;
import org.tms.travel_agency.domain.Destination;
import org.tms.travel_agency.domain.Hotel;
import org.tms.travel_agency.domain.HotelTypeByStars;
import org.tms.travel_agency.domain.HotelTypeByTargetMarket;
import org.tms.travel_agency.domain.Region;
import org.tms.travel_agency.domain.Role;
import org.tms.travel_agency.domain.Room;
import org.tms.travel_agency.domain.RoomTypesByOccupancy;
import org.tms.travel_agency.domain.RoomTypesByView;
import org.tms.travel_agency.domain.User;
import org.tms.travel_agency.dto.room.RoomDetailsDto;
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
class RoomControllerTest {
    @Autowired
    public DestinationRepository destinationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private CartRepository cartRepository;


    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

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
    public void clearDB() {
        cartRepository.deleteAll();
        userRepository.deleteAll();
        destinationRepository.deleteAll();
        regionRepository.deleteAll();
        hotelRepository.deleteAll();
        roomRepository.deleteAll();
    }

    @WithMockUser
    @ParameterizedTest
    @MethodSource("getRoomForBook")
    void bookSuccess(Room room, Hotel hotel, Region region, Address address, User user, Destination destination, RoomDetailsDto roomDetailsDto) throws Exception {
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
        roomDetailsDto.setId(room.getId());
        roomService.prebook(roomDetailsDto);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/rooms/book").param("id", room.getId().toString())).andReturn();
        String viewName = mvcResult.getModelAndView().getViewName();
        RoomDetailsDto roomResult = roomService.getById(room.getId());
        Assertions.assertThat(viewName).isEqualTo("/cart");
        Assertions.assertThat(roomResult.isBooked()).isTrue();

    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void getAddRoomPageSuccess() throws Exception {
        UUID id = UUID.randomUUID();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/rooms/add").param("id", id.toString())).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();
        Map<String, Object> model = modelAndView.getModel();
        Boolean isAdmin = (Boolean) model.get("isAdmin");
        RoomDetailsDto newRoom = (RoomDetailsDto) model.get("newRoom");
        Assertions.assertThat(viewName).isEqualTo("addRoom");
        Assertions.assertThat(isAdmin).isTrue();
        Assertions.assertThat(newRoom.getIdHotel()).isEqualTo(id);
    }

    @WithMockUser
    @Test
    void getAddRoomPageFailed() throws Exception {
        UUID id = UUID.randomUUID();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/rooms/add").param("id", id.toString())).andReturn();
        String errorMessage = mvcResult.getResponse().getErrorMessage();
        Assertions.assertThat(errorMessage).isEqualTo("Forbidden");
    }

    @WithMockUser(roles = "ADMIN")
    @ParameterizedTest
    @MethodSource("addNewRoom")
    void saveRoomSuccess(Hotel hotel, Region region, Address address, Destination destination, RoomDetailsDto roomDetailsDto) throws Exception {
        destinationRepository.save(destination);
        destination.addRegion(region);
        regionRepository.save(region);
        addressRepository.save(address);
        hotel.setAddress(address);
        region.addHotel(hotel);
        hotelRepository.save(hotel);
        roomDetailsDto.setIdHotel(hotel.getId());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/rooms/add").flashAttr("newRoom", roomDetailsDto)).andReturn();
        String redirectedUrl = mvcResult.getResponse().getRedirectedUrl();
        Room room = roomRepository.findAll().get(0);
        Assertions.assertThat(redirectedUrl).isEqualTo("/hotels/hotelManager");
        Assertions.assertThat(room.getHotel()).isEqualTo(hotel);
        Assertions.assertThat(room.getNumber()).isEqualTo(roomDetailsDto.getNumber());
        Assertions.assertThat(room.getNumOfTourist()).isEqualTo(roomDetailsDto.getNumOfTourist());
        Assertions.assertThat(room.getTypesByOccupancy()).isEqualTo(roomDetailsDto.getTypesByOccupancy());
        Assertions.assertThat(room.getTypesByView()).isEqualTo(roomDetailsDto.getTypesByView());
    }

    @WithMockUser(roles = "ADMIN")
    @ParameterizedTest
    @MethodSource("addNewRoom")
    void saveRoomIfDtoHasError(Hotel hotel, Region region, Address address, Destination destination, RoomDetailsDto roomDetailsDto) throws Exception {
        destinationRepository.save(destination);
        destination.addRegion(region);
        regionRepository.save(region);
        addressRepository.save(address);
        hotel.setAddress(address);
        region.addHotel(hotel);
        hotelRepository.save(hotel);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/rooms/add").flashAttr("newRoom", roomDetailsDto)).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();
        Map<String, Object> model = modelAndView.getModel();
        BindingResult bindingResult = (BindingResult) model.get("org.springframework.validation.BindingResult.newRoom");
        boolean hasFieldErrors = bindingResult.hasFieldErrors("idHotel");
        List<Room> all = roomRepository.findAll();
        Assertions.assertThat(hasFieldErrors).isTrue();
        Assertions.assertThat(viewName).isEqualTo("/addRoom");
        Assertions.assertThat(all).isEmpty();
    }

    @WithMockUser
    @ParameterizedTest
    @MethodSource("addNewRoom")
    void saveRoomIfUser(Hotel hotel, Region region, Address address, Destination destination, RoomDetailsDto roomDetailsDto) throws Exception {
        destinationRepository.save(destination);
        destination.addRegion(region);
        regionRepository.save(region);
        addressRepository.save(address);
        hotel.setAddress(address);
        region.addHotel(hotel);
        hotelRepository.save(hotel);
        roomDetailsDto.setIdHotel(hotel.getId());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/rooms/add").flashAttr("newRoom", roomDetailsDto)).andReturn();
        String errorMessage = mvcResult.getResponse().getErrorMessage();
        List<Room> all = roomRepository.findAll();
        Assertions.assertThat(errorMessage).isEqualTo("Forbidden");
        Assertions.assertThat(all).isEmpty();
    }

    @WithMockUser(roles = "ADMIN")
    @ParameterizedTest
    @MethodSource("getRoomForChange")
    void deleteRoomSuccess(Room room, Hotel hotel, Region region, Address address, Destination destination) throws Exception {
        destinationRepository.save(destination);
        destination.addRegion(region);
        regionRepository.save(region);
        addressRepository.save(address);
        hotel.setAddress(address);
        region.addHotel(hotel);
        hotelRepository.save(hotel);
        room.setHotel(hotel);
        roomRepository.save(room);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/rooms/delete").param("id", room.getId().toString())).andReturn();
        String redirectedUrl = mvcResult.getResponse().getRedirectedUrl();
        List<Room> all = roomRepository.findAll();
        Assertions.assertThat(all).isEmpty();
        Assertions.assertThat(redirectedUrl).isEqualTo("/hotels/hotelManager");
    }

    @WithMockUser
    @ParameterizedTest
    @MethodSource("getRoomForChange")
    void deleteRoomFailed(Room room, Hotel hotel, Region region, Address address, Destination destination) throws Exception {
        destinationRepository.save(destination);
        destination.addRegion(region);
        regionRepository.save(region);
        addressRepository.save(address);
        hotel.setAddress(address);
        region.addHotel(hotel);
        hotelRepository.save(hotel);
        room.setHotel(hotel);
        roomRepository.save(room);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/rooms/delete").param("id", room.getId().toString())).andReturn();
        String errorMessage = mvcResult.getResponse().getErrorMessage();
        List<Room> all = roomRepository.findAll();
        Assertions.assertThat(all).isNotEmpty();
        Assertions.assertThat(errorMessage).isEqualTo("Forbidden");
    }

    @WithMockUser(roles = "ADMIN")
    @ParameterizedTest
    @MethodSource("getRoomForChange")
    void getUpdatePageSuccess(Room room, Hotel hotel, Region region, Address address, Destination destination) throws Exception {
        destinationRepository.save(destination);
        destination.addRegion(region);
        regionRepository.save(region);
        addressRepository.save(address);
        hotel.setAddress(address);
        region.addHotel(hotel);
        hotelRepository.save(hotel);
        room.setHotel(hotel);
        roomRepository.save(room);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/rooms/update").param("id", room.getId().toString())).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();
        Map<String, Object> model = modelAndView.getModel();
        RoomDetailsDto currentRoom = (RoomDetailsDto) model.get("currentRoom");
        Boolean isAdmin = (Boolean) model.get("isAdmin");
        Assertions.assertThat(isAdmin).isTrue();
        Assertions.assertThat(currentRoom).isEqualTo(roomMapper.convert(room));
        Assertions.assertThat(viewName).isEqualTo("updateRoom");
    }

    @WithMockUser
    @ParameterizedTest
    @MethodSource("getRoomForChange")
    void getUpdatePageFailed(Room room, Hotel hotel, Region region, Address address, Destination destination) throws Exception {
        destinationRepository.save(destination);
        destination.addRegion(region);
        regionRepository.save(region);
        addressRepository.save(address);
        hotel.setAddress(address);
        region.addHotel(hotel);
        hotelRepository.save(hotel);
        room.setHotel(hotel);
        roomRepository.save(room);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/rooms/update").param("id", room.getId().toString())).andReturn();
        String errorMessage = mvcResult.getResponse().getErrorMessage();
        Assertions.assertThat(errorMessage).isEqualTo("Forbidden");
    }

    @WithMockUser(roles = "ADMIN")
    @ParameterizedTest
    @MethodSource("update")
    void updateSuccess(Room room, Hotel hotel, Region region, Address address, Destination destination, RoomDetailsDto roomDetailsDto) throws Exception {
        destinationRepository.save(destination);
        destination.addRegion(region);
        regionRepository.save(region);
        addressRepository.save(address);
        hotel.setAddress(address);
        region.addHotel(hotel);
        hotelRepository.save(hotel);
        room.setHotel(hotel);
        roomRepository.save(room);
        roomDetailsDto.setId(room.getId());
        roomDetailsDto.setIdHotel(hotel.getId());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/rooms/update").flashAttr("currentRoom", roomDetailsDto)).andReturn();
        String redirectedUrl = mvcResult.getResponse().getRedirectedUrl();
        Optional<Room> byId = roomRepository.findById(room.getId());
        Assertions.assertThat(byId.get().getTypesByView()).isEqualTo(roomDetailsDto.getTypesByView());
        Assertions.assertThat(redirectedUrl).isEqualTo("/rooms/details/?id=" + room.getId());
    }

    @WithMockUser
    @ParameterizedTest
    @MethodSource("update")
    void updateFailedIfUser(Room room, Hotel hotel, Region region, Address address, Destination destination, RoomDetailsDto roomDetailsDto) throws Exception {
        destinationRepository.save(destination);
        destination.addRegion(region);
        regionRepository.save(region);
        addressRepository.save(address);
        hotel.setAddress(address);
        region.addHotel(hotel);
        hotelRepository.save(hotel);
        room.setHotel(hotel);
        roomRepository.save(room);
        roomDetailsDto.setId(room.getId());
        roomDetailsDto.setIdHotel(hotel.getId());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/rooms/update").flashAttr("currentRoom", roomDetailsDto)).andReturn();
        String errorMessage = mvcResult.getResponse().getErrorMessage();
        Optional<Room> byId = roomRepository.findById(room.getId());
        Assertions.assertThat(byId.get().getTypesByView()).isNotEqualTo(roomDetailsDto.getTypesByView());
        Assertions.assertThat(errorMessage).isEqualTo("Forbidden");

    }

    @WithMockUser(roles = "ADMIN")
    @ParameterizedTest
    @MethodSource("update")
    void updateFailedIfDtoHasError(Room room, Hotel hotel, Region region, Address address, Destination destination, RoomDetailsDto roomDetailsDto) throws Exception {
        destinationRepository.save(destination);
        destination.addRegion(region);
        regionRepository.save(region);
        addressRepository.save(address);
        hotel.setAddress(address);
        region.addHotel(hotel);
        hotelRepository.save(hotel);
        room.setHotel(hotel);
        roomRepository.save(room);
        roomDetailsDto.setId(room.getId());
        roomDetailsDto.setIdHotel(hotel.getId());
        roomDetailsDto.setTypesByOccupancy(null);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/rooms/update").flashAttr("currentRoom", roomDetailsDto)).andReturn();
        Optional<Room> byId = roomRepository.findById(room.getId());
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();
        Map<String, Object> model = modelAndView.getModel();
        BindingResult bindingResult = (BindingResult) model.get("org.springframework.validation.BindingResult.currentRoom");
        boolean hasFieldErrors = bindingResult.hasFieldErrors("typesByOccupancy");
        Assertions.assertThat(hasFieldErrors).isTrue();
        Assertions.assertThat(viewName).isEqualTo("updateRoom");
        Assertions.assertThat(byId.get().getTypesByView()).isNotEqualTo(roomDetailsDto.getTypesByView());

    }

    @WithMockUser(roles = "ADMIN")
    @ParameterizedTest
    @MethodSource("getRoomForChange")
    void getDetailsForAdminSuccess(Room room, Hotel hotel, Region region, Address address, Destination destination) throws Exception {
        destinationRepository.save(destination);
        destination.addRegion(region);
        regionRepository.save(region);
        addressRepository.save(address);
        hotel.setAddress(address);
        region.addHotel(hotel);
        hotelRepository.save(hotel);
        room.setHotel(hotel);
        roomRepository.save(room);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/rooms/details").param("id", room.getId().toString())).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();
        Map<String, Object> model = modelAndView.getModel();
        Boolean isAdmin = (Boolean) model.get("isAdmin");
        RoomDetailsDto currentRoom = (RoomDetailsDto) model.get("currentRoom");
        Assertions.assertThat(isAdmin).isTrue();
        Assertions.assertThat(currentRoom).isEqualTo(roomMapper.convert(room));
        Assertions.assertThat(viewName).isEqualTo("roomDetailsForAdmin");
    }

    @WithMockUser
    @ParameterizedTest
    @MethodSource("getRoomForChange")
    void getDetailsForAdminSFailed(Room room, Hotel hotel, Region region, Address address, Destination destination) throws Exception {
        destinationRepository.save(destination);
        destination.addRegion(region);
        regionRepository.save(region);
        addressRepository.save(address);
        hotel.setAddress(address);
        region.addHotel(hotel);
        hotelRepository.save(hotel);
        room.setHotel(hotel);
        roomRepository.save(room);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/rooms/details").param("id", room.getId().toString())).andReturn();
        String errorMessage = mvcResult.getResponse().getErrorMessage();
        Assertions.assertThat(errorMessage).isEqualTo("Forbidden");
    }

    public static Stream<Arguments> getRoomForBook() {
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

        Region region = new Region();
        region.setName("TEST_NAME");
        region.setDescription("TEST_REGION_DESCRIPTION");

        Address address = new Address("TEST_CITY", "TEST_STREET", "TEST_HOME");
        User user = new User();
        user.setUsername("user");
        user.setPassword("password");
        user.setRole(Role.ROLE_USER);

        Room room = new Room();
        room.setNumber(1);
        room.setTypesByOccupancy(RoomTypesByOccupancy.TWIN);
        room.setTypesByView(RoomTypesByView.GARDEN);
        room.setNumOfTourist(2);
        room.setBooked(false);
        room.setPreBooked(false);
        RoomDetailsDto roomDetailsDto = new RoomDetailsDto();
        roomDetailsDto.setPrice(new BigDecimal("9000"));
        roomDetailsDto.setCheckIn(LocalDate.now());
        roomDetailsDto.setCheckOut(LocalDate.now().plusDays(10));
        roomDetailsDto.setBoardBases(BoardBasisTypes.FULL_BOARD);

        return Stream.of(Arguments.arguments(room, hotel, region, address, user, destination, roomDetailsDto));
    }

    public static Stream<Arguments> addNewRoom() {
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

        Region region = new Region();
        region.setName("TEST_NAME");
        region.setDescription("TEST_REGION_DESCRIPTION");

        Address address = new Address("TEST_CITY", "TEST_STREET", "TEST_HOME");

        RoomDetailsDto roomDetailsDto = new RoomDetailsDto();
        roomDetailsDto.setNumber(1);
        roomDetailsDto.setNumOfTourist(2);
        roomDetailsDto.setTypesByOccupancy(RoomTypesByOccupancy.TWIN);
        roomDetailsDto.setTypesByView(RoomTypesByView.GARDEN);

        return Stream.of(Arguments.arguments(hotel, region, address, destination, roomDetailsDto));
    }

    public static Stream<Arguments> getRoomForChange() {
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

        Region region = new Region();
        region.setName("TEST_NAME");
        region.setDescription("TEST_REGION_DESCRIPTION");

        Address address = new Address("TEST_CITY", "TEST_STREET", "TEST_HOME");

        Room room = new Room();
        room.setNumber(1);
        room.setTypesByOccupancy(RoomTypesByOccupancy.TWIN);
        room.setTypesByView(RoomTypesByView.GARDEN);
        room.setNumOfTourist(2);
        room.setBooked(false);
        room.setPreBooked(false);

        return Stream.of(Arguments.arguments(room, hotel, region, address, destination));
    }

    public static Stream<Arguments> update() {
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

        Region region = new Region();
        region.setName("TEST_NAME");
        region.setDescription("TEST_REGION_DESCRIPTION");

        Address address = new Address("TEST_CITY", "TEST_STREET", "TEST_HOME");

        Room room = new Room();
        room.setNumber(1);
        room.setTypesByOccupancy(RoomTypesByOccupancy.TWIN);
        room.setTypesByView(RoomTypesByView.GARDEN);
        room.setNumOfTourist(2);
        room.setBooked(false);
        room.setPreBooked(false);

        RoomDetailsDto roomDetailsDto = new RoomDetailsDto();
        roomDetailsDto.setNumber(1);
        roomDetailsDto.setDestination(destination.getName());
        roomDetailsDto.setBooked(false);
        roomDetailsDto.setPreBooked(false);
        roomDetailsDto.setNumOfTourist(2);
        roomDetailsDto.setTypesByOccupancy(RoomTypesByOccupancy.TWIN);
        roomDetailsDto.setTypesByView(RoomTypesByView.POOL);
        roomDetailsDto.setRegion(region.getName());
        roomDetailsDto.setHotelName(hotel.getName());
        return Stream.of(Arguments.arguments(room, hotel, region, address, destination, roomDetailsDto));
    }

}
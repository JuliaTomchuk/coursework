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
import org.tms.travel_agency.domain.Hotel;
import org.tms.travel_agency.domain.HotelTypeByStars;
import org.tms.travel_agency.domain.HotelTypeByTargetMarket;
import org.tms.travel_agency.domain.Rating;
import org.tms.travel_agency.domain.Region;
import org.tms.travel_agency.domain.Review;
import org.tms.travel_agency.domain.Room;
import org.tms.travel_agency.domain.RoomTypesByOccupancy;
import org.tms.travel_agency.domain.RoomTypesByView;
import org.tms.travel_agency.dto.hotel.HotelDetailsDto;
import org.tms.travel_agency.dto.hotel.HotelLightDto;
import org.tms.travel_agency.dto.region.RegionLightDto;
import org.tms.travel_agency.dto.review.ReviewLightDto;
import org.tms.travel_agency.dto.room.RoomLightDto;
import org.tms.travel_agency.mapper.HotelMapper;
import org.tms.travel_agency.mapper.RegionMapper;
import org.tms.travel_agency.mapper.ReviewMapper;
import org.tms.travel_agency.mapper.RoomMapper;
import org.tms.travel_agency.repository.AddressRepository;
import org.tms.travel_agency.repository.HotelRepository;
import org.tms.travel_agency.repository.RegionRepository;
import org.tms.travel_agency.repository.ReviewRepository;
import org.tms.travel_agency.repository.RoomRepository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class HotelControllerTest {
    @Autowired
   private  WebApplicationContext context;
    @Autowired
    private ReviewRepository reviewRepository;
   private MockMvc mockMvc;
   @Autowired
   private RegionRepository regionRepository;
   @Autowired
   private RegionMapper regionMapper;
   @Autowired
   private HotelRepository hotelRepository;
   @Autowired
   private HotelMapper hotelMapper;
   @Autowired
   private AddressRepository addressRepository;
   @Autowired
   private RoomRepository roomRepository;
   @Autowired
   private RoomMapper roomMapper;
   @Autowired
   private ReviewMapper reviewMapper;
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
    public void setup(){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

    }
    @BeforeEach
    void clearDB(){

        regionRepository.deleteAll();
        hotelRepository.deleteAll();
        addressRepository.deleteAll();
        roomRepository.deleteAll();

           }

    @WithMockUser(roles="ADMIN")
    @ParameterizedTest
    @MethodSource("getRegionList")
    void getHotelCreatorPageSuccess(List<Region> regionList) throws Exception {
        regionRepository.saveAll(regionList);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/hotels/create")).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();
        Map<String, Object> model = modelAndView.getModel();
        List<RegionLightDto> regions =(List<RegionLightDto>) model.get("regions");
        Boolean isAdmin =(Boolean) model.get("isAdmin");
        Assertions.assertThat(viewName).isEqualTo("/hotelCreator");
        Assertions.assertThat(regions).isEqualTo(regionMapper.convert(regionList));
        Assertions.assertThat(isAdmin).isTrue();
    }
    @WithMockUser
    @ParameterizedTest
    @MethodSource("getRegionList")
    void getHotelCreatorPageIfUser(List<Region> regionList) throws Exception {
        regionRepository.saveAll(regionList);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/hotels/create")).andReturn();
        String errorMessage = mvcResult.getResponse().getErrorMessage();
        Assertions.assertThat(errorMessage).isEqualTo("Forbidden");
    }
    @WithMockUser(roles="ADMIN")
    @ParameterizedTest
    @MethodSource("getHotelDetailsDtoAndRegion")
    @Transactional
    void saveSuccess(Region region, HotelDetailsDto hotelDetailsDto) throws Exception {
        regionRepository.save(region);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/hotels/create").flashAttr("newHotel", hotelDetailsDto)).andReturn();
        String viewName = mvcResult.getModelAndView().getViewName();
        Hotel hotelSaved = hotelRepository.findByRegionName(region.getName()).get(0);
        Assertions.assertThat(viewName).isEqualTo("redirect:/hotels/hotelManager");
        hotelDetailsDto.setId(hotelSaved.getId());
        HotelDetailsDto result= hotelMapper.convert(hotelSaved);
        Assertions.assertThat(result).isEqualTo(hotelDetailsDto);
    }
    @WithMockUser
    @ParameterizedTest
    @MethodSource("getHotelDetailsDtoAndRegion")
    void saveFailedIfUser(Region region, HotelDetailsDto hotelDetailsDto) throws Exception {
        regionRepository.save(region);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/hotels/create").flashAttr("newHotel", hotelDetailsDto)).andReturn();
        String errorMessage = mvcResult.getResponse().getErrorMessage();
        List<Hotel> hotelList = hotelRepository.findByRegionName(region.getName());
        Assertions.assertThat(errorMessage).isEqualTo("Forbidden");
        Assertions.assertThat(hotelList).isEmpty();
    }
    @WithMockUser(roles="ADMIN")
    @ParameterizedTest
    @MethodSource("getHotelDetailsDtoAndRegion")
    void saveFailedIfDtoHasError(Region region, HotelDetailsDto hotelDetailsDto) throws Exception {
        regionRepository.save(region);
        hotelDetailsDto.setDescription(null);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/hotels/create").flashAttr("newHotel", hotelDetailsDto)).andReturn();
        List<Hotel> hotelList = hotelRepository.findByRegionName(region.getName());
        ModelAndView modelAndView = mvcResult.getModelAndView();
        Map<String, Object> model = modelAndView.getModel();
        BindingResult bindingResult =(BindingResult) model.get("org.springframework.validation.BindingResult.newHotel");
        boolean hasFieldErrors = bindingResult.hasFieldErrors("description");
        Assertions.assertThat(hotelList).isEmpty();
        Assertions.assertThat(hasFieldErrors).isTrue();
    }
    @WithMockUser(roles="ADMIN")
    @ParameterizedTest
    @MethodSource("getHotelManagerPage")
    void getHotelManagerPageSuccess(List<Hotel> hotels, Region region,List<Address>addresses) throws Exception {
        regionRepository.save(region);
        addressRepository.save(addresses.get(0));
        addressRepository.save(addresses.get(1));
        hotels.get(0).setAddress(addresses.get(0));
        hotels.get(1).setAddress(addresses.get(1));
        hotels.forEach(region::addHotel);
        hotelRepository.saveAll(hotels);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/hotels/hotelManager")).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();
        Map<String, Object> model = modelAndView.getModel();
        List<HotelLightDto> hotelDtoList = (List<HotelLightDto>) model.get("hotels");
        Boolean isAdmin = (Boolean) model.get("isAdmin");
        Assertions.assertThat(viewName).isEqualTo("hotelManager");
        Assertions.assertThat(isAdmin).isTrue();
        Assertions.assertThat(hotelDtoList).isEqualTo(hotelMapper.convert(hotels));
    }
    @WithMockUser
    @Test
    void getHotelManagerPageIfUser() throws Exception {
         MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/hotels/hotelManager")).andReturn();
        String errorMessage = mvcResult.getResponse().getErrorMessage();
        Assertions.assertThat(errorMessage).isEqualTo("Forbidden");
    }
    @WithMockUser(roles="ADMIN")
    @ParameterizedTest
    @MethodSource("getHotelWithRegionAndAddress")
    void deleteSuccess(Hotel hotel, Region region, Address address) throws Exception {
      regionRepository.save(region);
      addressRepository.save(address);
      hotel.setAddress(address);
      hotelRepository.save(hotel);
      MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/hotels/delete").param("id", hotel.getId().toString())).andReturn();
        String viewName = mvcResult.getModelAndView().getViewName();
        Optional<Hotel> byId = hotelRepository.findById(hotel.getId());
        Assertions.assertThat(viewName).isEqualTo("redirect:/hotels/hotelManager");
        Assertions.assertThat(byId).isEmpty();
    }
    @WithMockUser
    @ParameterizedTest
    @MethodSource("getHotelWithRegionAndAddress")
    void deleteIfUser(Hotel hotel, Region region, Address address) throws Exception {
        regionRepository.save(region);
        addressRepository.save(address);
        hotel.setAddress(address);
        hotelRepository.save(hotel);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/hotels/delete").param("id", hotel.getId().toString())).andReturn();
        Optional<Hotel> byId = hotelRepository.findById(hotel.getId());
        String errorMessage = mvcResult.getResponse().getErrorMessage();
        Assertions.assertThat(errorMessage).isEqualTo("Forbidden");
        Assertions.assertThat(byId).isPresent();
    }
    @WithMockUser(roles="ADMIN")
    @ParameterizedTest
    @MethodSource("getHotelWithRegionAndAddress")
    void getUpdatePageSuccess(Hotel hotel, Region region, Address address) throws Exception {
        regionRepository.save(region);
        addressRepository.save(address);
        hotel.setAddress(address);
        hotelRepository.save(hotel);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/hotels/update").param("id", hotel.getId().toString())).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();
        Map<String, Object> model = modelAndView.getModel();
        Boolean isAdmin =(Boolean) model.get("isAdmin");
        HotelDetailsDto hotelResult =(HotelDetailsDto) model.get("hotel");
        Assertions.assertThat(hotelResult).isEqualTo(hotelMapper.convert(hotel));
        Assertions.assertThat(viewName).isEqualTo("updateHotel");
        Assertions.assertThat(isAdmin).isTrue();

    }
    @WithMockUser
    @Test
    void getUpdatePageIfUser() throws Exception {
         MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/hotels/update").param("id", UUID.randomUUID().toString())).andReturn();
        String errorMessage = mvcResult.getResponse().getErrorMessage();
        Assertions.assertThat(errorMessage).isEqualTo("Forbidden");
    }

    @WithMockUser("ADMIN")
    @ParameterizedTest
    @MethodSource("update")
    @Transactional
    void updateSuccess(Hotel hotel, HotelDetailsDto hotelDetailsDto, Region region, Address address) throws Exception {
        addressRepository.save(address);
        regionRepository.save(region);
        hotel.setRegion(region);
        hotel.setAddress(address);
        hotelRepository.save(hotel);
        hotelDetailsDto.setId(hotel.getId());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/hotels/update/").flashAttr("hotel", hotelDetailsDto)).andReturn();
        String viewName = mvcResult.getModelAndView().getViewName();
        Optional<Hotel> byId = hotelRepository.findById(hotel.getId());
        Assertions.assertThat(viewName).isEqualTo("redirect:/hotels/hotelManager");
        Assertions.assertThat(byId.get().getDescription()).isEqualTo(hotelDetailsDto.getDescription());
    }

    @WithMockUser
    @ParameterizedTest
    @MethodSource("update")
    void updateIfUser(Hotel hotel, HotelDetailsDto hotelDetailsDto, Region region, Address address) throws Exception {
        addressRepository.save(address);
        regionRepository.save(region);
        hotel.setRegion(region);
        hotel.setAddress(address);
        hotelRepository.save(hotel);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/hotels/update").flashAttr("hotel", hotelDetailsDto)).andReturn();
        Optional<Hotel> byId = hotelRepository.findById(hotel.getId());
        String errorMessage = mvcResult.getResponse().getErrorMessage();
        Assertions.assertThat(errorMessage).isEqualTo("Forbidden");
        Assertions.assertThat(byId.get().getDescription()).isNotEqualTo(hotelDetailsDto.getDescription());
    }
    @WithMockUser(roles="ADMIN")
    @ParameterizedTest
    @MethodSource("update")
    void updateIfDtoHasError(Hotel hotel, HotelDetailsDto hotelDetailsDto, Region region, Address address) throws Exception {
        addressRepository.save(address);
        regionRepository.save(region);
        hotel.setRegion(region);
        hotel.setAddress(address);
        hotelRepository.save(hotel);
        hotelDetailsDto.setDescription(null);
        hotelDetailsDto.setId(hotel.getId());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/hotels/update/").flashAttr("hotel", hotelDetailsDto)).andReturn();
        Optional<Hotel> byId = hotelRepository.findById(hotel.getId());
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();
        Map<String, Object> model = modelAndView.getModel();
        BindingResult bindingResult =(BindingResult) model.get("org.springframework.validation.BindingResult.hotel");
        boolean hasFieldErrors = bindingResult.hasFieldErrors("description");
        Assertions.assertThat(hasFieldErrors).isTrue();
        Assertions.assertThat(byId.get().getDescription()).isNotEqualTo(hotelDetailsDto.getDescription());
        Assertions.assertThat(viewName).isEqualTo("updateHotel");
    }
    @ParameterizedTest
    @WithMockUser(roles="ADMIN")
    @MethodSource("getHotelDetails")
    void getHotelDetailsSuccess(Region region, Hotel hotel, Address address, List<Room> rooms) throws Exception {
        regionRepository.save(region);
        addressRepository.save(address);
        hotel.setAddress(address);
        hotel.setRegion(region);
        hotelRepository.save(hotel);
        rooms.forEach(hotel::addRoom);
        roomRepository.saveAll(rooms);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/hotels/details").param("id", hotel.getId().toString())).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();
        Map<String, Object> model = modelAndView.getModel();
        HotelDetailsDto hotelResult= (HotelDetailsDto) model.get("hotel");
        List<RoomLightDto> roomsResult =(List<RoomLightDto>) model.get("rooms");
        Boolean isAdmin =(Boolean) model.get("isAdmin");
        Assertions.assertThat(isAdmin).isTrue();
        Assertions.assertThat(viewName).isEqualTo("hotelDetailsForAdmin");
        Assertions.assertThat(hotelResult).isEqualTo(hotelMapper.convert(hotel));
        Assertions.assertThat(roomsResult).isEqualTo(roomMapper.convert(rooms));
    }
    @ParameterizedTest
    @WithMockUser
    @MethodSource("getHotelDetails")
    void getHotelDetailsIfUser(Region region, Hotel hotel, Address address, List<Room> rooms) throws Exception {
        regionRepository.save(region);
        addressRepository.save(address);
        hotel.setAddress(address);
        hotel.setRegion(region);
        hotelRepository.save(hotel);
        rooms.forEach(hotel::addRoom);
        roomRepository.saveAll(rooms);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/hotels/details").param("id", hotel.getId().toString())).andReturn();
        String errorMessage = mvcResult.getResponse().getErrorMessage();
        Assertions.assertThat(errorMessage).isEqualTo("Forbidden");
    }
    @WithMockUser(roles="ADMIN")
    @Test
    void getBoardBasisAddPageSuccess() throws Exception {
        UUID id = UUID.randomUUID();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/hotels/addBoardBasis").param("id", id.toString())).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();
        Map<String, Object> model = modelAndView.getModel();
        UUID idHotel = (UUID) model.get("idHotel");
        Boolean isAdmin =(Boolean) model.get("isAdmin");
        Assertions.assertThat(viewName).isEqualTo("addBoardBasis");
        Assertions.assertThat(idHotel).isEqualTo(id);
        Assertions.assertThat(isAdmin).isTrue();

    }
    @WithMockUser
    @Test
    void getBoardBasisAddPageSIfUser() throws Exception {
        UUID id = UUID.randomUUID();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/hotels/addBoardBasis").param("id", id.toString())).andReturn();
        String errorMessage = mvcResult.getResponse().getErrorMessage();
        Assertions.assertThat(errorMessage).isEqualTo("Forbidden");
    }
    @WithMockUser(roles="ADMIN")
    @ParameterizedTest
    @MethodSource("getHotelWithRegionAndAddress")
    @Transactional
    void addBoardBasisTypeSuccess(Hotel hotel, Region region, Address address) throws Exception {
        regionRepository.save(region);
        addressRepository.save(address);
        hotel.setRegion(region);
        hotel.setAddress(address);
        hotelRepository.save(hotel);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/hotels/addBoardBasis/").param("idHotel",hotel.getId().toString()).param("boardBasisType",BoardBasisTypes.All_INCLUSIVE.toString())).andReturn();
        Optional<Hotel> byId = hotelRepository.findById(hotel.getId());
         String redirectedUrl = mvcResult.getResponse().getRedirectedUrl();
        Map<String, Object> model = mvcResult.getModelAndView().getModel();
        String id =(String)model.get("id");
        Assertions.assertThat(byId.get().getBoardBasisSet()).contains(BoardBasisTypes.All_INCLUSIVE);
        Assertions.assertThat(redirectedUrl).isEqualTo("/hotels/details?id="+hotel.getId().toString());
        Assertions.assertThat(id).isEqualTo(hotel.getId().toString());
    }
    @WithMockUser
    @ParameterizedTest
    @MethodSource("getHotelWithRegionAndAddress")
    @Transactional
    void addBoardBasisTypeIfUser(Hotel hotel, Region region, Address address) throws Exception {
        regionRepository.save(region);
        addressRepository.save(address);
        hotel.setRegion(region);
        hotel.setAddress(address);
        hotelRepository.save(hotel);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/hotels/addBoardBasis").param("idHotel",hotel.getId().toString()).param("boardBasisType",BoardBasisTypes.All_INCLUSIVE.toString())).andReturn();
        Optional<Hotel> byId = hotelRepository.findById(hotel.getId());
        String errorMessage = mvcResult.getResponse().getErrorMessage();
        Assertions.assertThat(errorMessage).isEqualTo("Forbidden");
        Assertions.assertThat(byId.get().getBoardBasisSet()).doesNotContain(BoardBasisTypes.All_INCLUSIVE);

    }
    @WithMockUser(roles="ADMIN")
    @ParameterizedTest
    @MethodSource("getHotelWithRegionAndAddress")
    @Transactional
    void deleteBoardBasisTypeSuccess(Hotel hotel, Region region, Address address) throws Exception {
        regionRepository.save(region);
        addressRepository.save(address);
        hotel.setRegion(region);
        hotel.setAddress(address);
        hotelRepository.save(hotel);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/hotels/deleteBoardBasis").param("idHotel",hotel.getId().toString()).param("boardBasisType",BoardBasisTypes.FULL_BOARD.toString())).andReturn();
        Optional<Hotel> byId = hotelRepository.findById(hotel.getId());
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String redirectedUrl = mvcResult.getResponse().getRedirectedUrl();
        Map<String, Object> model = modelAndView.getModel();
        String id =(String) model.get("id");
        Assertions.assertThat(byId.get().getBoardBasisSet()).doesNotContain(BoardBasisTypes.FULL_BOARD);
        Assertions.assertThat(redirectedUrl).isEqualTo("/hotels/details?id="+hotel.getId().toString());
        Assertions.assertThat(id).isEqualTo(hotel.getId().toString());
    }
    @WithMockUser
    @ParameterizedTest
    @MethodSource("getHotelWithRegionAndAddress")
    @Transactional
    void deleteBoardBasisTypeIUser(Hotel hotel, Region region, Address address) throws Exception {
        regionRepository.save(region);
        addressRepository.save(address);
        hotel.setRegion(region);
        hotel.setAddress(address);
        hotelRepository.save(hotel);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/hotels/deleteBoardBasis").param("idHotel",hotel.getId().toString()).param("boardBasisType",BoardBasisTypes.FULL_BOARD.toString())).andReturn();
        Optional<Hotel> byId = hotelRepository.findById(hotel.getId());
        String errorMessage = mvcResult.getResponse().getErrorMessage();
        Assertions.assertThat(errorMessage).isEqualTo("Forbidden");
        Assertions.assertThat(byId.get().getBoardBasisSet()).contains(BoardBasisTypes.FULL_BOARD);
    }
    @WithMockUser
    @ParameterizedTest
    @MethodSource("getHotelDetailsForUser")
    @Transactional
        void getDetailsForUserIfUser(Region region, Hotel hotel, Address address, List<Room> rooms, Review review) throws Exception {
        regionRepository.save(region);
        addressRepository.save(address);
        hotel.setAddress(address);
        hotel.setRegion(region);
        hotelRepository.save(hotel);
        review.setHotel(hotel);
        reviewRepository.save(review);
        rooms.forEach(hotel::addRoom);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/hotels/detailsForUser").param("id",hotel.getId().toString())).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();
        Map<String, Object> model = modelAndView.getModel();
        HotelDetailsDto hotelResult =(HotelDetailsDto) model.get("hotel");
        Boolean isAdmin =(Boolean) model.get("isAdmin");
        List<ReviewLightDto> reviewList= (List<ReviewLightDto>) model.get("reviews");
        Assertions.assertThat(viewName).isEqualTo("hotelDetailsForUser");
        Assertions.assertThat(hotelResult).isEqualTo(hotelMapper.convert(hotel));
        Assertions.assertThat(isAdmin).isFalse();
        Assertions.assertThat(reviewList).isEqualTo(reviewMapper.convert(List.of(review)));

    }

    @WithMockUser(roles="ADMIN")
    @ParameterizedTest
    @MethodSource("getHotelDetailsForUser")
    @Transactional
    void getDetailsForUserIfAdmin(Region region, Hotel hotel, Address address, List<Room> rooms, Review review) throws Exception {
        regionRepository.save(region);
        addressRepository.save(address);
        hotel.setAddress(address);
        hotel.setRegion(region);
        hotelRepository.save(hotel);
        review.setHotel(hotel);
        reviewRepository.save(review);
        rooms.forEach(hotel::addRoom);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/hotels/detailsForUser").param("id", hotel.getId().toString())).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();
        Map<String, Object> model = modelAndView.getModel();
        HotelDetailsDto hotelResult = (HotelDetailsDto) model.get("hotel");
        Boolean isAdmin = (Boolean) model.get("isAdmin");
        List<ReviewLightDto> reviewList = (List<ReviewLightDto>) model.get("reviews");
        Assertions.assertThat(viewName).isEqualTo("hotelDetailsForUser");
        Assertions.assertThat(hotelResult).isEqualTo(hotelMapper.convert(hotel));
        Assertions.assertThat(isAdmin).isTrue();
        Assertions.assertThat(reviewList).isEqualTo(reviewMapper.convert(List.of(review)));
    }
    public static Stream<Arguments> getHotelDetailsForUser(){
        Hotel hotel = new Hotel();
        hotel.setName("TEST_NAME");
        hotel.setDescription("TEST_DESCRIPTION");
        hotel.setBasicPriceOfRoomPerDay(new BigDecimal("89.90"));
        hotel.setBoardBasisSet(Set.of(BoardBasisTypes.FULL_BOARD));
        hotel.setTypeByStars(HotelTypeByStars.FIVE_STAR);
        hotel.setTypeByTargetMarket(HotelTypeByTargetMarket.BUSINESS);

        Region region= new Region();
        region.setName("TEST_NAME");
        region.setDescription("TEST_REGION_DESCRIPTION");

        Address address = new Address("TEST_CITY","TEST_STREET","TEST_HOME" );
        Room room1 = new Room();
        room1.setNumber(1);
        room1.setTypesByOccupancy(RoomTypesByOccupancy.TWIN);
        room1.setTypesByView(RoomTypesByView.GARDEN);
        room1.setNumOfTourist(2);
        room1.setBoardBases(BoardBasisTypes.FULL_BOARD);
        room1.setBooked(false);
        room1.setPreBooked(false);
        Room room2 = new Room();
        room2.setNumber(2);
        room2.setTypesByOccupancy(RoomTypesByOccupancy.DOUBLE);
        room2.setTypesByView(RoomTypesByView.SEA);
        room2.setNumOfTourist(2);
        room2.setBoardBases(BoardBasisTypes.All_INCLUSIVE);
        room2.setBooked(false);
        room2.setPreBooked(false);
        List<Room> roomList = new ArrayList<>();
        roomList.add(room1);
        roomList.add(room2);

        Review review = new Review();
        review.setMessage("TEST");
        review.setRating(Rating.EXCELLENT);
        review.setDate(LocalDate.now());
        return Stream.of(Arguments.arguments(region, hotel,address,roomList,review));
    }

    public static Stream<Arguments> getHotelDetails(){
        Hotel hotel = new Hotel();
        hotel.setName("TEST_NAME");
        hotel.setDescription("TEST_DESCRIPTION");
        hotel.setBasicPriceOfRoomPerDay(new BigDecimal("89.90"));
        hotel.setBoardBasisSet(Set.of(BoardBasisTypes.FULL_BOARD));
        hotel.setTypeByStars(HotelTypeByStars.FIVE_STAR);
        hotel.setTypeByTargetMarket(HotelTypeByTargetMarket.BUSINESS);

        Region region= new Region();
        region.setName("TEST_NAME");
        region.setDescription("TEST_REGION_DESCRIPTION");

        Address address = new Address("TEST_CITY","TEST_STREET","TEST_HOME" );
        Room room1 = new Room();
        room1.setNumber(1);
        room1.setTypesByOccupancy(RoomTypesByOccupancy.TWIN);
        room1.setTypesByView(RoomTypesByView.GARDEN);
        room1.setNumOfTourist(2);
        room1.setBoardBases(BoardBasisTypes.FULL_BOARD);
        room1.setBooked(false);
        room1.setPreBooked(false);
        Room room2 = new Room();
        room2.setNumber(2);
        room2.setTypesByOccupancy(RoomTypesByOccupancy.DOUBLE);
        room2.setTypesByView(RoomTypesByView.SEA);
        room2.setNumOfTourist(2);
        room2.setBoardBases(BoardBasisTypes.All_INCLUSIVE);
        room2.setBooked(false);
        room2.setPreBooked(false);
        List<Room> roomList = new ArrayList<>();
        roomList.add(room1);
        roomList.add(room2);

        return Stream.of(Arguments.arguments(region, hotel,address,roomList));


    }
    public static Stream<Arguments> update(){
        Set<BoardBasisTypes> boardBasisTypes = new HashSet<>();
        boardBasisTypes.add(BoardBasisTypes.FULL_BOARD);
        Hotel hotel = new Hotel();
        hotel.setName("TEST_NAME");
        hotel.setDescription("TEST_DESCRIPTION");
        hotel.setBasicPriceOfRoomPerDay(new BigDecimal("89.90"));
        hotel.setBoardBasisSet(boardBasisTypes);
        hotel.setTypeByStars(HotelTypeByStars.FIVE_STAR);
        hotel.setTypeByTargetMarket(HotelTypeByTargetMarket.BUSINESS);

        HotelDetailsDto hotelDetailsDto = new HotelDetailsDto();
        hotelDetailsDto.setName("TEST_NAME");
        hotelDetailsDto.setCity("TEST_CITY");
        hotelDetailsDto.setStreet("TEST_STREET");
        hotelDetailsDto.setHome("TEST_HOME");
        hotelDetailsDto.setDescription("NEW_DESCRIPTION");
        hotelDetailsDto.setBasicPriceOfRoom(new BigDecimal("89.90"));
        hotelDetailsDto.setBoardBasisSet(boardBasisTypes);
        hotelDetailsDto.setTypeByStars(HotelTypeByStars.FIVE_STAR);
        hotelDetailsDto.setTypeByTargetMarket(HotelTypeByTargetMarket.BUSINESS);
        hotelDetailsDto.setRegion("TEST_NAME");

        Region region= new Region();
        region.setName(hotelDetailsDto.getRegion());
        region.setDescription("TEST_REGION_DESCRIPTION");

        Address address = new Address("TEST_CITY","TEST_STREET","TEST_HOME" );
        return Stream.of(Arguments.arguments(hotel,hotelDetailsDto,region, address));
            }

    public static Stream<Arguments> getHotelManagerPage(){
        Hotel hotel = new Hotel();
        hotel.setName("TEST_NAME");
        hotel.setDescription("TEST_DESCRIPTION");
        hotel.setBasicPriceOfRoomPerDay(new BigDecimal(89.90));
        hotel.setBoardBasisSet(Set.of(BoardBasisTypes.FULL_BOARD));
        hotel.setTypeByStars(HotelTypeByStars.FIVE_STAR);
        hotel.setTypeByTargetMarket(HotelTypeByTargetMarket.BUSINESS);
        Hotel hotel2 = new Hotel();
        hotel2.setName("TEST_NAME2");
        hotel2.setDescription("TEST_DESCRIPTION2");
        hotel2.setBasicPriceOfRoomPerDay(new BigDecimal(99.90));
        hotel2.setBoardBasisSet(Set.of(BoardBasisTypes.All_INCLUSIVE));
        hotel2.setTypeByStars(HotelTypeByStars.FOUR_STAR);
        hotel2.setTypeByTargetMarket(HotelTypeByTargetMarket.RESORT);
        List<Hotel> hotelList = new ArrayList<>();
        hotelList.add(hotel);
        hotelList.add(hotel2);

        Region region = new Region();
        region.setDescription("TEST_DESCRIPTION");
        region.setName("TEST_NAME");

        Address address = new Address("TEST_CITY","TEST_STREET","TEST_HOME");
        Address address2 = new Address("TEST_CITY2","TEST_STREET2","TEST_HOME2");
        List<Address> addresses = new ArrayList<>();
        addresses.add(address);
        addresses.add(address2);
        return Stream.of(Arguments.arguments(hotelList,region,addresses));
    }


    public static Stream<Arguments> getHotelDetailsDtoAndRegion(){
        HotelDetailsDto hotelDetailsDto = new HotelDetailsDto();
        hotelDetailsDto.setName("TEST_NAME");
        hotelDetailsDto.setCity("TEST_CITY");
        hotelDetailsDto.setStreet("TEST_STREET");
        hotelDetailsDto.setHome("TEST_HOME");
        hotelDetailsDto.setDescription("TEST_DESCRIPTION");
        hotelDetailsDto.setBasicPriceOfRoom(new BigDecimal("90.89"));
        hotelDetailsDto.setBoardBasisSet(Set.of(BoardBasisTypes.FULL_BOARD));
        hotelDetailsDto.setTypeByStars(HotelTypeByStars.FIVE_STAR);
        hotelDetailsDto.setTypeByTargetMarket(HotelTypeByTargetMarket.BUSINESS);
        hotelDetailsDto.setRegion("TEST_NAME");
        hotelDetailsDto.setRoomTypesByOccupancySet(new HashSet<>());
        hotelDetailsDto.setRoomTypesByViewSet(new HashSet<>());

        Region region = new Region();
        region.setDescription("TEST_DESCRIPTION");
        region.setName("TEST_NAME");
        return Stream.of(Arguments.arguments(region,hotelDetailsDto));
    }
    public static Stream<List<Region>> getRegionList(){
        List<Region> regionList = new ArrayList<>();
        Region region = new Region();
        region.setDescription("TEST1");
        region.setName("TEST1");
        regionList.add(region);
        Region region2 = new Region();
        region2.setDescription("TEST");
        region2.setName("TEST2");
        regionList.add(region2);
        return Stream.of(regionList);
    }
   public static  Stream<Arguments> getHotelWithRegionAndAddress(){
        Set<BoardBasisTypes> boardBasisTypes = new HashSet<>();
        boardBasisTypes.add(BoardBasisTypes.FULL_BOARD);
       Hotel hotel = new Hotel();
       hotel.setName("TEST_NAME");
       hotel.setDescription("TEST_DESCRIPTION");
       hotel.setBasicPriceOfRoomPerDay(new BigDecimal("89.90"));
       hotel.setBoardBasisSet(boardBasisTypes);
       hotel.setTypeByStars(HotelTypeByStars.FIVE_STAR);
       hotel.setTypeByTargetMarket(HotelTypeByTargetMarket.BUSINESS);

       Region region = new Region();
       region.setDescription("TEST_DESCRIPTION");
       region.setName("TEST_NAME");

       Address address = new Address("TEST_CITY","TEST_STREET","TEST_HOME");
       return Stream.of(Arguments.arguments(hotel,region,address));
   }

}
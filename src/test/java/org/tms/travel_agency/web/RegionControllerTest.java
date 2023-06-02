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
import org.tms.travel_agency.dto.destination.DestinationLightDto;
import org.tms.travel_agency.dto.hotel.HotelLightDto;
import org.tms.travel_agency.dto.region.RegionDetailsDto;
import org.tms.travel_agency.dto.region.RegionLightDto;
import org.tms.travel_agency.mapper.DestinationMapper;
import org.tms.travel_agency.mapper.HotelMapper;
import org.tms.travel_agency.mapper.RegionMapper;
import org.tms.travel_agency.repository.AddressRepository;
import org.tms.travel_agency.repository.DestinationRepository;
import org.tms.travel_agency.repository.HotelRepository;
import org.tms.travel_agency.repository.RegionRepository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class RegionControllerTest {
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private RegionMapper mapper;
    @Autowired
    private DestinationMapper mapperDestination;
    @Autowired
    private HotelMapper hotelMapper;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private AddressRepository addressRepository;


    @Autowired
    private WebApplicationContext context;
    @Container
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer<>("postgres:12.12")
            .withDatabaseName("postgres")
            .withUsername("postgres")
            .withPassword("14271915");

  @DynamicPropertySource
  public static void initSpringProperties(DynamicPropertyRegistry registry){
      registry.add("spring.datasource.url",()->postgreSQLContainer.getJdbcUrl());
      registry.add("spring.datasource.username",()->postgreSQLContainer.getUsername());
      registry.add("spring.datasource.password",()->postgreSQLContainer.getPassword());
  }

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }
    @BeforeEach
    public void clearDB(){
         destinationRepository.deleteAll();
    }

    @ParameterizedTest
    @WithMockUser(roles = "ADMIN")
    @MethodSource("getRegionAndDestinationList")
     void getRegionManagerPageIfAdmin(List<Region> regionList, List<Destination> destinationList) throws Exception {
        regionRepository.saveAll(regionList);
        destinationRepository.saveAll(destinationList);
        List<RegionLightDto> regionLightDtos = mapper.convert(regionList);
        List<DestinationLightDto> destinationLightDtos = mapperDestination.convert(destinationList);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/regionManager")).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();
        Map<String, Object> model = modelAndView.getModel();
        Object destinations = model.get("destinations");
        Object regions = model.get("regions");
        Boolean isAdmin =(Boolean) model.get("isAdmin");
        Assertions.assertThat(isAdmin).isTrue();
        Assertions.assertThat(regions).isEqualTo(regionLightDtos);
        Assertions.assertThat(destinations).isEqualTo(destinationLightDtos);
        Assertions.assertThat(viewName).isEqualTo("regionManager");
    }
    @Test
    @WithMockUser(roles = "USER")
    void getRegionManagerPageIfNotAdmin() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/regionManager")).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        Assertions.assertThat(modelAndView).isNull();
    }
    @ParameterizedTest
    @WithMockUser(roles = "ADMIN")
    @MethodSource("getRegionDtoAndDestination")
    void saveSuccessIfAdmin(RegionDetailsDto dto,Destination destination) throws Exception {
        destinationRepository.save(destination);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/regionManager").flashAttr("newRegion",dto)).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        Optional<Region> savedRegion = regionRepository.findByNameIgnoreCase(dto.getName());
        Region region = savedRegion.get();
        Assertions.assertThat(modelAndView.getViewName()).isEqualTo("redirect:/regionManager");
        Assertions.assertThat(region.getName()).isEqualTo(dto.getName());
        Assertions.assertThat(region.getDestination().getName()).isEqualTo(dto.getDestination());
        Assertions.assertThat(region.getDescription()).isEqualTo(dto.getDescription());

    }
    @ParameterizedTest
    @WithMockUser
    @MethodSource("getRegionDtoAndDestination")
    void saveFailedIfUser(RegionDetailsDto dto,Destination destination) throws Exception {
        destinationRepository.save(destination);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/regionManager").flashAttr("newRegion",dto)).andReturn();
        String errorMessage = mvcResult.getResponse().getErrorMessage();
        Optional<Region> savedRegion = regionRepository.findByNameIgnoreCase(dto.getName());
        Assertions.assertThat(errorMessage).isEqualTo("Forbidden");
        Assertions.assertThat(savedRegion).isEmpty();
    }

    @ParameterizedTest
    @WithMockUser(roles = "ADMIN")
    @MethodSource("getRegionDtoAndDestination")
    void saveFailedIfDtoHasError(RegionDetailsDto dto,Destination destination) throws Exception {
        destinationRepository.save(destination);
        dto.setDescription(null);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/regionManager").flashAttr("newRegion",dto)).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        Map<String, Object> model = modelAndView.getModel();
        BindingResult bindingResult =(BindingResult) model.get("org.springframework.validation.BindingResult.newRegion");
        boolean hasFieldErrors = bindingResult.hasFieldErrors("description");
        Optional<Region> savedRegion = regionRepository.findByNameIgnoreCase(dto.getName());
        Assertions.assertThat(hasFieldErrors).isTrue();
        Assertions.assertThat(savedRegion).isEmpty();
    }

    @ParameterizedTest
    @WithMockUser(roles="ADMIN")
    @MethodSource("update")
    @Transactional
    void updateSuccessIfAdmin(Region region, Destination destination, RegionDetailsDto dto) throws Exception {
        destinationRepository.deleteAll();
        destinationRepository.save(destination);
        region.setDestination(destination);
        regionRepository.save(region);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/regionManager/update").flashAttr("newRegion", dto)).andReturn();
        String viewName = mvcResult.getModelAndView().getViewName();
        Optional<Region> updateRegion = regionRepository.findByNameIgnoreCase(dto.getName());
        Assertions.assertThat(viewName).isEqualTo("redirect:/regionManager");
        Assertions.assertThat(updateRegion.get()).isEqualTo(mapper.convert(dto));
    }
    @ParameterizedTest
    @WithMockUser
    @MethodSource("update")
    @Transactional
    void updateFailedIfUser(Region region, Destination destination, RegionDetailsDto dto) throws Exception {
        destinationRepository.deleteAll();
        destinationRepository.save(destination);
        region.setDestination(destination);
        regionRepository.save(region);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/regionManager/update").flashAttr("newRegion", dto)).andReturn();
        String errorMessage = mvcResult.getResponse().getErrorMessage();
        Optional<Region> updateRegion = regionRepository.findByNameIgnoreCase(dto.getName());
        Assertions.assertThat(errorMessage).isEqualTo("Forbidden");
        Assertions.assertThat(updateRegion.get()).isEqualTo(region);
    }
    @ParameterizedTest
    @WithMockUser(roles = "ADMIN")
    @MethodSource("update")
    @Transactional
    void updateFailedIfDtoHasError(Region region, Destination destination, RegionDetailsDto dto) throws Exception {
        destinationRepository.deleteAll();
        destinationRepository.save(destination);
        region.setDestination(destination);
        regionRepository.save(region);
        dto.setDescription(null);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/regionManager/update").flashAttr("newRegion", dto)).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        Map<String, Object> model = modelAndView.getModel();
        BindingResult bindingResult =(BindingResult) model.get("org.springframework.validation.BindingResult.newRegion");
        boolean hasFieldErrors = bindingResult.hasFieldErrors("description");
        Optional<Region> updateRegion = regionRepository.findByNameIgnoreCase(dto.getName());
        Assertions.assertThat(hasFieldErrors).isTrue();
        Assertions.assertThat(updateRegion.get()).isEqualTo(region);
    }
    @ParameterizedTest
    @MethodSource("getRegionAndDestination")
    @WithMockUser(roles="ADMIN")
   void deleteSuccessIfAdmin(Destination destination, Region region) throws Exception {
        destinationRepository.save(destination);
        region.setDestination(destination);
        regionRepository.save(region);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/regionManager/delete").param("id",region.getId().toString())).andReturn();
        String viewName = mvcResult.getModelAndView().getViewName();
        Optional<Region> byId = regionRepository.findById(region.getId());
        Assertions.assertThat(viewName).isEqualTo("redirect:/regionManager");
        Assertions.assertThat(byId).isEmpty();
    }
    @ParameterizedTest
    @MethodSource("getRegionAndDestination")
    @WithMockUser()
    void deleteFailedIfUser(Destination destination, Region region) throws Exception {
        destinationRepository.save(destination);
        region.setDestination(destination);
        regionRepository.save(region);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/regionManager/delete").param("id",region.getId().toString())).andReturn();
        String errorMessage = mvcResult.getResponse().getErrorMessage();
        Optional<Region> byId = regionRepository.findById(region.getId());
        Assertions.assertThat(errorMessage).isEqualTo("Forbidden");
        Assertions.assertThat(byId).isPresent();
    }
    @WithMockUser(roles="ADMIN")
    @ParameterizedTest
    @MethodSource("getDetails")
    @Transactional
    void getDetailsIfAdmin(Region region,Destination destination,List <Hotel> hotels, List<Address> addressList) throws Exception {
        destinationRepository.save(destination);
        region.setDestination(destination);
        regionRepository.save(region);
        addressRepository.saveAll(addressList);

        hotels.get(0).setAddress(addressList.get(0));
        hotels.get(1).setAddress(addressList.get(1));
        hotels.forEach(region::addHotel);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/regionManager/details").param("id", region.getId().toString())).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        Map<String, Object> model = modelAndView.getModel();
        Boolean isAdmin = (Boolean) model.get("isAdmin");
        RegionDetailsDto regionDtoResult =(RegionDetailsDto) model.get("region");
        List<HotelLightDto> hotelDtoListResult= (List<HotelLightDto>) model.get("hotels");
        Assertions.assertThat(modelAndView.getViewName()).isEqualTo("regionDetails");
        Assertions.assertThat(isAdmin).isTrue();
        Assertions.assertThat(regionDtoResult).isEqualTo(mapper.convert(region));
        Assertions.assertThat(hotelDtoListResult).isEqualTo(hotelMapper.convert(hotels));
        hotelRepository.deleteAll();
    }
    @WithMockUser()
    @ParameterizedTest
    @MethodSource("getDetails")
    @Transactional
    void getDetailsIfUser(Region region,Destination destination,List <Hotel> hotels, List<Address> addressList) throws Exception {
        destinationRepository.save(destination);
        region.setDestination(destination);
        regionRepository.save(region);
        addressRepository.saveAll(addressList);

        hotels.get(0).setAddress(addressList.get(0));
        hotels.get(1).setAddress(addressList.get(1));
        hotels.forEach(region::addHotel);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/regionManager/details").param("id", region.getId().toString())).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        Map<String, Object> model = modelAndView.getModel();
        Boolean isAdmin = (Boolean) model.get("isAdmin");
        RegionDetailsDto regionDtoResult =(RegionDetailsDto) model.get("region");
        List<HotelLightDto> hotelDtoListResult= (List<HotelLightDto>) model.get("hotels");
        Assertions.assertThat(modelAndView.getViewName()).isEqualTo("regionDetails");
        Assertions.assertThat(isAdmin).isFalse();
        Assertions.assertThat(regionDtoResult).isEqualTo(mapper.convert(region));
        Assertions.assertThat(hotelDtoListResult).isEqualTo(hotelMapper.convert(hotels));
        hotelRepository.deleteAll();
    }

    public static Stream<Arguments> getRegionAndDestinationList(){
        List<Destination> destinationList = new ArrayList<>();
        Destination destination = new Destination();
        destination.setName("TEST1");
        destination.setDescription("TEST1");
        destinationList.add(destination);
        Destination destination2 = new Destination();
        destination2.setName("TEST2");
        destination2.setDescription("TEST2");
        destinationList.add(destination2);

        List<Region> regionList = new ArrayList<>();
        Region region = new Region();
        region.setDescription("TEST1");
        region.setName("TEST1");
        regionList.add(region);
        Region region2 = new Region();
        region2.setDescription("TEST");
        region2.setName("TEST2");
        regionList.add(region2);

        return Stream.of(Arguments.arguments(regionList,destinationList));
    }
    public static Stream<Arguments> getRegionDtoAndDestination(){
        RegionDetailsDto dto =new RegionDetailsDto();
        dto.setName("TEST_NAME");
        dto.setDescription("TEST_DESCRIPTION");
        dto.setDestination("TEST_DESTINATION");

        Destination destination = new Destination();
        destination.setName(dto.getDestination());
        destination.setDescription("TEST_DESTINATION_DESC");
        return Stream.of(Arguments.arguments(dto,destination));
    }

    public static Stream<Arguments> update() {
        Destination destination = new Destination();
        destination.setName("TEST_DESTINATION");
        destination.setDescription("TEST_DESTINATION");
        Region region = new Region();
        region.setName("TEST");
        region.setDescription("TEST_DESCRIPTION");

        RegionDetailsDto dtoUpdate = new RegionDetailsDto();
        dtoUpdate.setName(region.getName());
        dtoUpdate.setDescription("BRAND_NEW_DESCRIPTION");
        dtoUpdate.setDestination(destination.getName());
        return Stream.of(Arguments.arguments(region, destination, dtoUpdate));
    }

    public static Stream<Arguments> getRegionAndDestination(){
        Region region=new Region();
        region.setName("TEST");
        region.setDescription("TEST_DESCRIPTION");

        Destination destination = new Destination();
        destination.setName("TEST");
        destination.setDescription("TEST_DESCRIPTION");
        return Stream.of(Arguments.arguments(destination,region));
    }
    public static  Stream<Arguments> getDetails(){
        Destination destination = new Destination();
        destination.setName("TEST_NAME_DESTINATION");
        destination.setDescription("TEST_DESCRIPTION_DESTINATION");

        Region region= new Region();
        region.setName("TEST_NAME_REGION");
        region.setDescription("TEST_DESCRIPTION_REGION");
        Address address = new Address("TEST_CITY","TEST_STREET","TEST_HOME");
        Address address2 = new Address("TEST_CITY2","TEST_STREET2","TEST_HOME2");
        List<Address> addressList = new ArrayList<>();
        addressList.add(address);
        addressList.add(address2);

        Hotel hotel = new Hotel();
        hotel.setName("TEST_HOTEL_NAME");
        hotel.setDescription("TEST_HOTEL_DESCRIPTION");
        hotel.setTypeByStars(HotelTypeByStars.FIVE_STAR);
        hotel.setTypeByTargetMarket(HotelTypeByTargetMarket.BUSINESS);
        hotel.setBasicPriceOfRoomPerDay(new BigDecimal(80.90));
        hotel.setBoardBasisSet(Set.of(BoardBasisTypes.FULL_BOARD));

        Hotel hotel2 = new Hotel();
        hotel2.setName("TEST_HOTEL_NAME2");
        hotel2.setDescription("TEST_HOTEL_DESCRIPTION2");
        hotel2.setTypeByStars(HotelTypeByStars.FOUR_STAR);
        hotel2.setTypeByTargetMarket(HotelTypeByTargetMarket.RESORT);
        hotel2.setBasicPriceOfRoomPerDay(new BigDecimal(100.90));
        hotel2.setBoardBasisSet(Set.of(BoardBasisTypes.All_INCLUSIVE));
        List<Hotel> hotelList = new ArrayList<>();
        hotelList.add(hotel);
        hotelList.add(hotel2);
        return Stream.of(Arguments.arguments(region, destination, hotelList,addressList));
    }


}
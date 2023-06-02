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
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.tms.travel_agency.domain.Destination;
import org.tms.travel_agency.domain.Region;
import org.tms.travel_agency.dto.destination.DestinationDetailsDto;
import org.tms.travel_agency.dto.destination.DestinationLightDto;
import org.tms.travel_agency.dto.region.RegionLightDto;
import org.tms.travel_agency.mapper.DestinationMapper;
import org.tms.travel_agency.mapper.RegionMapper;
import org.tms.travel_agency.repository.DestinationRepository;
import org.tms.travel_agency.repository.RegionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;


import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class DestinationControllerTest {
    @Autowired
   private DestinationRepository destinationRepository;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private DestinationMapper destinationMapper;
    @Autowired
    private RegionMapper regionMapper;
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
    @BeforeEach
    public void clearDb(){
        destinationRepository.deleteAll();
    }

    @DynamicPropertySource
    public static void initSpringProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> postgreSQLContainer.getJdbcUrl());
        registry.add("spring.datasource.username", () -> postgreSQLContainer.getUsername());
        registry.add("spring.datasource.password", () -> postgreSQLContainer.getPassword());
    }

    @ParameterizedTest
    @MethodSource("getDestinationList")
    @WithMockUser(roles="ADMIN")
    void getAllDestinationIfAdmin(List<Destination> destinationList) throws Exception {
       destinationRepository.saveAll(destinationList);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/destinations")).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        Map<String, Object> model = modelAndView.getModel();
        Boolean isAdmin =(Boolean) model.get("isAdmin");
        List<DestinationLightDto> allDestinations = (List<DestinationLightDto>) model.get("allDestinations");
        String viewName = modelAndView.getViewName();
        Assertions.assertThat(viewName).isEqualTo("destinations");
        Assertions.assertThat(isAdmin).isTrue();
        Assertions.assertThat(allDestinations).isEqualTo(destinationMapper.convert(destinationList));
    }
    @ParameterizedTest
    @MethodSource("getDestinationList")
    @WithMockUser
    void getAllDestinationsIfUser(List<Destination> destinationList) throws Exception {
        destinationRepository.saveAll(destinationList);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/destinations")).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        Map<String, Object> model = modelAndView.getModel();
        Boolean isAdmin =(Boolean) model.get("isAdmin");
        List<DestinationLightDto> allDestinations = (List<DestinationLightDto>) model.get("allDestinations");
        String viewName = modelAndView.getViewName();
        Assertions.assertThat(viewName).isEqualTo("destinations");
        Assertions.assertThat(isAdmin).isFalse();
        Assertions.assertThat(allDestinations).isEqualTo(destinationMapper.convert(destinationList));
    }
    @ParameterizedTest
    @MethodSource("getDestinationList")
    @WithMockUser(roles="ADMIN")
    void getDestinationManagerPageIfAdmin(List<Destination> destinationList) throws Exception {
        destinationRepository.saveAll(destinationList);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/destinations/destinationManager")).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();
        Map<String, Object> model = modelAndView.getModel();
        List<DestinationLightDto> destinationDtoList =(List<DestinationLightDto>) model.get("destinationList");
        Boolean isAdmin = (Boolean) model.get("isAdmin");
        Assertions.assertThat(destinationDtoList).isEqualTo(destinationMapper.convert(destinationList));
        Assertions.assertThat(isAdmin).isTrue();
        Assertions.assertThat(viewName).isEqualTo("destinationManager");
    }
    @WithMockUser
    @ParameterizedTest
    @MethodSource("getDestinationList")
      void getDestinationManagerPageIfUser(List<Destination> destinationList) throws Exception {
        destinationRepository.saveAll(destinationList);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/destinations/destinationManager")).andReturn();
        String errorMessage = mvcResult.getResponse().getErrorMessage();
        Assertions.assertThat(errorMessage).isEqualTo("Forbidden");
    }
    @ParameterizedTest
    @WithMockUser(roles="ADMIN")
    @MethodSource("getDestinationDetailsDto")
    void saveSuccess(DestinationDetailsDto dto) throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/destinations/destinationManager").flashAttr("newDestination", dto)).andReturn();
        String viewName = mvcResult.getModelAndView().getViewName();
        Optional<Destination> destinationResult = destinationRepository.findByNameIgnoreCase(dto.getName());
        Assertions.assertThat(viewName).isEqualTo("redirect:/destinations/destinationManager");
        dto.setId(destinationResult.get().getId());
        Assertions.assertThat(destinationResult.get()).isEqualTo(destinationMapper.convert(dto));
    }
    @ParameterizedTest
    @WithMockUser
    @MethodSource("getDestinationDetailsDto")
    void saveFailedIfUser(DestinationDetailsDto dto) throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/destinations/destinationManager").flashAttr("newDestination", dto)).andReturn();
        String errorMessage = mvcResult.getResponse().getErrorMessage();
        Optional<Destination> destinationResult = destinationRepository.findByNameIgnoreCase(dto.getName());
        Assertions.assertThat(errorMessage).isEqualTo("Forbidden");
        Assertions.assertThat(destinationResult).isEmpty();
    }
    @ParameterizedTest
    @WithMockUser(roles="ADMIN")
    @MethodSource("getDestinationDetailsDto")
    void saveFailedIfDtoHasError(DestinationDetailsDto dto) throws Exception {
        dto.setDescription(null);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/destinations/destinationManager").flashAttr("newDestination", dto)).andReturn();
        Optional<Destination> destinationResult = destinationRepository.findByNameIgnoreCase(dto.getName());
        ModelAndView modelAndView = mvcResult.getModelAndView();
        Map<String, Object> model = modelAndView.getModel();
        BindingResult bindingResult =(BindingResult) model.get("org.springframework.validation.BindingResult.newDestination");
        boolean hasFieldErrors = bindingResult.hasFieldErrors("description");
        Assertions.assertThat(destinationResult).isEmpty();
        Assertions.assertThat(hasFieldErrors).isTrue();
    }
    @WithMockUser(roles="ADMIN")
    @ParameterizedTest
    @MethodSource("getDestination")
    void deleteSuccess(Destination destination) throws Exception {
        destinationRepository.save(destination);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/destinations/delete").param("id", destination.getId().toString())).andReturn();
        String viewName = mvcResult.getModelAndView().getViewName();
        Optional<Destination> destinationResult = destinationRepository.findById(destination.getId());
        Assertions.assertThat(destinationResult).isEmpty();
        Assertions.assertThat(viewName).isEqualTo("redirect:/destinations/destinationManager");
    }
    @WithMockUser
    @ParameterizedTest
    @MethodSource("getDestination")
    void deleteFailedIfUser(Destination destination) throws Exception {
        destinationRepository.save(destination);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/destinations/delete").param("id", destination.getId().toString())).andReturn();
        Optional<Destination> destinationResult = destinationRepository.findById(destination.getId());
        String errorMessage = mvcResult.getResponse().getErrorMessage();
        Assertions.assertThat(destinationResult).isPresent();
        Assertions.assertThat(errorMessage).isEqualTo("Forbidden");
    }
    @WithMockUser(roles="ADMIN")
    @ParameterizedTest
    @MethodSource("update")
    void updateSuccess(Destination destination, DestinationDetailsDto dto) throws Exception {
        destinationRepository.save(destination);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/destinations/destinationManager/update").flashAttr("newDestination", dto)).andReturn();
        String viewName = mvcResult.getModelAndView().getViewName();
        Optional<Destination> destinationResultOptional = destinationRepository.findById(destination.getId());
        Destination destinationResult = destinationResultOptional.get();
        Assertions.assertThat(viewName).isEqualTo("redirect:/destinations/destinationManager");
        Assertions.assertThat(destinationResult.getName()).isEqualTo(dto.getName());
        Assertions.assertThat(destinationResult.getDescription()).isEqualTo(dto.getDescription());
       }
    @WithMockUser
    @ParameterizedTest
    @MethodSource("update")
    void updateFailedIfUser(Destination destination, DestinationDetailsDto dto) throws Exception {
        destinationRepository.save(destination);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/destinations/destinationManager/update").flashAttr("newDestination", dto)).andReturn();
        String errorMessage = mvcResult.getResponse().getErrorMessage();
        Optional<Destination> destinationResultOptional = destinationRepository.findById(destination.getId());
        Destination destinationResult = destinationResultOptional.get();
        Assertions.assertThat(errorMessage).isEqualTo("Forbidden");
        Assertions.assertThat(destinationResult.getDescription()).isNotEqualTo(dto.getDescription());
    }
    @WithMockUser(roles = "ADMIN")
    @ParameterizedTest
    @MethodSource("update")
    void updateFailedIfDtoHasError(Destination destination, DestinationDetailsDto dto) throws Exception {
        destinationRepository.save(destination);
        dto.setDescription(null);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/destinations/destinationManager/update").flashAttr("newDestination", dto)).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        Optional<Destination> destinationResultOptional = destinationRepository.findById(destination.getId());
        Destination destinationResult = destinationResultOptional.get();
        Map<String, Object> model = modelAndView.getModel();
        BindingResult bindingResult =(BindingResult) model.get("org.springframework.validation.BindingResult.newDestination");
        boolean hasFieldErrors = bindingResult.hasFieldErrors("description");
        Assertions.assertThat(hasFieldErrors).isTrue();
        Assertions.assertThat(destinationResult.getDescription()).isNotEqualTo(dto.getDescription());
    }
    @WithMockUser
    @MethodSource("getDestinationAndRegionList")
    @ParameterizedTest
    void getDetailsIfUser(Destination destination, List<Region> regionList) throws Exception {
        destinationRepository.save(destination);
        regionList.forEach(destination::addRegion);
        regionRepository.saveAll(regionList);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/destinations/details").param("id", destination.getId().toString())).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();
        Map<String, Object> model = modelAndView.getModel();
        Boolean isAdmin =(Boolean) model.get("isAdmin");
        List<RegionLightDto> regionsResult = (List<RegionLightDto>) model.get("regions");
        DestinationDetailsDto destinationResult=(DestinationDetailsDto) model.get("destination");
        Assertions.assertThat(viewName).isEqualTo("destinationDetails");
        Assertions.assertThat(isAdmin).isFalse();
        Assertions.assertThat(regionsResult).isEqualTo(regionMapper.convert(regionList));
        Assertions.assertThat(destinationResult).isEqualTo(destinationMapper.convert(destination));
    }
    @WithMockUser(roles="ADMIN")
    @MethodSource("getDestinationAndRegionList")
    @ParameterizedTest
    void getDetailsIfAdmin(Destination destination, List<Region> regionList) throws Exception {
        destinationRepository.save(destination);
        regionList.forEach(destination::addRegion);
        regionRepository.saveAll(regionList);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/destinations/details").param("id", destination.getId().toString())).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();
        String viewName = modelAndView.getViewName();
        Map<String, Object> model = modelAndView.getModel();
        Boolean isAdmin =(Boolean) model.get("isAdmin");
        List<RegionLightDto> regionsResult = (List<RegionLightDto>) model.get("regions");
        DestinationDetailsDto destinationResult=(DestinationDetailsDto) model.get("destination");
        Assertions.assertThat(viewName).isEqualTo("destinationDetails");
        Assertions.assertThat(isAdmin).isTrue();
        Assertions.assertThat(regionsResult).isEqualTo(regionMapper.convert(regionList));
        Assertions.assertThat(destinationResult).isEqualTo(destinationMapper.convert(destination));
    }

    public static Stream<List<Destination>> getDestinationList(){
        Destination destination = new Destination();
        destination.setName("TEST_NAME");
        destination.setDescription("TEST_DESCRIPTION");

        Destination destination2 = new Destination();
        destination2.setName("TEST_NAME2");
        destination2.setDescription("TEST_DESCRIPTION2");
        List<Destination> destinationList = new ArrayList<>();
        destinationList.add(destination);
        destinationList.add(destination2);
        return Stream.of(destinationList);

    }
    public static Stream<DestinationDetailsDto> getDestinationDetailsDto(){
        DestinationDetailsDto dto = new DestinationDetailsDto();
        dto.setName("TEST_NAME");
        dto.setDescription("TEST_DESCRIPTION");
        return Stream.of(dto);
    }
    public static Stream<Destination> getDestination(){
        Destination destination = new Destination();
        destination.setName("TEST_NAME");
        destination.setDescription("TEST_DESCRIPTION");
        return Stream.of(destination);
    }
    public static Stream<Arguments> update(){
        Destination destination = new Destination();
        destination.setName("TEST_NAME");
        destination.setDescription("TEST_DESCRIPTION");

        DestinationDetailsDto dto = new DestinationDetailsDto();
        dto.setName("TEST_NAME");
        dto.setDescription("NEW_DESCRIPTION");

        return Stream.of(Arguments.arguments(destination,dto));
    }
    public static Stream<Arguments> getDestinationAndRegionList(){
        Destination destination = new Destination();
        destination.setName("TEST_NAME");
        destination.setDescription("TEST_DESCRIPTION");
        List<Region> regionList = new ArrayList<>();
        Region region = new Region();
        region.setDescription("TEST1");
        region.setName("TEST1");
        regionList.add(region);
        Region region2 = new Region();
        region2.setDescription("TEST");
        region2.setName("TEST2");
        regionList.add(region2);
        return Stream.of(Arguments.arguments(destination,regionList));
    }
}
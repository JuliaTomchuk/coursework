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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tms.travel_agency.domain.Role;
import org.tms.travel_agency.domain.User;
import org.tms.travel_agency.dto.user.UserFullDescriptionDto;
import org.tms.travel_agency.dto.user.UserLightDescriptionDto;
import org.tms.travel_agency.exception.DuplicateUserException;
import org.tms.travel_agency.exception.NoSuchUserException;
import org.tms.travel_agency.mapper.UserMapper;
import org.tms.travel_agency.repository.UserRepository;
import org.tms.travel_agency.services.UserService;
import org.tms.travel_agency.validator.impl.UserValidator;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration
class UserServiceImplTest {
    private UserMapper mapper;
    private UserRepository repository;
    private UserValidator validator;
    private UserService service;

    @BeforeEach
    void init() {
        mapper = Mockito.mock(UserMapper.class);
        repository = Mockito.mock(UserRepository.class);
        validator = Mockito.mock(UserValidator.class);
        service = new UserServiceImpl(mapper, repository, validator);
    }

    @ParameterizedTest
    @MethodSource("getUser")
    void loadUserByUserNameIfUserPreset(User user) {
        Mockito.when(repository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(user));
        UserDetails test = service.loadUserByUsername(user.getUsername());

        Assertions.assertThat(test).isEqualTo(user);
    }

    @Test
    void loadUserByUserNameIfUserNotPresent() {
        Mockito.when(repository.findByUsername(Mockito.anyString())).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(NoSuchUserException.class).isThrownBy(() -> service.loadUserByUsername("TEST"));
    }

    @ParameterizedTest
    @MethodSource("getUserAndUserFullDescriptionDto")
    void saveSuccess(User user, UserFullDescriptionDto dto) {
        Mockito.when(validator.isUnique(dto)).thenReturn(true);
        Mockito.when(mapper.convert(dto)).thenReturn(user);
        Mockito.when(repository.save(user)).thenReturn(user);
        Mockito.when(mapper.convert(user)).thenReturn(dto);
        UserFullDescriptionDto save = service.save(dto);
        Assertions.assertThat(save).isEqualTo(dto);

    }

    @ParameterizedTest
    @MethodSource("getUserFullDescriptionDto")
    void saveUserNotUnique(UserFullDescriptionDto dto) {
        Mockito.when(validator.isUnique(dto)).thenReturn(false);
        Assertions.assertThatExceptionOfType(DuplicateUserException.class).isThrownBy(() -> service.save(dto));
    }

    @Test
    @WithMockUser
    void updateIfUserNotExist() {
        Mockito.when(repository.findByUsername(Mockito.anyString())).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(NoSuchUserException.class).isThrownBy(() -> service.update(Mockito.any(UserFullDescriptionDto.class)));
    }

    @Test
    @WithMockUser(username = "TEST")
    void updateIfDuplicate() {
        UserFullDescriptionDto dto = new UserFullDescriptionDto();
        dto.setUsername("TEST1");

        User user = new User();
        user.setUsername("TEST");

        Mockito.when(repository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        Mockito.when(validator.isUnique(dto)).thenReturn(false);
        Assertions.assertThatExceptionOfType(DuplicateUserException.class).isThrownBy(() -> service.update(dto));
    }

    @ParameterizedTest
    @MethodSource("getUserAndUserFullDescriptionDto")
    @WithMockUser
    void successUpdate(User user, UserFullDescriptionDto dto) {
        Mockito.when(repository.findByUsername("user")).thenReturn(Optional.of(user));
        Mockito.when(mapper.update(dto, user)).thenReturn(user);
        Mockito.when(repository.save(user)).thenReturn(user);
        Mockito.when(mapper.convert(user)).thenReturn(dto);
        UserFullDescriptionDto updated = service.update(dto);
        Assertions.assertThat(updated).isEqualTo(dto);
    }

    @Test
    void deleteIfNotExist() {
        UUID uuid = UUID.randomUUID();
        Mockito.when(repository.findById(uuid)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> service.delete(uuid)).isInstanceOf(NoSuchUserException.class);

    }

    @ParameterizedTest
    @MethodSource("getUser")
    void deleteSuccess(User user) {
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.when(repository.findById(user.getId())).thenReturn(Optional.of(user));
        service.delete(user.getId());
        Mockito.verify(repository, Mockito.times(1)).delete(user);
        Mockito.verify(repository).delete(userArgumentCaptor.capture());
        User value = userArgumentCaptor.getValue();
        Assertions.assertThat(value).isEqualTo(user);
    }

    @ParameterizedTest
    @MethodSource("getUserAndUserFullDescriptionDto")
    @WithMockUser
    void getCurrentSuccess(User user, UserFullDescriptionDto dto) {
        Mockito.when(repository.findByUsername("user")).thenReturn(Optional.of(user));
        Mockito.when(mapper.convert(user)).thenReturn(dto);
        UserFullDescriptionDto current = service.getCurrent();
        Assertions.assertThat(current).isEqualTo(dto);

    }

    @Test
    @WithMockUser
    void getCurrentIfNotExist() {
        Mockito.when(repository.findByUsername("user")).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(NoSuchUserException.class).isThrownBy(() -> service.getCurrent());
    }

    @ParameterizedTest
    @MethodSource({"getUserAndLightDtoLists"})
    void getAll(List<User> userList, List<UserLightDescriptionDto> dtoList) {
        Mockito.when(repository.findAll()).thenReturn(userList);
        Mockito.when(mapper.convert(userList)).thenReturn(dtoList);
        List<UserLightDescriptionDto> all = service.getAll();
        Assertions.assertThat(all).isEqualTo(dtoList);
    }

    @ParameterizedTest
    @MethodSource("getUserAndUserFullDescriptionDto")
    void getByIdSuccess(User user, UserFullDescriptionDto dto) {
        Mockito.when(repository.findById(dto.getId())).thenReturn(Optional.of(user));
        Mockito.when(mapper.convert(user)).thenReturn(dto);
        UserFullDescriptionDto byId = service.getById(dto.getId());
        Assertions.assertThat(byId).isEqualTo(dto);
    }

    @Test
    void getByIdIfNotExist() {
        UUID uuid = UUID.randomUUID();
        Mockito.when(repository.findById(uuid)).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(NoSuchUserException.class).isThrownBy(() -> service.getById(uuid));
    }

    @ParameterizedTest
    @MethodSource("getUserAndUserFullDescriptionDto")
    void changeRoleSuccess(User user, UserFullDescriptionDto dto) {
        dto.setRole(Role.ROLE_ADMIN);
        Mockito.when(repository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(mapper.convert(user)).thenReturn(dto);
        UserFullDescriptionDto userFullDescriptionDto = service.changeRole("ROLE_ADMIN", user.getId());
        Assertions.assertThat(userFullDescriptionDto).isEqualTo(dto);
    }

    @Test
    void changeRoleIfNotExist() {
        UUID id = UUID.randomUUID();
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(NoSuchUserException.class).isThrownBy(() -> service.changeRole("TEST", id));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void isAdminTrue() {
        boolean admin = service.isAdmin();
        Assertions.assertThat(admin).isTrue();
    }

    @Test
    @WithMockUser
    void isAdminFalse() {
        boolean admin = service.isAdmin();
        Assertions.assertThat(admin).isFalse();
    }

    public static Stream<Arguments> getUserAndLightDtoLists() {
        User user1 = new User();
        user1.setId(UUID.fromString("6f4c1fce-2fa5-47c7-b358-907e36bcaf79"));
        user1.setUsername("user1");
        user1.setRole(Role.ROLE_USER);
        user1.setAge(35);
        user1.setPassword("TEST1");
        user1.setFirstName("TEST1");
        user1.setPassportNumber("TEST1");
        user1.setPatronymic("TEST1");
        user1.setSecondName("TEST1");

        User user2 = new User();
        user2.setId(UUID.fromString("d612c183-7f34-445b-898b-814532644b1c"));
        user2.setUsername("user2");
        user2.setRole(Role.ROLE_ADMIN);
        user2.setAge(40);
        user2.setPassword("TEST2");
        user2.setFirstName("TEST2");
        user2.setPassportNumber("TEST2");
        user2.setPatronymic("TEST2");
        user2.setSecondName("TEST2");


        UserLightDescriptionDto dto1 = new UserLightDescriptionDto();
        dto1.setId(UUID.fromString("6f4c1fce-2fa5-47c7-b358-907e36bcaf79"));
        dto1.setUsername("user1");
        dto1.setRole(Role.ROLE_USER);

        UserLightDescriptionDto dto2 = new UserLightDescriptionDto();
        dto2.setId(UUID.fromString("d612c183-7f34-445b-898b-814532644b1c"));
        dto2.setUsername("user2");
        dto2.setRole(Role.ROLE_ADMIN);

        return Stream.of(Arguments.arguments(Arrays.asList(user1, user2), Arrays.asList(dto1, dto2)));
    }

    public static Stream<User> getUser() {
        User user = new User();
        user.setUsername("TEST");
        user.setRole(Role.ROLE_USER);
        user.setAge(25);
        user.setPassword("TEST");
        user.setId(UUID.fromString("bb677de6-c556-4a58-b4d9-b23be4223d6e"));
        user.setFirstName("TEST");
        user.setPassportNumber("TEST");
        user.setPatronymic("TEST");
        user.setSecondName("TEST");
        return Stream.of(user);
    }

    public static Stream<Arguments> getUserAndUserFullDescriptionDto() {
        UserFullDescriptionDto dto = new UserFullDescriptionDto();
        dto.setRole(Role.ROLE_USER);
        dto.setUsername("user");
        dto.setAge(35);
        dto.setPassword("TEST");
        dto.setFirstName("TEST");
        dto.setPassportNumber("TEST");
        dto.setPatronymic("TEST");
        dto.setSecondName("TEST");
        dto.setId(UUID.fromString("a9f86bd7-0371-406f-a4fc-551df70dcad7"));

        User user = new User();
        user.setRole(Role.ROLE_USER);
        user.setUsername("user");
        user.setAge(35);
        user.setPassword("TEST");
        user.setFirstName("TEST");
        user.setPassportNumber("TEST");
        user.setPatronymic("TEST");
        user.setSecondName("TEST");
        user.setId(UUID.fromString("a9f86bd7-0371-406f-a4fc-551df70dcad7"));
        return Stream.of(Arguments.arguments(user, dto));

    }

    public static Stream<UserFullDescriptionDto> getUserFullDescriptionDto() {
        UserFullDescriptionDto dto = new UserFullDescriptionDto();
        dto.setRole(Role.ROLE_USER);
        dto.setUsername("TEST");
        dto.setAge(35);
        dto.setPassword("TEST");
        dto.setFirstName("TEST");
        dto.setPassportNumber("TEST");
        dto.setPatronymic("TEST");
        dto.setSecondName("TEST");
        return Stream.of(dto);
    }


}
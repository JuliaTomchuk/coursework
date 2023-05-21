package org.tms.travel_agency.services.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import org.tms.travel_agency.exception.DuplicateUserException;
import org.tms.travel_agency.exception.NoSuchUserException;
import org.tms.travel_agency.mapper.UserMapper;
import org.tms.travel_agency.repository.UserRepository;
import org.tms.travel_agency.services.UserService;
import org.tms.travel_agency.validator.impl.UserValidator;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration
class UserServiceImplTest {
    private  UserMapper mapper;
    private  UserRepository repository;
    private  UserValidator validator;
    private  UserService service;

    @BeforeEach
    public  void init(){
        mapper = Mockito.mock(UserMapper.class);
        repository = Mockito.mock(UserRepository.class);
        validator = Mockito.mock(UserValidator.class);
        service = new UserServiceImpl(mapper,repository,validator);
    }
    @Test
    public void loadUserByUserNameIfUserPreset(){
        User user = new User();
        user.setUsername("TEST");
        Mockito.when(repository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(user));
        UserDetails test = service.loadUserByUsername(user.getUsername());
        Assertions.assertThat(test.getUsername()).isEqualTo(user.getUsername());
    }
    @Test
    public void loadUserByUserNameIfUserNotPresent(){
        Mockito.when(repository.findByUsername(Mockito.anyString())).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(NoSuchUserException.class).isThrownBy(()->service.loadUserByUsername("TEST"));
    }
    @Test
    public void saveSuccess(){
        UserFullDescriptionDto dto = new UserFullDescriptionDto();
        dto.setRole(Role.ROLE_USER);
        dto.setUsername("TEST");
        dto.setAge(35);
        dto.setPassword("TEST");
        dto.setFirstName("TEST");
        dto.setPassportNumber("TEST");
        dto.setPatronymic("TEST");
        dto.setSecondName("TEST");

        User user = new User();
        user.setRole(dto.getRole());


        Mockito.when(validator.isUnique(dto)).thenReturn(true);
        Mockito.when(mapper.convert(dto)).thenReturn(user);
        Mockito.when(repository.save(user)).thenReturn(user);
        Mockito.when(mapper.convert(user)).thenReturn(dto);
        UserFullDescriptionDto save = service.save(dto);
        Assertions.assertThat(save).isEqualTo(dto);

    }
    @Test
    public void saveUserNotUnique(){
        UserFullDescriptionDto dto = new UserFullDescriptionDto();
        dto.setRole(Role.ROLE_USER);
        dto.setUsername("TEST");
        dto.setAge(35);
        dto.setPassword("TEST");
        dto.setFirstName("TEST");
        dto.setPassportNumber("TEST");
        dto.setPatronymic("TEST");
        dto.setSecondName("TEST");

        Mockito.when(validator.isUnique(dto)).thenReturn(false);
        Assertions.assertThatExceptionOfType(DuplicateUserException.class).isThrownBy(()->service.save(dto));
    }

    @Test
    @WithMockUser
    public void updateIfUserNotExist(){
        Mockito.when(repository.findByUsername(Mockito.anyString())).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(NoSuchUserException.class).isThrownBy(()-> service.update(Mockito.any(UserFullDescriptionDto.class)));
    }
    @Test
    @WithMockUser(username="TEST")
    public void updateIfDuplicate(){
        UserFullDescriptionDto dto = new UserFullDescriptionDto();
        dto.setUsername("TEST1");
        User user = new User();
        user.setUsername("TEST");
        Mockito.when(repository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        Mockito.when(validator.isUnique(dto)).thenReturn(false);
        Assertions.assertThatExceptionOfType(DuplicateUserException.class).isThrownBy( ()->service.update(dto));
    }

    @Test
    @WithMockUser
    public void successUpdate() {
        UserFullDescriptionDto dto = new UserFullDescriptionDto();
        dto.setUsername("user");
        User user = new User();
        user.setUsername("user");
        Mockito.when(repository.findByUsername("user")).thenReturn(Optional.of(user));
        Mockito.when(mapper.update(dto, user)).thenReturn(user);
        Mockito.when(repository.save(user)).thenReturn(user);
        Mockito.when(mapper.convert(user)).thenReturn(dto);
        UserFullDescriptionDto updated = service.update(dto);
        Assertions.assertThat(updated).isEqualTo(dto);
    }

    @Test
    public void deleteIfNotExist(){
        UUID uuid=UUID.randomUUID();
        Mockito.when(repository.findById(uuid)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(()->service.delete(uuid)).isInstanceOf(NoSuchUserException.class);

    }
    @Test
    public void deleteSuccess(){
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        UUID uuid=UUID.randomUUID();
        User user = new User();
        user.setId(uuid);
        Mockito.when(repository.findById(uuid)).thenReturn(Optional.of(user));
        service.delete(uuid);
        Mockito.verify(repository,Mockito.times(1)).delete(user);
        Mockito.verify(repository).delete(userArgumentCaptor.capture());
        User value = userArgumentCaptor.getValue();
        Assertions.assertThat(value.getId()).isEqualTo(uuid);
    }
    @Test
    @WithMockUser
    public void getCurrentSuccess(){
        User user = new User();
        user.setUsername("user");
        UserFullDescriptionDto dto = new UserFullDescriptionDto();
        dto.setUsername(user.getUsername());
        Mockito.when(repository.findByUsername("user")).thenReturn(Optional.of(user));
        Mockito.when(mapper.convert(user)).thenReturn(dto);
        UserFullDescriptionDto current = service.getCurrent();
        Assertions.assertThat(current.getUsername()).isEqualTo("user");

    }
    @Test
    @WithMockUser
    public void getCurrentIfNotExist(){
        Mockito.when(repository.findByUsername("user")).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(NoSuchUserException.class).isThrownBy(()->service.getCurrent());
    }
    @Test
    public void getAll(){
        service.getAll();
        Mockito.verify(repository,Mockito.times(1)).findAll();
    }
    @Test
    public void getByIdSuccess(){
        UUID uuid =UUID.randomUUID();
        User user= new User();
        user.setId(uuid);
        UserFullDescriptionDto dto = new UserFullDescriptionDto();
        dto.setId(uuid);
        Mockito.when(repository.findById(uuid)).thenReturn(Optional.of(user));
        Mockito.when(mapper.convert(user)).thenReturn(dto);
        UserFullDescriptionDto byId = service.getById(uuid);
        Assertions.assertThat(byId).isEqualTo(dto);
    }
    @Test
    public void getByIdIfNotExist(){
        UUID uuid = UUID.randomUUID();
        Mockito.when(repository.findById(uuid)).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(NoSuchUserException.class).isThrownBy(()->service.getById(uuid));
    }
    @Test
    public void changeRoleSuccess(){
        User user = new User();
        user.setId(UUID.randomUUID());
        UserFullDescriptionDto dto = new UserFullDescriptionDto();
        dto.setId(user.getId());
        dto.setRole(Role.ROLE_ADMIN);
        Mockito.when(repository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(mapper.convert(user)).thenReturn(dto);
        UserFullDescriptionDto userFullDescriptionDto = service.changeRole("ROLE_ADMIN", user.getId());
        Assertions.assertThat(userFullDescriptionDto.getRole()).isEqualTo(Role.ROLE_ADMIN);
        Assertions.assertThat(userFullDescriptionDto.getId()).isEqualTo(user.getId());
    }

    @Test
    public void changeRoleIfNotExist(){
        UUID id = UUID.randomUUID();
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(NoSuchUserException.class).isThrownBy(()->service.changeRole("TEST",id));
    }
    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void isAdminTrue(){
        boolean admin = service.isAdmin();
        Assertions.assertThat(admin).isTrue();
    }
    @Test
    @WithMockUser
    public void isAdminFalse(){
        boolean admin = service.isAdmin();
        Assertions.assertThat(admin).isFalse();
    }

}
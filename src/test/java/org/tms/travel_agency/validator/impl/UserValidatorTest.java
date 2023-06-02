package org.tms.travel_agency.validator.impl;

import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tms.travel_agency.domain.User;
import org.tms.travel_agency.dto.user.UserFullDescriptionDto;
import org.tms.travel_agency.repository.UserRepository;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest {

    private UserRepository repository;
    private UserValidator validator;

    @BeforeEach
    void initServices() {
        repository = Mockito.mock(UserRepository.class);
        validator = new UserValidator(repository);
    }

    @Test
    void isUniqueSuccess() {
        UserFullDescriptionDto dto = new UserFullDescriptionDto();
        dto.setUsername("TEST");
        Mockito.when(repository.findByUsername(dto.getUsername())).thenReturn(Optional.empty());
        boolean unique = validator.isUnique(dto);
        Assertions.assertThat(unique).isTrue();
    }

    @Test
    void isNotUnique() {
        UserFullDescriptionDto dto = new UserFullDescriptionDto();
        dto.setUsername("TEST");
        User user = new User();
        user.setUsername("TEST");
        Mockito.when(repository.findByUsername(dto.getUsername())).thenReturn(Optional.of(user));
        boolean unique = validator.isUnique(dto);
        Assertions.assertThat(unique).isFalse();
    }
}
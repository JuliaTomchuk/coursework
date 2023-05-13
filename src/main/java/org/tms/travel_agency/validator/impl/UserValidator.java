package org.tms.travel_agency.validator.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.tms.travel_agency.domain.User;
import org.tms.travel_agency.dto.user.UserFullDescriptionDto;
import org.tms.travel_agency.repository.UserRepository;
import org.tms.travel_agency.validator.DuplicateValidator;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserValidator implements DuplicateValidator <UserFullDescriptionDto>{

    private final UserRepository repository;

    @Override
    public boolean isUnique(UserFullDescriptionDto dto) {
        Optional<User> optionalUser = repository.findByUsername(dto.getUsername());
        if (optionalUser.isPresent()) {
            return false;
        }
        return true;
    }
}

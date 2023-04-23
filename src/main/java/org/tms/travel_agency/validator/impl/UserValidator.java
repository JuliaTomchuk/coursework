package org.tms.travel_agency.validator.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.tms.travel_agency.domain.User;
import org.tms.travel_agency.repository.UserRepository;
import org.tms.travel_agency.validator.DuplicateValidator;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserValidator implements DuplicateValidator<User> {

    private final UserRepository repository;

    @Override
    public boolean isUnique(String username) {
        Optional<User> optionalUser = repository.findByUsername(username);
        if (optionalUser.isPresent()) {
            return false;
        }
        return true;
    }
}

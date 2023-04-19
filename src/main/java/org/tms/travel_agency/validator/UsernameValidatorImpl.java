package org.tms.travel_agency.validator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.tms.travel_agency.domain.User;
import org.tms.travel_agency.repository.UserRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UsernameValidatorImpl implements UsernameValidator {

    private final UserRepository repository;

    @Override
    public boolean isUsernameUnique(String username) {
        Optional<User> optionalUser = repository.findByUsername(username);
        if (optionalUser.isPresent()) {
            return false;
        }
        return true;
    }
}

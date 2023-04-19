package org.tms.travel_agency.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.tms.travel_agency.domain.Role;
import org.tms.travel_agency.domain.User;
import org.tms.travel_agency.dto.user.UserFullDescriptionDto;
import org.tms.travel_agency.exception.NoSuchUserException;
import org.tms.travel_agency.exception.UserWithThatUsernameAlreadyExistException;
import org.tms.travel_agency.mapper.UserMapper;
import org.tms.travel_agency.repository.UserRepository;
import org.tms.travel_agency.services.UserService;
import org.tms.travel_agency.validator.UsernameValidator;


import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper mapper;
    private final UserRepository repository;
    private final UsernameValidator validator;


    public UserDetails loadUserByUsername(String username) {
        User byUsername = repository.findByUsername(username).orElseThrow(() -> new NoSuchUserException("no user with username: " + username));
        return byUsername;
    }


    @Override
    public UserFullDescriptionDto save(UserFullDescriptionDto inputDto) {
        if (!validator.isUsernameUnique(inputDto.getUsername())) {
            throw new UserWithThatUsernameAlreadyExistException("A user with  username " + inputDto.getUsername() + " already exist");
        }
        User user = mapper.convert(inputDto);
        user.setRole(Role.ROLE_USER);
        User save = repository.save(user);
        return mapper.convert(user);
    }


    @Override
    public UserFullDescriptionDto update(UserFullDescriptionDto inputDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = repository.findByUsername(username).orElseThrow(() -> new NoSuchUserException("no user with username: " + username));
        if (!username.equals(inputDto.getUsername())) {
            if (!validator.isUsernameUnique(inputDto.getUsername())) {
                throw new UserWithThatUsernameAlreadyExistException("A user with  username:" + inputDto.getUsername() + " already exist");
            }
        }
        User updated = mapper.update(inputDto, user);
        repository.save(updated);
        UserFullDescriptionDto converted = mapper.convert(updated);
        return converted;
    }

    @Override
    public void delete(UUID id) {
        repository.findById(id).ifPresentOrElse((user) -> repository.delete(user), NoSuchUserException::new);
    }

    @Override
    public UserFullDescriptionDto getCurrent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) loadUserByUsername(authentication.getName());
        UserFullDescriptionDto converted = mapper.convert(user);
        return converted;
    }

    @Override
    public List<User> getAll() {
        List<User> all = repository.findAll();
        return all;
    }

    @Override
    @Transactional
    public void changeRole(String role, UUID id) {
        repository.findById(id).ifPresentOrElse(user -> user.setRole(Role.valueOf(role)), NoSuchUserException::new);

    }


}

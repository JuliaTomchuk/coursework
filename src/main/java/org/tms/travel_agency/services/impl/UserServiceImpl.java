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
        User byUsername = repository.findByUsername(username).orElseThrow(NoSuchUserException::new);
        return byUsername;
    }


    @Override
    public void save(UserFullDescriptionDto inputDto) {
        if(validator.isUsernameUnique(inputDto.getUsername())) {
            User user = mapper.convert(inputDto);
            user.setRole(Role.ROLE_USER);
            repository.save(user);

        }
    }


   @Override
    public UserFullDescriptionDto update(UserFullDescriptionDto inputDto) {

        User user =repository.findByUsername(inputDto.getUsername()).orElseThrow(NoSuchUserException::new);
        User updated = mapper.update(inputDto, user);
        repository.save(updated);
       UserFullDescriptionDto converted = mapper.convert(updated);
     return converted;
    }

   @Override
    public void delete(UUID id) {
     repository.findById(id).ifPresentOrElse((user)->repository.delete(user), NoSuchUserException::new);
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
        repository.findById(id).ifPresentOrElse(user -> user.setRole(Role.valueOf(role)),NoSuchUserException::new);

    }


}

package org.tms.travel_agency.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.tms.travel_agency.domain.User;
import org.tms.travel_agency.dto.user.UserFullDescriptionDto;

import java.util.List;
import java.util.UUID;


public interface UserService extends UserDetailsService {

    void save(UserFullDescriptionDto inputDto);
    UserFullDescriptionDto update(UserFullDescriptionDto inputDto);
    void delete(UUID id);
    UserFullDescriptionDto getCurrent();
    List<User> getAll();
    void changeRole(String role, UUID id);
}

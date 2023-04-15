package org.tms.travel_agency.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.tms.travel_agency.dto.user.UserFullDescriptionDto;

import java.util.UUID;


public interface UserService extends UserDetailsService {

    UserFullDescriptionDto save(UserFullDescriptionDto inputDto);
    UserFullDescriptionDto update(UserFullDescriptionDto inputDto);
    void delete(UUID id);
    UserFullDescriptionDto getCurrent();
}

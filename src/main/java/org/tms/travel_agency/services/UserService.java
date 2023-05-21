package org.tms.travel_agency.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.tms.travel_agency.domain.User;
import org.tms.travel_agency.dto.user.UserFullDescriptionDto;
import org.tms.travel_agency.dto.user.UserLightDescriptionDto;

import java.util.List;
import java.util.UUID;


public interface UserService extends UserDetailsService,CRUDService<UserFullDescriptionDto, UserLightDescriptionDto> {

    UserFullDescriptionDto getCurrent();

    UserFullDescriptionDto changeRole(String role, UUID id);
    boolean isAdmin();
}

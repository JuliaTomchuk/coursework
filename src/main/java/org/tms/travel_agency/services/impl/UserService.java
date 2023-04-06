package org.tms.travel_agency.services.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.tms.travel_agency.domain.User;
import org.tms.travel_agency.services.TravelAdminService;

import java.util.List;
import java.util.UUID;

public class UserService  implements UserDetailsService, TravelAdminService<User> {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public void delete(UUID id) {

    }

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public User getById() {
        return null;
    }

    @Override
    public List<User> search(User user) {
        return null;
    }
}

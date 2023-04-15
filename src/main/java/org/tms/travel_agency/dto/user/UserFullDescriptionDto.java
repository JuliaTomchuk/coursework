package org.tms.travel_agency.dto.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.tms.travel_agency.domain.Role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFullDescriptionDto extends UserLightDescriptionDto {
    private UUID id;
    private String firstName;
    private String secondName;
    private String patronymic;
    private String username;
    private String passportNumber;
    private Integer age;
    private String password;
    private Role role;


}

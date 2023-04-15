package org.tms.travel_agency.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tms.travel_agency.domain.Role;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLightDescriptionDto {
    private UUID id;
    private String username;
    private Role role;
}

package org.tms.travel_agency.dto.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tms.travel_agency.domain.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFullDescription {
    private String firstName;
    private String secondName;
    private String patronymic;
    private String username;
    private String passwordNumber;
    private Integer age;
    private Role role;
}

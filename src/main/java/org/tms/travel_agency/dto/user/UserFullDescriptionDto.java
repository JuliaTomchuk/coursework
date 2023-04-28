package org.tms.travel_agency.dto.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.tms.travel_agency.domain.Role;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import javax.validation.constraints.Positive;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFullDescriptionDto {

    private UUID id;
    @NotBlank()
    private String firstName;
    @NotBlank()
    private String secondName;
    @NotBlank()
    private String patronymic;
    @NotBlank()
    private String username;
    @NotBlank()
    private String passportNumber;
    @NotNull()
    @Positive()
    private Integer age;
    @NotBlank()
    private String password;
    private Role role;


}

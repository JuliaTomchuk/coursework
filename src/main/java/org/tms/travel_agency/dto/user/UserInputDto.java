package org.tms.travel_agency.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserInputDto {
    @NotBlank
    private String firstName;
    @NotBlank
    private String secondName;
    @NotBlank
    private String patronymic;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String passwordNumber;
    @NotBlank
    private Integer age;
}

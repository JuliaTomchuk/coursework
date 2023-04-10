package org.tms.travel_agency.dto.review;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tms.travel_agency.validator.OnCreate;
import org.tms.travel_agency.validator.OnUpdate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewInputDto {
    @Null(groups = {OnCreate.class})
    @NotNull(groups ={OnUpdate.class})
    private UUID id;
    @NotBlank(groups = {OnUpdate.class, OnCreate.class})
    private String message;
    @NotNull(groups = {OnUpdate.class, OnCreate.class})
    private UUID hotelId;
}

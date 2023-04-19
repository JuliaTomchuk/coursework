package org.tms.travel_agency.dto.hotel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tms.travel_agency.domain.HotelTypeByStars;
import org.tms.travel_agency.domain.HotelTypeByTargetMarket;
import org.tms.travel_agency.domain.Region;
import org.tms.travel_agency.validator.OnCreate;
import org.tms.travel_agency.validator.OnUpdate;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class HotelInputDto {
    @NotNull(groups = {OnUpdate.class})
    @Null(groups={OnCreate.class})
    private UUID id;
    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    private String name;
    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    private BigDecimal basicPriceOfRoom;
    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    private String city;
    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    private String street;
    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    private String home;
    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    private String region;
    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    private String description;
    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    private HotelTypeByStars typeByStars;
    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    private HotelTypeByTargetMarket typeByTargetMarket;

}

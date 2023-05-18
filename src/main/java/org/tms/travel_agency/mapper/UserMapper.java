package org.tms.travel_agency.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.tms.travel_agency.domain.User;
import org.tms.travel_agency.dto.user.UserFullDescriptionDto;
import org.tms.travel_agency.dto.user.UserLightDescriptionDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserFullDescriptionDto convert(User user);

    User convert(UserFullDescriptionDto userFullDescriptionDto);
    List<UserLightDescriptionDto> convert(List<User> users);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    User update(UserFullDescriptionDto userFullDescriptionDto, @MappingTarget User user);

}

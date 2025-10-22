package com.challenge.geosapiens.service_a.infrastructure.mapper;

import com.challenge.geosapiens.service_a.domain.entity.User;
import com.challenge.geosapiens.service_a.infrastructure.dto.request.UserRequest;
import com.challenge.geosapiens.service_a.infrastructure.dto.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toDomain (UserRequest userRequest);
    UserResponse domainToResponse(User user);
}

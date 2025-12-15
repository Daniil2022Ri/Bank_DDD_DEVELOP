package com.bank.authorization.mappers;

import com.bank.authorization.dtos.user.UserDto;
import com.bank.authorization.dtos.user.UserDtoForUpdate;
import com.bank.authorization.entities.User;
import lombok.Generated;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "profileId", target = "profile")
    UserDto toDto(User user);
    @Mapping(source = "profile", target = "profileId")
    User toEntity(UserDto userDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "profile", target = "profileId")
    void updateUserFromDto(UserDtoForUpdate dto, @MappingTarget User user);
}

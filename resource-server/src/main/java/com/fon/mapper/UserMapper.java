package com.fon.mapper;

import com.fon.dto.UserDto;
import com.fon.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toUser(UserDto userDto);

    UserDto toUserDto(User user);

    List<User> toList(List<UserDto> userDtoList);

    List<UserDto> toListDto(List<User> userList);

}

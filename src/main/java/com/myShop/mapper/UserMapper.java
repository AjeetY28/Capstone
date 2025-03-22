package com.myShop.mapper;

import com.myShop.dto.UserDto;
import com.myShop.entity.User;

public class UserMapper {

    public static UserDto toUserDto(User user){

        UserDto userDto=new UserDto();

        userDto.setId(user.getId());
        userDto.setFullName(user.getName());
        userDto.setEmail(user.getEmail());

        return userDto;
    }
}

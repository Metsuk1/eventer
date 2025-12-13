package org.eventer.utils;

import org.eventer.dto.UserDto;
import org.eventer.entity.User;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public UserDto toDto(User user) {
        if (user == null) return null;

        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUserName());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole());
        userDto.setGroupId(user.getGroupId());

        return userDto;
    }

    public User toUser(UserDto userDto) {
        if (userDto == null) return null;

        User user = new User();
        user.setUserName(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setRole(userDto.getRole());
        user.setGroupId(userDto.getGroupId());

        return user;
    }

}

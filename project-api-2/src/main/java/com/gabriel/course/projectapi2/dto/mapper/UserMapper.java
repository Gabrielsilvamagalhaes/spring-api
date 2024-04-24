package com.gabriel.course.projectapi2.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import com.gabriel.course.projectapi2.dto.Users.UserCreateDto;
import com.gabriel.course.projectapi2.dto.Users.UserResponseDto;
import com.gabriel.course.projectapi2.model.User;

public class UserMapper {

	public static User toUser(UserCreateDto userCreateDto) {
		return new ModelMapper().map(userCreateDto, User.class);

	}

	public static UserResponseDto toDto(User user) {
		String role = user.getRole().name().substring("ROLE_".length());

		PropertyMap<User, UserResponseDto> props = new PropertyMap<User, UserResponseDto>() {

			@Override
			protected void configure() {
				map().setRole(role);
			}
		};

		ModelMapper mapper = new ModelMapper();
		mapper.addMappings(props);

		return mapper.map(user, UserResponseDto.class);
	}

	public static List<UserResponseDto> toListDto(List<User> users) {
		return users.stream()
				.map( u -> toDto(u))
				.collect(Collectors.toList());

	}
}
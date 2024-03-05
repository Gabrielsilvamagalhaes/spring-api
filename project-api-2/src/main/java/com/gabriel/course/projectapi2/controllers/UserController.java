package com.gabriel.course.projectapi2.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabriel.course.projectapi2.dto.UserCreateDto;
import com.gabriel.course.projectapi2.dto.UserPassDto;
import com.gabriel.course.projectapi2.dto.UserResponseDto;
import com.gabriel.course.projectapi2.dto.mapper.UserMapper;
import com.gabriel.course.projectapi2.model.User;
import com.gabriel.course.projectapi2.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api/users")
public class UserController {
	
	@Autowired
	UserService userService;
	

	@GetMapping
	public ResponseEntity<List<UserResponseDto>> getAllUsers() {
		List<User> users = userService.findUsers();
		return ResponseEntity.ok(UserMapper.toListDto(users));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UserResponseDto> getById(@PathVariable Long id) {
		User user = userService.findById(id);
		return ResponseEntity.ok(UserMapper.toDto(user));
	}
	
	@PostMapping
	public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserCreateDto userCreateDto){
		User user = userService.createUser(UserMapper.toUser(userCreateDto));
		return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toDto(user));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Void> updateUser(@PathVariable Long id, @Valid @RequestBody UserPassDto userPassDto) {
		 userService.updatePassword(id, userPassDto.getCurrentPass(), userPassDto.getNewPass(), userPassDto.getConfirmPass());
		return ResponseEntity.noContent().build();
	}
}

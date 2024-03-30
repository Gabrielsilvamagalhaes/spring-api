package com.gabriel.course.projectapi2.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.gabriel.course.projectapi2.exceptions.ErrorMessage;
import com.gabriel.course.projectapi2.model.User;
import com.gabriel.course.projectapi2.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Usuários", description = "Realiza todas as operações de leitura, adição, edição e remoção do usuário.")
@RestController
@RequestMapping(value = "/api/users")
public class UserController {

	@Autowired
	UserService userService;


	@Operation(summary = "Localização de todos os usuários", description = "Recurso usado para localizar todos os usuários",
			responses = {
					@ApiResponse(responseCode = "200", description = "Usuários resgatados com sucesso!",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class)))
			})
	@GetMapping
	public ResponseEntity<List<UserResponseDto>> getAllUsers() {
		List<User> users = userService.findUsers();
		return ResponseEntity.ok(UserMapper.toListDto(users));
	}

	@Operation(summary = "Localização de um usuário", description = "Recurso usado para localizar um usuários por id",
			responses = {
					@ApiResponse(responseCode = "200", description = "Usuário resgatado com sucesso!",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
					@ApiResponse(responseCode = "404", description = "Usuário não encontrado!",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
			})
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN') OR ( hasRole('CLIENT') AND #id == authentication.principal.id)")
	public ResponseEntity<UserResponseDto> getById(@PathVariable Long id) {
		User user = userService.findById(id);
		return ResponseEntity.ok(UserMapper.toDto(user));
	}

	@Operation(summary = "Localização de um usuário", description = "Recurso usado para localizar um usuários por nome",
			responses = {
					@ApiResponse(responseCode = "200", description = "Usuário resgatado com sucesso!",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
					@ApiResponse(responseCode = "404", description = "Usuário não encontrado!",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
			})
	@GetMapping("/username/{username}")
	public ResponseEntity<UserResponseDto> getByUsername(@PathVariable String username) {
		var user = userService.findByUsername(username);
		return  ResponseEntity.ok(UserMapper.toDto(user));
	}

	@Operation(summary = "Criação de um novo usuário", description = "Recurso usado para criar um usuário",
			responses = {
					@ApiResponse(responseCode = "201", description = "Usuário criado com sucesso!",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class ))),
					@ApiResponse(responseCode = "409", description = "E-mail já cadastrado no sistema!",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
					@ApiResponse(responseCode = "422", description = "Recurso não processo por dados de entrada inválidos!",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
			})
	@PostMapping
	public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserCreateDto userCreateDto){
		User user = userService.createUser(UserMapper.toUser(userCreateDto));
		return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toDto(user));
	}

	@Operation(summary = "Edição de senha de um usuário", description = "Recurso usado para editar a senha de um usuário",
			responses = {
					@ApiResponse(responseCode = "204", description = "Senha alterada com sucesso!",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
					@ApiResponse(responseCode = "400", description = "Senha inserida incompátivel!",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
					@ApiResponse(responseCode = "404", description = "Usuário não encontrado!",
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
			})
	@PutMapping("/{id}")
	public ResponseEntity<Void> updateUser(@PathVariable Long id, @Valid @RequestBody UserPassDto userPassDto) {
		userService.updatePassword(id, userPassDto.getCurrentPass(), userPassDto.getNewPass(), userPassDto.getConfirmPass());
		return ResponseEntity.noContent().build();
	}
}
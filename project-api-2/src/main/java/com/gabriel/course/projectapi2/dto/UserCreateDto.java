package com.gabriel.course.projectapi2.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserCreateDto {

	@NotBlank
	@Email(message = "Formato de e-mail inválido!")
	private String username;
	
	@NotBlank
	@Size(min = 6, max = 6)
	private String password;
}

package com.gabriel.course.projectapi2.dto.Users;

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
public class UserPassDto {

	@NotBlank
	@Size(min = 6, max = 6)
	private String currentPass;
	@NotBlank
	@Size(min = 6, max = 6)
	private String newPass;
	@NotBlank
	@Size(min = 6, max = 6)
	private String confirmPass;
}

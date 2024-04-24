package com.gabriel.course.projectapi2.dto.Users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserLoginDto {

    @NotBlank
    @Email(message = "Formato de e-mail inválido!")
    private String username;

    @NotBlank
    @Size(min = 6, max = 6)
    private String password;

}

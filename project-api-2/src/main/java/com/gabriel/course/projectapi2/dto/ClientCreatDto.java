package com.gabriel.course.projectapi2.dto;

import com.gabriel.course.projectapi2.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ClientCreatDto {

    @NotBlank
    @CPF(message = "CPF inválido!")
    @Size(min = 11, max = 11)
    private String cpf;

    @NotBlank
    @Size(min = 5)
    private String name;

}

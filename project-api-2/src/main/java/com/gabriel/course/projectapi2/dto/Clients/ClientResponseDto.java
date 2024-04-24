package com.gabriel.course.projectapi2.dto.Clients;

import com.gabriel.course.projectapi2.model.User;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ClientResponseDto {

    private Long id;
    private String name;
    private String cpf;
}

package com.gabriel.course.projectapi2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ParkingCreateDto {
    @NotBlank
    private String mark;
    @NotBlank
    private String color;
    @NotBlank
    private String model;
    @NotBlank
    @Size(min = 8, max = 8)
    @Pattern(regexp =  "[A-Z]{3}-[0-9]{4}", message = "A placa do veículo deve ser o padrão 'XXX-0000")
    private String plate;
    @NotBlank
    @Size(min = 11, max = 11)
    @CPF
    private String clientCpf;
}

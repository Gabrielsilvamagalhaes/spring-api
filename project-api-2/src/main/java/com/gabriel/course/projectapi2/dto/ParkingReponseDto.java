package com.gabriel.course.projectapi2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ParkingReponseDto {
    private String mark;
    private String color;
    private String model;
    private String plate;
    private String clientCpf;
    private String receipt;
    private LocalDateTime enter_date;
    private LocalDateTime exit_date;
    private String vacancyCode;
    private BigDecimal value;
    private BigDecimal discount;
}

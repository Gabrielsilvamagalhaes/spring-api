package com.gabriel.course.projectapi2.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParkingResponseDto {
    private String mark;
    private String color;
    private String model;
    private String plate;
    private String clientCpf;
    private String receipt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime enterDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime exitDate;
    private String vacancyCode;
    private BigDecimal ammount;
    private BigDecimal discount;
}

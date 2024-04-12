package com.gabriel.course.projectapi2.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class VacancyResponseDto {
    private Long id;
    private String code;
    private String status;
}
